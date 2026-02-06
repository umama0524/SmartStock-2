package com.smartstock.dto;

import com.smartstock.entity.Inventory;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryDto {
    
    private Long id;
    private String sku;
    private String productName;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private Integer reorderPoint;
    private Integer safetyStock;
    private String status;
    private String location;
    private BigDecimal unitPrice;
    private LocalDateTime lastUpdated;

    public InventoryDto() {}

    public InventoryDto(Long id, String sku, String productName, Integer quantity, 
                        Integer reservedQuantity, Integer availableQuantity,
                        Integer reorderPoint, Integer safetyStock, String status,
                        String location, BigDecimal unitPrice, LocalDateTime lastUpdated) {
        this.id = id;
        this.sku = sku;
        this.productName = productName;
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.availableQuantity = availableQuantity;
        this.reorderPoint = reorderPoint;
        this.safetyStock = safetyStock;
        this.status = status;
        this.location = location;
        this.unitPrice = unitPrice;
        this.lastUpdated = lastUpdated;
    }
    
    public static InventoryDto fromEntity(Inventory inventory) {
        InventoryDto dto = new InventoryDto();
        dto.id = inventory.getId();
        dto.sku = inventory.getProduct().getSku();
        dto.productName = inventory.getProduct().getProductName();
        dto.quantity = inventory.getQuantity();
        dto.reservedQuantity = inventory.getReservedQuantity();
        dto.availableQuantity = inventory.getAvailableQuantity();
        dto.reorderPoint = inventory.getProduct().getReorderPoint();
        dto.safetyStock = inventory.getProduct().getSafetyStock();
        dto.status = inventory.getStatus().name().toLowerCase();
        dto.location = inventory.getProduct().getLocation();
        dto.unitPrice = inventory.getProduct().getUnitPrice();
        dto.lastUpdated = inventory.getLastUpdated();
        return dto;
    }

    // Getters
    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public Integer getReservedQuantity() { return reservedQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public Integer getReorderPoint() { return reorderPoint; }
    public Integer getSafetyStock() { return safetyStock; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSku(String sku) { this.sku = sku; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
    public void setReorderPoint(Integer reorderPoint) { this.reorderPoint = reorderPoint; }
    public void setSafetyStock(Integer safetyStock) { this.safetyStock = safetyStock; }
    public void setStatus(String status) { this.status = status; }
    public void setLocation(String location) { this.location = location; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    // Builder
    public static InventoryDtoBuilder builder() { return new InventoryDtoBuilder(); }

    public static class InventoryDtoBuilder {
        private Long id;
        private String sku;
        private String productName;
        private Integer quantity;
        private Integer reservedQuantity;
        private Integer availableQuantity;
        private Integer reorderPoint;
        private Integer safetyStock;
        private String status;
        private String location;
        private BigDecimal unitPrice;
        private LocalDateTime lastUpdated;

        public InventoryDtoBuilder id(Long id) { this.id = id; return this; }
        public InventoryDtoBuilder sku(String sku) { this.sku = sku; return this; }
        public InventoryDtoBuilder productName(String productName) { this.productName = productName; return this; }
        public InventoryDtoBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public InventoryDtoBuilder reservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; return this; }
        public InventoryDtoBuilder availableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; return this; }
        public InventoryDtoBuilder reorderPoint(Integer reorderPoint) { this.reorderPoint = reorderPoint; return this; }
        public InventoryDtoBuilder safetyStock(Integer safetyStock) { this.safetyStock = safetyStock; return this; }
        public InventoryDtoBuilder status(String status) { this.status = status; return this; }
        public InventoryDtoBuilder location(String location) { this.location = location; return this; }
        public InventoryDtoBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public InventoryDtoBuilder lastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; return this; }

        public InventoryDto build() {
            return new InventoryDto(id, sku, productName, quantity, reservedQuantity,
                    availableQuantity, reorderPoint, safetyStock, status, location, unitPrice, lastUpdated);
        }
    }
}
