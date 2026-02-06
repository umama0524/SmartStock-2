package com.smartstock.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions")
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "balance_after")
    private Integer balanceAfter;

    @Column(length = 100)
    private String reason;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(length = 500)
    private String note;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public InventoryTransaction() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (transactionDate == null) {
            transactionDate = LocalDateTime.now();
        }
    }

    // Getters
    public Long getId() { return id; }
    public Inventory getInventory() { return inventory; }
    public TransactionType getType() { return type; }
    public Integer getQuantity() { return quantity; }
    public Integer getBalanceAfter() { return balanceAfter; }
    public String getReason() { return reason; }
    public String getLotNumber() { return lotNumber; }
    public String getNote() { return note; }
    public String getReferenceType() { return referenceType; }
    public Long getReferenceId() { return referenceId; }
    public User getUser() { return user; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }
    public void setType(TransactionType type) { this.type = type; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setBalanceAfter(Integer balanceAfter) { this.balanceAfter = balanceAfter; }
    public void setReason(String reason) { this.reason = reason; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public void setNote(String note) { this.note = note; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public void setUser(User user) { this.user = user; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder
    public static InventoryTransactionBuilder builder() { return new InventoryTransactionBuilder(); }

    public static class InventoryTransactionBuilder {
        private Long id;
        private Inventory inventory;
        private TransactionType type;
        private Integer quantity;
        private Integer balanceAfter;
        private String reason;
        private String lotNumber;
        private String note;
        private String referenceType;
        private Long referenceId;
        private User user;
        private LocalDateTime transactionDate;

        public InventoryTransactionBuilder id(Long id) { this.id = id; return this; }
        public InventoryTransactionBuilder inventory(Inventory inventory) { this.inventory = inventory; return this; }
        public InventoryTransactionBuilder type(TransactionType type) { this.type = type; return this; }
        public InventoryTransactionBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public InventoryTransactionBuilder balanceAfter(Integer balanceAfter) { this.balanceAfter = balanceAfter; return this; }
        public InventoryTransactionBuilder reason(String reason) { this.reason = reason; return this; }
        public InventoryTransactionBuilder lotNumber(String lotNumber) { this.lotNumber = lotNumber; return this; }
        public InventoryTransactionBuilder note(String note) { this.note = note; return this; }
        public InventoryTransactionBuilder referenceType(String referenceType) { this.referenceType = referenceType; return this; }
        public InventoryTransactionBuilder referenceId(Long referenceId) { this.referenceId = referenceId; return this; }
        public InventoryTransactionBuilder user(User user) { this.user = user; return this; }
        public InventoryTransactionBuilder transactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; return this; }

        public InventoryTransaction build() {
            InventoryTransaction t = new InventoryTransaction();
            t.id = this.id;
            t.inventory = this.inventory;
            t.type = this.type;
            t.quantity = this.quantity;
            t.balanceAfter = this.balanceAfter;
            t.reason = this.reason;
            t.lotNumber = this.lotNumber;
            t.note = this.note;
            t.referenceType = this.referenceType;
            t.referenceId = this.referenceId;
            t.user = this.user;
            t.transactionDate = this.transactionDate;
            return t;
        }
    }

    public enum TransactionType {
        INBOUND,
        OUTBOUND,
        ADJUSTMENT,
        RESERVE,
        RELEASE
    }
}
