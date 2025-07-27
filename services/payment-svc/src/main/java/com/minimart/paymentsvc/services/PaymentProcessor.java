package com.minimart.shippingsvc.services;

import com.minimart.shippingsvc.models.Payment;
import com.minimart.shippingsvc.models.PaymentStatus;
import com.minimart.shippingsvc.repositories.IPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentProcessor {
    private final Random random = new Random();
    private final IPaymentRepository paymentRepository;

    @Autowired
    public PaymentProcessor(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment capturePayment(long orderId, double amount) {
        boolean success = random.nextDouble() > 0.1;

        PaymentStatus paymentStatus = success ? PaymentStatus.CAPTURED : PaymentStatus.FAILED;

        Payment payment = Payment.builder()
                .orderId(orderId)
                .paymentStatus(paymentStatus)
                .amount(amount)
                .build();
        return paymentRepository.save(payment);
    }
}
