package com.minimart.ordersvc.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class OrderInventoryEventListener {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderService orderService;

    @KafkaListener(topics = "inventory.failed", groupId = "inventory-svc")
    public void onOrderCreated(ConsumerRecord<String, String> record) {
        try {
            Map<String, Object> recordMap = objectMapper.readValue(record.value(), new TypeReference<Map<String, Object>>() {
            });
            long orderId = Long.parseLong(recordMap.get("orderId").toString());
            orderService.markOrderFailed(orderId);
        } catch (Exception ex) {
            log.error("---", ex);
        }
    }
}
