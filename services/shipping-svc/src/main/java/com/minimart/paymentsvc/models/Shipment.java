package com.minimart.paymentsvc.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("shipments")
@Builder
@Data
public class Shipment {

    @Id
    private String id;

    private Long orderId;

    private String labelId;

    private ShippingStatus shippingStatus;

    private Instant createdat;
}
