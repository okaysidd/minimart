package com.minimart.shippingsvc.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimart.shippingsvc.models.Shipment;
import com.minimart.shippingsvc.models.ShippingStatus;
import com.minimart.shippingsvc.repositories.IShippingRepository;
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
public class ShippingListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final IShippingRepository shippingRepository;
    private final ShippingService shippingService;

    @KafkaListener(topics = "payment.success", groupId = "shipping-svc")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            log.info("---Packet received in ShippingListener: {}", record.value());
            Map<String, Object> recordMap = objectMapper.readValue(record.value(), new TypeReference<Map<String, Object>>() {
            });
            long orderId = Long.parseLong(recordMap.get("orderId").toString());

            Shipment shipment = shippingService.prepare(orderId);
            String outTopic = shipment.getShippingStatus() == ShippingStatus.PREPARED ? "shipping.prepared" : "shipping.failed";
            log.info("---Packet getting shipped to: {}", outTopic);
            kafkaTemplate.send(outTopic, String.valueOf(orderId), objectMapper.writeValueAsString(
                    Map.of(
                            "orderId", orderId,
                            "status", shipment.getShippingStatus().toString(),
                            "labelId", shipment.getLabelId()
                    )
            ));
        } catch (Exception ex) {
            log.error("---", ex);
        }
    }
}
