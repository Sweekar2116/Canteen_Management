package com.canteen.controller;

import com.canteen.dto.CartItemRequest;
import com.canteen.dto.CartResponse;
import com.canteen.security.UserPrincipal;
import com.canteen.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Shopping cart operations for authenticated customers")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "Get current authenticated user's cart")
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(cartService.getCart(currentUser.getId()));
    }

    @PostMapping("/items")
    @Operation(summary = "Add an item to shopping cart")
    public ResponseEntity<CartResponse> addItem(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @Valid @RequestBody CartItemRequest request
    ) {
        return ResponseEntity.ok(cartService.addItemToCart(currentUser.getId(), request));
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update item quantity in cart")
    public ResponseEntity<CartResponse> updateItemQuantity(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long cartItemId,
        @RequestParam int quantity
    ) {
        return ResponseEntity.ok(cartService.updateItemQuantity(currentUser.getId(), cartItemId, quantity));
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Remove an item from cart")
    public ResponseEntity<CartResponse> removeItem(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long cartItemId
    ) {
        return ResponseEntity.ok(cartService.removeItemFromCart(currentUser.getId(), cartItemId));
    }

    @DeleteMapping
    @Operation(summary = "Clear all items from cart")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserPrincipal currentUser) {
        cartService.clearCart(currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
