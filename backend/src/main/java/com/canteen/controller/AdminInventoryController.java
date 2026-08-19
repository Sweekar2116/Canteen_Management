package com.canteen.controller;

import com.canteen.dto.InventoryResponse;
import com.canteen.dto.UpdateInventoryRequest;
import com.canteen.security.UserPrincipal;
import com.canteen.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inventory")
@Tag(name = "Admin Inventory Management", description = "Stock tracking and threshold alerting APIs")
public class AdminInventoryController {

    private final InventoryService inventoryService;

    public AdminInventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    @Operation(summary = "Get all inventory stock levels")
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get items currently below their minimum stock threshold")
    public ResponseEntity<List<InventoryResponse>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update inventory quantity or minimum threshold")
    public ResponseEntity<InventoryResponse> updateInventory(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id,
        @Valid @RequestBody UpdateInventoryRequest request
    ) {
        return ResponseEntity.ok(inventoryService.updateInventory(currentUser.getId(), id, request));
    }
}
