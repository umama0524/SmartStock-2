package com.smartstock.service;

import com.smartstock.dto.InventoryDto;
import com.smartstock.entity.Inventory;
import com.smartstock.entity.InventoryTransaction;
import com.smartstock.entity.User;
import com.smartstock.repository.InventoryRepository;
import com.smartstock.repository.InventoryTransactionRepository;
import com.smartstock.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public InventoryService(InventoryRepository inventoryRepository, 
                            InventoryTransactionRepository transactionRepository,
                            UserRepository userRepository) {
        this.inventoryRepository = inventoryRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<InventoryDto> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(InventoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public InventoryDto getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("在庫が見つかりません: " + id));
        return InventoryDto.fromEntity(inventory);
    }

    public List<InventoryDto> searchInventory(String keyword) {
        return inventoryRepository.searchByKeyword(keyword).stream()
                .map(InventoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<InventoryDto> getInventoryByStatus(String status) {
        Inventory.InventoryStatus inventoryStatus = Inventory.InventoryStatus.valueOf(status.toUpperCase());
        return inventoryRepository.findByStatus(inventoryStatus).stream()
                .map(InventoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public InventoryDto inbound(Long inventoryId, Integer quantity, String lotNumber, String note) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("在庫が見つかりません: " + inventoryId));

        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventory = inventoryRepository.save(inventory);

        createTransaction(inventory, InventoryTransaction.TransactionType.INBOUND, quantity, lotNumber, note);

        return InventoryDto.fromEntity(inventory);
    }

    @Transactional
    public InventoryDto outbound(Long inventoryId, Integer quantity, String reason, String note) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("在庫が見つかりません: " + inventoryId));

        if (inventory.getAvailableQuantity() < quantity) {
            throw new RuntimeException("在庫が不足しています");
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory = inventoryRepository.save(inventory);

        createTransaction(inventory, InventoryTransaction.TransactionType.OUTBOUND, -quantity, null, reason + (note != null ? ": " + note : ""));

        return InventoryDto.fromEntity(inventory);
    }

    @Transactional
    public InventoryDto adjust(Long inventoryId, Integer adjustmentQuantity, String reasonCode, String note) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("在庫が見つかりません: " + inventoryId));

        inventory.setQuantity(inventory.getQuantity() + adjustmentQuantity);
        inventory = inventoryRepository.save(inventory);

        String reason = getReasonText(reasonCode) + (note != null ? ": " + note : "");
        createTransaction(inventory, InventoryTransaction.TransactionType.ADJUSTMENT, adjustmentQuantity, null, reason);

        return InventoryDto.fromEntity(inventory);
    }

    private void createTransaction(Inventory inventory, InventoryTransaction.TransactionType type, 
                                   Integer quantity, String lotNumber, String note) {
        User currentUser = getCurrentUser();
        
        InventoryTransaction transaction = InventoryTransaction.builder()
                .inventory(inventory)
                .type(type)
                .quantity(quantity)
                .balanceAfter(inventory.getQuantity())
                .lotNumber(lotNumber)
                .note(note)
                .user(currentUser)
                .build();
        
        transactionRepository.save(transaction);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    private String getReasonText(String reasonCode) {
        return switch (reasonCode) {
            case "damaged" -> "破損";
            case "expired" -> "期限切れ";
            case "lost" -> "紛失";
            case "found" -> "発見";
            case "count_diff" -> "棚卸差異";
            default -> "その他";
        };
    }
}
