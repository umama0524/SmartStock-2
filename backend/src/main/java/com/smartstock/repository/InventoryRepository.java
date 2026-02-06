package com.smartstock.repository;

import com.smartstock.entity.Inventory;
import com.smartstock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    Optional<Inventory> findByProduct(Product product);
    
    Optional<Inventory> findByProductId(Long productId);
    
    @Query("SELECT i FROM Inventory i WHERE i.status = 'CRITICAL'")
    List<Inventory> findCriticalStock();
    
    @Query("SELECT i FROM Inventory i WHERE i.status = 'LOW'")
    List<Inventory> findLowStock();
    
    @Query("SELECT i FROM Inventory i JOIN FETCH i.product p WHERE " +
           "LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Inventory> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT i FROM Inventory i WHERE i.status = :status")
    List<Inventory> findByStatus(@Param("status") Inventory.InventoryStatus status);
    
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.status = 'CRITICAL'")
    Long countCriticalStock();
    
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.status = 'LOW'")
    Long countLowStock();
}
