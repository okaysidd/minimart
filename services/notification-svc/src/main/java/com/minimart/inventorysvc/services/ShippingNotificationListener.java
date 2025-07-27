package com.minimart.inventorysvc.services;

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
@Component
@Slf4j
public class ShippingNotificationListener {

    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Notifier notifier;

    @KafkaListener(topics = "shipping.prepared", groupId = "notification-svc")
    public void onShippingPrepared(ConsumerRecord<String, String> record) {
        try {
            Map<String, String> recordMap = objectMapper.readValue(record.value(), new TypeReference<>() {
            });

            long orderId = Long.parseLong(recordMap.get("orderId"));
            String labelId = recordMap.get("labelId");

            notifier.enqueueSuccess(orderId);

            kafka.send("order.completed", String.valueOf(orderId), objectMapper.writeValueAsString(Map.of(
                    "orderId", orderId,
                    "labelId", labelId
            )));
            log.info("Order completed for: {}", orderId);
        } catch (Exception ex) {
            log.error("---", ex);
        }
    }

    @KafkaListener(topics = "shipping.failed", groupId = "notification-svc")
    public void onShippingFailed(ConsumerRecord<String, String> record) {
        try {
            Map<String, String> recordMap = objectMapper.readValue(record.value(), new TypeReference<>() {
            });

            long orderId = Long.parseLong(recordMap.get("orderId"));

            notifier.enqueueFailure(orderId);

            kafka.send("order.failed", String.valueOf(orderId), objectMapper.writeValueAsString(Map.of(
                    "orderId", orderId
            )));
            log.info("Order failed for: {}", orderId);
        } catch (Exception ex) {
            log.error("---", ex);
        }
    }
}
