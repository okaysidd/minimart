package com.minimart.notificationsvc.services;

import com.minimart.notificationsvc.models.InventoryItem;
import com.minimart.notificationsvc.repositories.IInventoryItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SeedService {

    private final IInventoryItemRepository inventoryItemRepository;

    public SeedService(IInventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @PostConstruct
    public void seedInventory() {
        log.info("⚙️ Seeding inventory...");
        try {
            InventoryItem inventoryItem = new InventoryItem();
            inventoryItem.setSku("SKU-42");
            inventoryItem.setAvailableQuantity(3);
            InventoryItem saved = inventoryItemRepository.save(inventoryItem);
            log.info("✅ Inventory seeded successfully: {}", saved);
        } catch (Exception e) {
            log.error("❌ Failed to seed inventory: {}", e.getMessage(), e);
        }
    }
}
