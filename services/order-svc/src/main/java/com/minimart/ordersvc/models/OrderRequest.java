package com.minimart.ordersvc.models;

public record OrderRequest(String sku, Integer quantity) {
    public PurchaseOrder toEntity() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSku(sku);
        purchaseOrder.setQuantity(quantity);
        return purchaseOrder;
    }
}
