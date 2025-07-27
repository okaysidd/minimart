package com.minimart.inventorysvc.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class InventoryItem {

    @Id
    private String sku;

    private Integer availableQuantity;
}
