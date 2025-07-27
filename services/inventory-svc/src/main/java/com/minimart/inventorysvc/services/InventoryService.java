package com.minimart.inventorysvc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimart.inventorysvc.models.InventoryItem;
import com.minimart.inventorysvc.repositories.IInventoryItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final IInventoryItemRepository inventoryItemRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String, Object> kakfa;

    public void tryReserve(long orderId, String sku, int quantity) {
        try {
            InventoryItem inventoryItem = inventoryItemRepository.findById(sku).orElse(null);
            if (inventoryItem == null || inventoryItem.getAvailableQuantity() < quantity) {
                log.info("---Publishing packet to inventory.failed");
                kakfa.send("inventory.failed", objectMapper.writeValueAsString(Map.of(
                                "orderId", orderId,
                                "amount", 25
                        )
                ));
                return;
            }
            inventoryItem.setAvailableQuantity(inventoryItem.getAvailableQuantity() - quantity);
            inventoryItemRepository.save(inventoryItem);
            log.info("---Publishing packet to inventory.reserved");
            kakfa.send("inventory.reserved", objectMapper.writeValueAsString(Map.of(
                            "orderId", orderId,
                            "amount", 25
                    )
            ));
        } catch (Exception ex) {
            log.error("---", ex);
        }
    }
}
