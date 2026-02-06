package com.smartstock.controller;

import com.smartstock.dto.InventoryDto;
import com.smartstock.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryDto>> getAllInventory(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ) {
        List<InventoryDto> inventory;
        
        if (search != null && !search.isEmpty()) {
            inventory = inventoryService.searchInventory(search);
        } else if (status != null && !status.isEmpty() && !status.equals("all")) {
            inventory = inventoryService.getInventoryByStatus(status);
        } else {
            inventory = inventoryService.getAllInventory();
        }
        
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDto> getInventoryById(@PathVariable Long id) {
        InventoryDto inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/{id}/inbound")
    public ResponseEntity<InventoryDto> inbound(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request
    ) {
        Integer quantity = (Integer) request.get("quantity");
        String lotNumber = (String) request.get("lotNumber");
        String note = (String) request.get("note");
        
        InventoryDto inventory = inventoryService.inbound(id, quantity, lotNumber, note);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/{id}/outbound")
    public ResponseEntity<InventoryDto> outbound(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request
    ) {
        Integer quantity = (Integer) request.get("quantity");
        String reason = (String) request.get("reason");
        String note = (String) request.get("note");
        
        InventoryDto inventory = inventoryService.outbound(id, quantity, reason, note);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/{id}/adjust")
    public ResponseEntity<InventoryDto> adjust(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request
    ) {
        Integer adjustmentQuantity = (Integer) request.get("adjustmentQuantity");
        String reasonCode = (String) request.get("reasonCode");
        String note = (String) request.get("note");
        
        InventoryDto inventory = inventoryService.adjust(id, adjustmentQuantity, reasonCode, note);
        return ResponseEntity.ok(inventory);
    }
}
