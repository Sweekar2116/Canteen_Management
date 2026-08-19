package com.canteen.service;

import com.canteen.dto.CartItemRequest;
import com.canteen.dto.CartItemResponse;
import com.canteen.dto.CartResponse;
import com.canteen.entity.*;
import com.canteen.exception.BadRequestException;
import com.canteen.exception.ResourceNotFoundException;
import com.canteen.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;

    public CartService(
        CartRepository cartRepository,
        CartItemRepository cartItemRepository,
        MenuItemRepository menuItemRepository,
        UserRepository userRepository,
        InventoryRepository inventoryRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public CartResponse getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse addItemToCart(Long userId, CartItemRequest request) {
        Cart cart = getOrCreateCart(userId);
        MenuItem item = menuItemRepository.findById(request.getMenuItemId())
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + request.getMenuItemId()));

        if (!item.isAvailable()) {
            throw new BadRequestException("Item '" + item.getName() + "' is currently unavailable");
        }

        // Validate inventory
        inventoryRepository.findByMenuItemId(item.getId()).ifPresent(inv -> {
            if (inv.getQuantity() < request.getQuantity()) {
                throw new BadRequestException("Only " + inv.getQuantity() + " units of '" + item.getName() + "' available in stock");
            }
        });

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), item.getId());
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            int newQty = cartItem.getQuantity() + request.getQuantity();

            // Check total against stock
            inventoryRepository.findByMenuItemId(item.getId()).ifPresent(inv -> {
                if (inv.getQuantity() < newQty) {
                    throw new BadRequestException("Cannot add more: total would exceed available stock (" + inv.getQuantity() + ")");
                }
            });

            cartItem.setQuantity(newQty);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newItem = new CartItem(cart, item, request.getQuantity());
            cartItemRepository.save(newItem);
        }

        // Reload cart
        Cart updatedCart = cartRepository.findById(cart.getId()).orElse(cart);
        return mapToResponse(updatedCart);
    }

    @Transactional
    public CartResponse updateItemQuantity(Long userId, Long cartItemId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to your cart");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            inventoryRepository.findByMenuItemId(cartItem.getMenuItem().getId()).ifPresent(inv -> {
                if (inv.getQuantity() < quantity) {
                    throw new BadRequestException("Only " + inv.getQuantity() + " units in stock");
                }
            });
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        Cart updatedCart = cartRepository.findById(cart.getId()).orElse(cart);
        return mapToResponse(updatedCart);
    }

    @Transactional
    public CartResponse removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to your cart");
        }

        cartItemRepository.delete(cartItem);
        Cart updatedCart = cartRepository.findById(cart.getId()).orElse(cart);
        return mapToResponse(updatedCart);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCartId(cart.getId());
    }

    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
            Cart newCart = new Cart(user);
            return cartRepository.save(newCart);
        });
    }

    public CartResponse mapToResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());

        var itemResponses = cart.getItems().stream().map(item -> {
            CartItemResponse res = new CartItemResponse();
            res.setId(item.getId());
            res.setMenuItemId(item.getMenuItem().getId());
            res.setItemName(item.getMenuItem().getName());
            res.setImageUrl(item.getMenuItem().getImageUrl());
            res.setUnitPrice(item.getMenuItem().getPrice());
            res.setQuantity(item.getQuantity());
            res.setTotalPrice(item.getMenuItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            res.setAvailable(item.getMenuItem().isAvailable());

            inventoryRepository.findByMenuItemId(item.getMenuItem().getId())
                .ifPresent(inv -> res.setStockQuantity(inv.getQuantity()));

            return res;
        }).collect(Collectors.toList());

        response.setItems(itemResponses);

        BigDecimal subtotal = itemResponses.stream()
            .map(CartItemResponse::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 5% GST tax simulation
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(tax);

        response.setTotalItems(itemResponses.stream().mapToInt(CartItemResponse::getQuantity).sum());
        response.setSubtotal(subtotal);
        response.setTax(tax);
        response.setTotal(total);

        return response;
    }
}
