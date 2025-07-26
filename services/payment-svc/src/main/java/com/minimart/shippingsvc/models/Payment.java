package com.minimart.shippingsvc.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Builder
@Data
public class Payment {

    @Id @GeneratedValue
    private Long id;

    private Long orderId;

    private Double amount;

    private PaymentStatus paymentStatus;
}
