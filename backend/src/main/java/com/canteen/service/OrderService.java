package com.canteen.service;

import com.canteen.dto.CreateOrderRequest;
import com.canteen.dto.OrderItemResponse;
import com.canteen.dto.OrderResponse;
import com.canteen.dto.OrderStatusUpdateRequest;
import com.canteen.entity.*;
import com.canteen.exception.BadRequestException;
import com.canteen.exception.ResourceNotFoundException;
import com.canteen.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final InventoryRepository inventoryRepository;
    private final NotificationService notificationService;
    private final CouponService couponService;
    private final AuditLogService auditLogService;

    public OrderService(
        OrderRepository orderRepository,
        CartRepository cartRepository,
        CartItemRepository cartItemRepository,
        UserRepository userRepository,
        CouponRepository couponRepository,
        InventoryRepository inventoryRepository,
        NotificationService notificationService,
        CouponService couponService,
        AuditLogService auditLogService
    ) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.inventoryRepository = inventoryRepository;
        this.notificationService = notificationService;
        this.couponService = couponService;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new BadRequestException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cannot checkout: cart is empty");
        }

        // Validate stock for all items
        for (CartItem cartItem : cart.getItems()) {
            MenuItem item = cartItem.getMenuItem();
            if (!item.isAvailable()) {
                throw new BadRequestException("Item '" + item.getName() + "' is no longer available");
            }
            inventoryRepository.findByMenuItemId(item.getId()).ifPresent(inv -> {
                if (inv.getQuantity() < cartItem.getQuantity()) {
                    throw new BadRequestException("Insufficient stock for '" + item.getName() + "'. Available: " + inv.getQuantity());
                }
            });
        }

        // Calculate totals
        BigDecimal subtotal = cart.getItems().stream()
            .map(ci -> ci.getMenuItem().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = BigDecimal.ZERO;
        String couponCode = null;

        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            Coupon coupon = couponRepository.findByCodeIgnoreCaseAndActiveTrue(request.getCouponCode().trim())
                .orElseThrow(() -> new BadRequestException("Invalid coupon: " + request.getCouponCode()));

            discount = couponService.calculateDiscount(coupon, subtotal);
            couponCode = coupon.getCode();
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            couponRepository.save(coupon);
        }

        BigDecimal discountedSubtotal = subtotal.subtract(discount);
        if (discountedSubtotal.compareTo(BigDecimal.ZERO) < 0) {
            discountedSubtotal = BigDecimal.ZERO;
        }

        BigDecimal tax = discountedSubtotal.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalAmount = discountedSubtotal.add(tax);

        // Generate unique order number
        String orderNumber = "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                             "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PLACED);
        order.setTotalAmount(subtotal);
        order.setDiscountAmount(discount);
        order.setTaxAmount(tax);
        order.setFinalAmount(finalAmount);
        order.setCouponCode(couponCode);
        order.setPickupTime(request.getPickupTime() != null ? request.getPickupTime() : LocalDateTime.now().plusMinutes(25));
        order.setNotes(request.getNotes());

        // Create order items
        List<OrderItem> orderItems = cart.getItems().stream().map(ci -> {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setMenuItem(ci.getMenuItem());
            oi.setItemName(ci.getMenuItem().getName());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getMenuItem().getPrice());
            oi.setTotalPrice(ci.getMenuItem().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
            return oi;
        }).collect(Collectors.toList());
        order.setItems(orderItems);

        // Create payment record
        Payment.PaymentStatus paymentStatus = request.getPaymentMethod() == Payment.PaymentMethod.CASH_ON_PICKUP
            ? Payment.PaymentStatus.PENDING
            : Payment.PaymentStatus.COMPLETED;

        String txRef = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Payment payment = new Payment(order, finalAmount, request.getPaymentMethod(), paymentStatus);
        payment.setTransactionRef(txRef);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        // Deduct inventory
        for (CartItem cartItem : cart.getItems()) {
            inventoryRepository.findByMenuItemId(cartItem.getMenuItem().getId()).ifPresent(inv -> {
                inv.setQuantity(Math.max(0, inv.getQuantity() - cartItem.getQuantity()));
                inventoryRepository.save(inv);
            });
        }

        // Clear user's cart
        cart.getItems().clear();
        cartItemRepository.deleteByCartId(cart.getId());
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        // Send notification
        notificationService.createNotification(
            user,
            "Order Placed Successfully",
            "Your order #" + savedOrder.getOrderNumber() + " for ₹" + finalAmount + " has been placed."
        );

        auditLogService.log(userId, "CREATE_ORDER", "Order", savedOrder.getId(),
            "Order placed: " + savedOrder.getOrderNumber() + " with amount ₹" + finalAmount);

        return mapToResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getCustomerOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId, Long userId, boolean isAdmin) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!isAdmin && !order.getUser().getId().equals(userId)) {
            throw new BadRequestException("You do not have access to view this order");
        }

        return mapToResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> searchOrders(
        Order.OrderStatus status,
        Long userId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable
    ) {
        return orderRepository.searchOrders(status, userId, startDate, endDate, pageable)
            .map(this::mapToResponse);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long staffId, Long orderId, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        Order.OrderStatus oldStatus = order.getStatus();
        Order.OrderStatus newStatus = request.getStatus();

        // Validate state transitions
        if (oldStatus == Order.OrderStatus.COMPLETED || oldStatus == Order.OrderStatus.CANCELLED) {
            throw new BadRequestException("Cannot modify order in final status: " + oldStatus);
        }

        order.setStatus(newStatus);

        // If completed and cash on pickup, update payment status to completed
        if (newStatus == Order.OrderStatus.COMPLETED && order.getPayment() != null) {
            order.getPayment().setStatus(Payment.PaymentStatus.COMPLETED);
        }

        // If cancelled, restore inventory
        if (newStatus == Order.OrderStatus.CANCELLED) {
            for (OrderItem item : order.getItems()) {
                inventoryRepository.findByMenuItemId(item.getMenuItem().getId()).ifPresent(inv -> {
                    inv.setQuantity(inv.getQuantity() + item.getQuantity());
                    inventoryRepository.save(inv);
                });
            }
            if (order.getPayment() != null && order.getPayment().getStatus() == Payment.PaymentStatus.COMPLETED) {
                order.getPayment().setStatus(Payment.PaymentStatus.REFUNDED);
            }
        }

        Order updated = orderRepository.save(order);

        // Send notification to customer
        notificationService.createNotification(
            order.getUser(),
            "Order Status: " + newStatus,
            "Your order #" + order.getOrderNumber() + " is now " + newStatus.name().replace('_', ' ') + "."
        );

        auditLogService.log(staffId, "UPDATE_ORDER_STATUS", "Order", orderId,
            "Order status changed from " + oldStatus + " to " + newStatus);

        return mapToResponse(updated);
    }

    @Transactional
    public OrderResponse cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException("You can only cancel your own orders");
        }

        if (order.getStatus() != Order.OrderStatus.PLACED) {
            throw new BadRequestException("Cannot cancel order once it is " + order.getStatus());
        }

        OrderStatusUpdateRequest req = new OrderStatusUpdateRequest(Order.OrderStatus.CANCELLED);
        return updateOrderStatus(userId, orderId, req);
    }

    public OrderResponse mapToResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setId(order.getId());
        res.setOrderNumber(order.getOrderNumber());
        res.setUserId(order.getUser().getId());
        res.setUserName(order.getUser().getName());
        res.setUserEmail(order.getUser().getEmail());
        res.setUserPhone(order.getUser().getPhone());
        res.setStatus(order.getStatus());
        res.setTotalAmount(order.getTotalAmount());
        res.setDiscountAmount(order.getDiscountAmount());
        res.setTaxAmount(order.getTaxAmount());
        res.setFinalAmount(order.getFinalAmount());
        res.setCouponCode(order.getCouponCode());
        res.setPickupTime(order.getPickupTime());
        res.setNotes(order.getNotes());
        res.setCreatedAt(order.getCreatedAt());
        res.setUpdatedAt(order.getUpdatedAt());

        if (order.getPayment() != null) {
            res.setPaymentMethod(order.getPayment().getMethod());
            res.setPaymentStatus(order.getPayment().getStatus());
            res.setTransactionRef(order.getPayment().getTransactionRef());
        }

        var itemResponses = order.getItems().stream().map(oi -> {
            OrderItemResponse r = new OrderItemResponse();
            r.setId(oi.getId());
            r.setMenuItemId(oi.getMenuItem().getId());
            r.setItemName(oi.getItemName());
            r.setImageUrl(oi.getMenuItem().getImageUrl());
            r.setQuantity(oi.getQuantity());
            r.setUnitPrice(oi.getUnitPrice());
            r.setTotalPrice(oi.getTotalPrice());
            return r;
        }).collect(Collectors.toList());

        res.setItems(itemResponses);
        return res;
    }
}
