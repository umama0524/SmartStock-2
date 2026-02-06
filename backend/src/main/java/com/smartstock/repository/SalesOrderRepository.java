package com.smartstock.repository;

import com.smartstock.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    
    Optional<SalesOrder> findBySoNumber(String soNumber);
    
    List<SalesOrder> findByStatus(SalesOrder.SOStatus status);
    
    @Query("SELECT so FROM SalesOrder so WHERE so.status IN ('RESERVED', 'PICKED') " +
           "AND so.promisedDeliveryDate = :date")
    List<SalesOrder> findShipmentsDueOn(@Param("date") LocalDate date);
    
    @Query("SELECT so FROM SalesOrder so JOIN FETCH so.customer c WHERE " +
           "LOWER(so.soNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SalesOrder> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT COUNT(so) FROM SalesOrder so WHERE so.status IN ('RESERVED', 'PICKED') " +
           "AND so.promisedDeliveryDate = :date")
    Long countShipmentsDueOn(@Param("date") LocalDate date);
    
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(so.soNumber, 9) AS integer)), 0) FROM SalesOrder so " +
           "WHERE so.soNumber LIKE :prefix%")
    Integer findMaxNumberByPrefix(@Param("prefix") String prefix);
}
