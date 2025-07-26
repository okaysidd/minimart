package com.minimart.ordersvc.repositories;

import com.minimart.ordersvc.models.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInventoryItemRepository extends JpaRepository<InventoryItem, String> {
}
