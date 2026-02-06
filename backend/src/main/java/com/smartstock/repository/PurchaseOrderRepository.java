package com.smartstock.repository;

import com.smartstock.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    
    Optional<PurchaseOrder> findByPoNumber(String poNumber);
    
    List<PurchaseOrder> findByStatus(PurchaseOrder.POStatus status);
    
    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = 'PENDING'")
    List<PurchaseOrder> findPendingApproval();
    
    @Query("SELECT po FROM PurchaseOrder po JOIN FETCH po.supplier s WHERE " +
           "LOWER(po.poNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<PurchaseOrder> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.status = 'PENDING'")
    Long countPendingApproval();
    
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(po.poNumber, 9) AS integer)), 0) FROM PurchaseOrder po " +
           "WHERE po.poNumber LIKE :prefix%")
    Integer findMaxNumberByPrefix(@Param("prefix") String prefix);
}
