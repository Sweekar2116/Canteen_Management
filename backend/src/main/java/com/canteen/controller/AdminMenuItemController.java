package com.canteen.controller;

import com.canteen.dto.CategoryRequest;
import com.canteen.dto.CategoryResponse;
import com.canteen.dto.MenuItemRequest;
import com.canteen.dto.MenuItemResponse;
import com.canteen.security.UserPrincipal;
import com.canteen.service.CategoryService;
import com.canteen.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/menu")
@Tag(name = "Admin Menu Management", description = "Admin Menu CRUD & Category Management APIs")
public class AdminMenuItemController {

    private final MenuItemService menuItemService;
    private final CategoryService categoryService;

    public AdminMenuItemController(MenuItemService menuItemService, CategoryService categoryService) {
        this.menuItemService = menuItemService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all menu items (including unavailable) with filters and pagination")
    public ResponseEntity<Page<MenuItemResponse>> getAllMenuItems(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Boolean vegetarian,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        String sortField = sort[0];
        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return ResponseEntity.ok(menuItemService.searchMenuItems(query, categoryId, vegetarian, minPrice, maxPrice, false, pageable));
    }

    @PostMapping
    @Operation(summary = "Create a new food item")
    public ResponseEntity<MenuItemResponse> createMenuItem(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @Valid @RequestBody MenuItemRequest request
    ) {
        MenuItemResponse response = menuItemService.createMenuItem(currentUser.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing food item")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id,
        @Valid @RequestBody MenuItemRequest request
    ) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(currentUser.getId(), id, request));
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Toggle availability on/off for a food item")
    public ResponseEntity<MenuItemResponse> toggleAvailability(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(menuItemService.toggleAvailability(currentUser.getId(), id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete / deactivate a food item")
    public ResponseEntity<Void> deleteMenuItem(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id
    ) {
        menuItemService.deleteMenuItem(currentUser.getId(), id);
        return ResponseEntity.noContent().build();
    }

    // Categories admin endpoints
    @GetMapping("/categories")
    @Operation(summary = "Get all categories (including inactive)")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/categories")
    @Operation(summary = "Create a new category")
    public ResponseEntity<CategoryResponse> createCategory(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @Valid @RequestBody CategoryRequest request
    ) {
        return new ResponseEntity<>(categoryService.createCategory(currentUser.getId(), request), HttpStatus.CREATED);
    }

    @PutMapping("/categories/{id}")
    @Operation(summary = "Update a category")
    public ResponseEntity<CategoryResponse> updateCategory(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id,
        @Valid @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(currentUser.getId(), id, request));
    }

    @DeleteMapping("/categories/{id}")
    @Operation(summary = "Deactivate a category")
    public ResponseEntity<Void> deleteCategory(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id
    ) {
        categoryService.deleteCategory(currentUser.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
