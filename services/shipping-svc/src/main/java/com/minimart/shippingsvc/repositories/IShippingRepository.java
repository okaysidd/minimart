package com.minimart.shippingsvc.repositories;

import com.minimart.shippingsvc.models.Shipment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IShippingRepository extends MongoRepository<Shipment, String> {
    Optional<Shipment> findByOrderId(long orderId);
}
