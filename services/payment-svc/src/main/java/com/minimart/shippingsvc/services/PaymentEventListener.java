package com.minimart.shippingsvc.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimart.shippingsvc.models.Payment;
import com.minimart.shippingsvc.models.PaymentStatus;
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
public class PaymentEventListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentProcessor paymentProcessor;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "inventory.reserved", groupId = "payment-svc")
    public void onInventoryReserved(ConsumerRecord<String, String> record) {
        try {
            log.info("---Packet received: {}", record.value());
            Map<String, Object> recordMap = objectMapper.readValue(record.value(), new TypeReference<Map<String, Object>>() {
            });
            long orderId = Long.parseLong(recordMap.get("orderId").toString());
            double amount = Double.parseDouble(recordMap.get("amount").toString());

            Payment payment = paymentProcessor.capturePayment(orderId, amount);

            String resultTopic = payment.getPaymentStatus() == PaymentStatus.CAPTURED ? "payment.success" : "payment.failed";

            kafkaTemplate.send(resultTopic, objectMapper.writeValueAsString(
                    Map.of(
                            "orderId", orderId,
                            "paymentStatus", payment.getPaymentStatus().toString()
                    )
            ));
        } catch (Exception ex) {
            log.error("---", ex);
        }
    }
}
