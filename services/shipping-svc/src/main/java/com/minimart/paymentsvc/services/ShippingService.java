package com.minimart.paymentsvc.services;

import com.minimart.paymentsvc.models.Shipment;
import com.minimart.paymentsvc.models.ShippingStatus;
import com.minimart.paymentsvc.repositories.IShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ShippingService {

    private final IShippingRepository shippingRepository;
    private final Random random = new Random();

    public Shipment prepare(long orderId) {
        boolean success = random.nextDouble() > 0.1;

        Shipment shipment = Shipment.builder()
                .orderId(orderId)
                .labelId(UUID.randomUUID().toString())
                .shippingStatus(success ? ShippingStatus.PREPARED : ShippingStatus.FAILED)
                .createdat(Instant.now())
                .build();
        return shippingRepository.save(shipment);
    }
}
