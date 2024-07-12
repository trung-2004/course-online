package com.example.payment1_service.repository;

import com.example.payment1_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Payment findByOrderIdAndUserId(String orderId, String userId);
    Payment findByOrderId(String orderId);
}
