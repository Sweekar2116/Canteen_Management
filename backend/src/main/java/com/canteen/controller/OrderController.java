package com.canteen.controller;

import com.canteen.dto.CreateOrderRequest;
import com.canteen.dto.OrderResponse;
import com.canteen.security.UserPrincipal;
import com.canteen.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Customer Order placement, tracking and history")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Place a new order from active shopping cart")
    public ResponseEntity<OrderResponse> createOrder(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @Valid @RequestBody CreateOrderRequest request
    ) {
        OrderResponse response = orderService.createOrder(currentUser.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get current user's order history")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(orderService.getCustomerOrders(currentUser.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order details and timeline tracking by ID")
    public ResponseEntity<OrderResponse> getOrderById(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(orderService.getOrderById(id, currentUser.getId(), false));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an order if still in PLACED state")
    public ResponseEntity<OrderResponse> cancelOrder(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(orderService.cancelOrder(currentUser.getId(), id));
    }
}
