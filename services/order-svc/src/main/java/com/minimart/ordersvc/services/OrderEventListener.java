package com.minimart.ordersvc.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class OrderEventListener {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderService orderService;

    @KafkaListener(topics = {"inventory.failed", "order.failed", "payment.failed", "shipping.failed"}, groupId = "order-svc")
    public void onOrderFailed(ConsumerRecord<String, String> record) {
        try {
            Map<String, Object> recordMap = objectMapper.readValue(record.value(), new TypeReference<>() {
            });
            long orderId = Long.parseLong(recordMap.get("orderId").toString());
            orderService.markOrderFailed(orderId);
        } catch (Exception ex) {
            log.error("---", ex);
        }
    }

    @KafkaListener(topics = {"order.completed"}, groupId = "order-svc")
    public void onOrderCreated(ConsumerRecord<String, String> record) {
        try {
            Map<String, Object> recordMap = objectMapper.readValue(record.value(), new TypeReference<>() {
            });
            long orderId = Long.parseLong(recordMap.get("orderId").toString());
            orderService.markOrderConfirmed(orderId);
        } catch (Exception ex) {
            log.error("---", ex);
        }
    }
}
