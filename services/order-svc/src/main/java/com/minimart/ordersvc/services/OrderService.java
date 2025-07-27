package com.minimart.ordersvc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimart.ordersvc.models.OrderRequest;
import com.minimart.ordersvc.models.OrderStatus;
import com.minimart.ordersvc.models.PurchaseOrder;
import com.minimart.ordersvc.repositories.IPurchaseOrderRepository;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final IPurchaseOrderRepository purchaseOrderRepository;
    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void placeOrderForInventoryCheck(OrderRequest request) {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderRepository.save(request.toEntity());
            String orderId = purchaseOrder.getId().toString();

            Baggage baggage = Baggage.current().toBuilder().put("order.id", orderId).build();

            try (Scope ignored = baggage.makeCurrent()) {
                Span.current().setAttribute("order.id", orderId);
            }
            Span.current().setAttribute("order.id", orderId);
            kafka.send("checkout.requested", objectMapper.writeValueAsString(
                    Map.of(
                            "orderId", orderId
                    )
            ));
        } catch (Exception ex) {
            log.error("---", ex);
        }
    }

    public void markOrderFailed(long orderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId).orElse(null);
        if (purchaseOrder == null) return;

        purchaseOrder.setOrderStatus(OrderStatus.FAILED);
        purchaseOrderRepository.save(purchaseOrder);
    }

    public void markOrderConfirmed(long orderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId).orElse(null);
        if (purchaseOrder == null) return;

        purchaseOrder.setOrderStatus(OrderStatus.CONFIRMED);
        purchaseOrderRepository.save(purchaseOrder);
    }
}
