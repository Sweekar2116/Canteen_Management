package com.canteen.controller;

import com.canteen.dto.MenuItemResponse;
import com.canteen.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Tag(name = "Menu", description = "Public Food Menu browsing, search and filter APIs")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    @Operation(summary = "Search and filter menu items with pagination")
    public ResponseEntity<Page<MenuItemResponse>> searchMenuItems(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Boolean vegetarian,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "12") int size,
        @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        String sortField = sort[0];
        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<MenuItemResponse> results = menuItemService.searchMenuItems(query, categoryId, vegetarian, minPrice, maxPrice, true, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all available menu items as a flat list")
    public ResponseEntity<List<MenuItemResponse>> getAllAvailable() {
        return ResponseEntity.ok(menuItemService.getAllAvailable());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu item by ID")
    public ResponseEntity<MenuItemResponse> getMenuItemById(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }
}
