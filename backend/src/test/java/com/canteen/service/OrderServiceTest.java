package com.canteen.service;

import com.canteen.dto.CreateOrderRequest;
import com.canteen.dto.OrderResponse;
import com.canteen.dto.OrderStatusUpdateRequest;
import com.canteen.entity.*;
import com.canteen.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private CouponService couponService;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private OrderService orderService;

    private User sampleUser;
    private Cart sampleCart;
    private MenuItem sampleItem;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        sampleUser = new User("Rahul Sharma", "rahul@example.com", "9876543210", "password");
        sampleUser.setId(3L);

        sampleItem = new MenuItem("Masala Dosa", "Crispy crepe", new BigDecimal("80.00"), null, true);
        sampleItem.setId(1L);
        sampleItem.setAvailable(true);

        sampleCart = new Cart(sampleUser);
        sampleCart.setId(1L);
        CartItem cartItem = new CartItem(sampleCart, sampleItem, 2);
        sampleCart.getItems().add(cartItem);

        sampleOrder = new Order(sampleUser, "ORD-2026-0001", new BigDecimal("160.00"), OrderStatus.PLACED);
        sampleOrder.setId(10L);
    }

    @Test
    void testCreateOrderSuccess() {
        CreateOrderRequest req = new CreateOrderRequest();
        req.setPaymentMethod(PaymentMethod.UPI);

        Inventory inv = new Inventory(sampleItem, 50, "plates", 10);

        when(userRepository.findById(3L)).thenReturn(Optional.of(sampleUser));
        when(cartRepository.findByUserId(3L)).thenReturn(Optional.of(sampleCart));
        when(inventoryRepository.findByMenuItemId(1L)).thenReturn(Optional.of(inv));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(100L);
            return o;
        });

        OrderResponse res = orderService.createOrder(3L, req);

        assertNotNull(res);
        assertEquals(OrderStatus.PLACED, res.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartItemRepository, times(1)).deleteByCartId(1L);
    }

    @Test
    void testUpdateOrderStatus() {
        OrderStatusUpdateRequest req = new OrderStatusUpdateRequest();
        req.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById(10L)).thenReturn(Optional.of(sampleOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse res = orderService.updateOrderStatus(10L, req, 1L);

        assertNotNull(res);
        assertEquals(OrderStatus.CONFIRMED, res.getStatus());
        verify(auditLogService, times(1)).log(eq(1L), eq("UPDATE_ORDER_STATUS"), eq("Order"), eq(10L), anyString());
    }
}
