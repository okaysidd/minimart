package com.minimart.shippingsvc.repositories;

import com.minimart.shippingsvc.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface IPaymentRepository extends JpaRepository<Payment, Long> {
}
