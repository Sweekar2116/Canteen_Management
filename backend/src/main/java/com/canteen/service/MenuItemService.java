package com.canteen.service;

import com.canteen.dto.MenuItemRequest;
import com.canteen.dto.MenuItemResponse;
import com.canteen.entity.Category;
import com.canteen.entity.Inventory;
import com.canteen.entity.MenuItem;
import com.canteen.exception.ResourceNotFoundException;
import com.canteen.repository.CategoryRepository;
import com.canteen.repository.InventoryRepository;
import com.canteen.repository.MenuItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final AuditLogService auditLogService;

    public MenuItemService(
        MenuItemRepository menuItemRepository,
        CategoryRepository categoryRepository,
        InventoryRepository inventoryRepository,
        AuditLogService auditLogService
    ) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional(readOnly = true)
    public Page<MenuItemResponse> searchMenuItems(
        String query,
        Long categoryId,
        Boolean vegetarian,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        boolean availableOnly,
        Pageable pageable
    ) {
        return menuItemRepository.searchMenuItems(query, categoryId, vegetarian, minPrice, maxPrice, availableOnly, pageable)
            .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> getAllAvailable() {
        return menuItemRepository.findByAvailableTrue().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItemById(Long id) {
        MenuItem item = getEntity(id);
        return mapToResponse(item);
    }

    @Transactional
    public MenuItemResponse createMenuItem(Long adminId, MenuItemRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        MenuItem item = new MenuItem(
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            category,
            request.isVegetarian()
        );
        item.setImageUrl(request.getImageUrl());
        item.setAvailable(request.isAvailable());
        item.setPreparationTime(request.getPreparationTime());

        MenuItem saved = menuItemRepository.save(item);

        // Auto-create inventory record
        Inventory inventory = new Inventory(saved, 100, "plates", 15);
        inventoryRepository.save(inventory);

        auditLogService.log(adminId, "CREATE_MENU_ITEM", "MenuItem", saved.getId(), "Created menu item: " + saved.getName());
        return mapToResponse(saved);
    }

    @Transactional
    public MenuItemResponse updateMenuItem(Long adminId, Long id, MenuItemRequest request) {
        MenuItem item = getEntity(id);
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(category);
        item.setImageUrl(request.getImageUrl());
        item.setAvailable(request.isAvailable());
        item.setVegetarian(request.isVegetarian());
        item.setPreparationTime(request.getPreparationTime());

        MenuItem updated = menuItemRepository.save(item);

        auditLogService.log(adminId, "UPDATE_MENU_ITEM", "MenuItem", updated.getId(), "Updated menu item: " + updated.getName());
        return mapToResponse(updated);
    }

    @Transactional
    public MenuItemResponse toggleAvailability(Long adminId, Long id) {
        MenuItem item = getEntity(id);
        item.setAvailable(!item.isAvailable());
        MenuItem updated = menuItemRepository.save(item);

        auditLogService.log(adminId, "TOGGLE_ITEM_AVAILABILITY", "MenuItem", id,
            "Availability toggled to: " + updated.isAvailable());
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteMenuItem(Long adminId, Long id) {
        MenuItem item = getEntity(id);
        item.setAvailable(false);
        menuItemRepository.save(item);

        auditLogService.log(adminId, "DELETE_MENU_ITEM", "MenuItem", id, "Deactivated menu item: " + item.getName());
    }

    public MenuItem getEntity(Long id) {
        return menuItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }

    public MenuItemResponse mapToResponse(MenuItem item) {
        MenuItemResponse res = new MenuItemResponse();
        res.setId(item.getId());
        res.setName(item.getName());
        res.setDescription(item.getDescription());
        res.setPrice(item.getPrice());
        res.setImageUrl(item.getImageUrl());
        res.setCategoryId(item.getCategory() != null ? item.getCategory().getId() : null);
        res.setCategoryName(item.getCategory() != null ? item.getCategory().getName() : null);
        res.setAvailable(item.isAvailable());
        res.setVegetarian(item.isVegetarian());
        res.setRating(item.getRating());
        res.setRatingCount(item.getRatingCount());
        res.setPreparationTime(item.getPreparationTime());

        inventoryRepository.findByMenuItemId(item.getId())
            .ifPresent(inv -> res.setStockQuantity(inv.getQuantity()));

        return res;
    }
}
