package com.minimart.ordersvc.repositories;

import com.minimart.ordersvc.models.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface IPurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}
