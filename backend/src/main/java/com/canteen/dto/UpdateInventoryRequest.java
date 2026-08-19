package com.canteen.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateInventoryRequest {

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 1, message = "Minimum stock level must be at least 1")
    private Integer minStockLevel;

    private String unit;

    public UpdateInventoryRequest() {}

    public UpdateInventoryRequest(Integer quantity, Integer minStockLevel, String unit) {
        this.quantity = quantity;
        this.minStockLevel = minStockLevel;
        this.unit = unit;
    }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getMinStockLevel() { return minStockLevel; }
    public void setMinStockLevel(Integer minStockLevel) { this.minStockLevel = minStockLevel; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
