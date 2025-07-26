package com.minimart.ordersvc.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PurchaseOrder {

    @Id
    @GeneratedValue
    private Long id;

    private String sku;

    private Integer quantity;

    private OrderStatus orderStatus = OrderStatus.PENDING;
}
