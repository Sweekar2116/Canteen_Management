package com.canteen.service;

import com.canteen.dto.InventoryResponse;
import com.canteen.dto.UpdateInventoryRequest;
import com.canteen.entity.Inventory;
import com.canteen.exception.ResourceNotFoundException;
import com.canteen.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final AuditLogService auditLogService;

    public InventoryService(InventoryRepository inventoryRepository, AuditLogService auditLogService) {
        this.inventoryRepository = inventoryRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> getLowStockItems() {
        return inventoryRepository.findLowStockItems().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public InventoryResponse updateInventory(Long adminId, Long inventoryId, UpdateInventoryRequest request) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory record not found with id: " + inventoryId));

        inventory.setQuantity(request.getQuantity());
        if (request.getMinStockLevel() != null) {
            inventory.setMinStockLevel(request.getMinStockLevel());
        }
        if (request.getUnit() != null) {
            inventory.setUnit(request.getUnit());
        }

        Inventory updated = inventoryRepository.save(inventory);

        auditLogService.log(adminId, "UPDATE_INVENTORY", "Inventory", inventoryId,
            "Updated stock for " + inventory.getMenuItem().getName() + " to " + request.getQuantity());

        return mapToResponse(updated);
    }

    private InventoryResponse mapToResponse(Inventory inv) {
        InventoryResponse res = new InventoryResponse();
        res.setId(inv.getId());
        res.setMenuItemId(inv.getMenuItem().getId());
        res.setItemName(inv.getMenuItem().getName());
        res.setCategoryName(inv.getMenuItem().getCategory() != null ? inv.getMenuItem().getCategory().getName() : null);
        res.setQuantity(inv.getQuantity());
        res.setUnit(inv.getUnit());
        res.setMinStockLevel(inv.getMinStockLevel());
        res.setLowStock(inv.getQuantity() <= inv.getMinStockLevel());
        res.setLastUpdated(inv.getLastUpdated());
        return res;
    }
}
