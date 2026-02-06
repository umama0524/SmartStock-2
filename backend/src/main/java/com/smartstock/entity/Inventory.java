package com.smartstock.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(name = "reserved_quantity")
    private Integer reservedQuantity = 0;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private InventoryStatus status;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public Inventory() {}

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
        updateStatus();
    }

    public Integer getAvailableQuantity() {
        return quantity - reservedQuantity;
    }

    private void updateStatus() {
        if (product == null) {
            status = InventoryStatus.NORMAL;
            return;
        }
        
        Integer safetyStock = product.getSafetyStock();
        Integer reorderPoint = product.getReorderPoint();
        
        if (safetyStock != null && quantity <= safetyStock) {
            status = InventoryStatus.CRITICAL;
        } else if (reorderPoint != null && quantity <= reorderPoint) {
            status = InventoryStatus.LOW;
        } else {
            status = InventoryStatus.NORMAL;
        }
    }

    // Getters
    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public Integer getQuantity() { return quantity; }
    public Integer getReservedQuantity() { return reservedQuantity; }
    public InventoryStatus getStatus() { return status; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setProduct(Product product) { this.product = product; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }
    public void setStatus(InventoryStatus status) { this.status = status; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    // Builder
    public static InventoryBuilder builder() { return new InventoryBuilder(); }

    public static class InventoryBuilder {
        private Long id;
        private Product product;
        private Integer quantity = 0;
        private Integer reservedQuantity = 0;
        private InventoryStatus status;
        private LocalDateTime lastUpdated;

        public InventoryBuilder id(Long id) { this.id = id; return this; }
        public InventoryBuilder product(Product product) { this.product = product; return this; }
        public InventoryBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public InventoryBuilder reservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; return this; }
        public InventoryBuilder status(InventoryStatus status) { this.status = status; return this; }
        public InventoryBuilder lastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; return this; }

        public Inventory build() {
            Inventory inv = new Inventory();
            inv.id = this.id;
            inv.product = this.product;
            inv.quantity = this.quantity;
            inv.reservedQuantity = this.reservedQuantity;
            inv.status = this.status;
            inv.lastUpdated = this.lastUpdated;
            return inv;
        }
    }

    public enum InventoryStatus {
        NORMAL,
        LOW,
        CRITICAL,
        EXCESS
    }
}
