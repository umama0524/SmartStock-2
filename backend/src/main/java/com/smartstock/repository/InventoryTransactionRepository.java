package com.smartstock.repository;

import com.smartstock.entity.Inventory;
import com.smartstock.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    
    List<InventoryTransaction> findByInventoryOrderByTransactionDateDesc(Inventory inventory);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.inventory.id = :inventoryId " +
           "ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findByInventoryId(@Param("inventoryId") Long inventoryId);
    
    @Query("SELECT t FROM InventoryTransaction t ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findRecentTransactions();
}
