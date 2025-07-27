package com.minimart.inventorysvc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class InventoryConsumer {

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public InventoryConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "checkout.requested", groupId = "inventory-svc")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            Map<String, String> recordMap = objectMapper.readValue(record.value(), Map.class);
            log.info("---recordMap received: {}", recordMap.get("orderId"));
            long orderId = Long.parseLong(recordMap.get("orderId"));
            inventoryService.tryReserve(orderId, "SKU-42", 1);
        } catch (Exception ex) {
            log.error("---", ex);
        }
    }
}
