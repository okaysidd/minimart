package com.minimart.paymentsvc.repositories;

import com.minimart.paymentsvc.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface IPaymentRepository extends JpaRepository<Payment, Long> {
}
