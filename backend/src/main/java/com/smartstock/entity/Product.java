package com.smartstock.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String sku;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(length = 1000)
    private String description;

    @Column(length = 100)
    private String category;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(length = 20)
    private String unit;

    @Column(name = "reorder_point")
    private Integer reorderPoint;

    @Column(name = "safety_stock")
    private Integer safetyStock;

    @Column(length = 100)
    private String location;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Product() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public String getUnit() { return unit; }
    public Integer getReorderPoint() { return reorderPoint; }
    public Integer getSafetyStock() { return safetyStock; }
    public String getLocation() { return location; }
    public Boolean getIsActive() { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSku(String sku) { this.sku = sku; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setReorderPoint(Integer reorderPoint) { this.reorderPoint = reorderPoint; }
    public void setSafetyStock(Integer safetyStock) { this.safetyStock = safetyStock; }
    public void setLocation(String location) { this.location = location; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Builder
    public static ProductBuilder builder() { return new ProductBuilder(); }

    public static class ProductBuilder {
        private Long id;
        private String sku;
        private String productName;
        private String description;
        private String category;
        private BigDecimal unitPrice;
        private String unit;
        private Integer reorderPoint;
        private Integer safetyStock;
        private String location;
        private Boolean isActive = true;

        public ProductBuilder id(Long id) { this.id = id; return this; }
        public ProductBuilder sku(String sku) { this.sku = sku; return this; }
        public ProductBuilder productName(String productName) { this.productName = productName; return this; }
        public ProductBuilder description(String description) { this.description = description; return this; }
        public ProductBuilder category(String category) { this.category = category; return this; }
        public ProductBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public ProductBuilder unit(String unit) { this.unit = unit; return this; }
        public ProductBuilder reorderPoint(Integer reorderPoint) { this.reorderPoint = reorderPoint; return this; }
        public ProductBuilder safetyStock(Integer safetyStock) { this.safetyStock = safetyStock; return this; }
        public ProductBuilder location(String location) { this.location = location; return this; }
        public ProductBuilder isActive(Boolean isActive) { this.isActive = isActive; return this; }

        public Product build() {
            Product p = new Product();
            p.id = this.id;
            p.sku = this.sku;
            p.productName = this.productName;
            p.description = this.description;
            p.category = this.category;
            p.unitPrice = this.unitPrice;
            p.unit = this.unit;
            p.reorderPoint = this.reorderPoint;
            p.safetyStock = this.safetyStock;
            p.location = this.location;
            p.isActive = this.isActive;
            return p;
        }
    }
}
