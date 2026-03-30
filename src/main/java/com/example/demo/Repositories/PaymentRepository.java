package com.example.demo.Repositories;

import com.example.demo.Entities.Payment;
import com.example.demo.Enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // 🔹 SUPER ADMIN → all payments received
    List<Payment> findByReceivedByRoleOrderByPaymentDateDesc(String receivedByRole);

    // 🔹 SOCIETY ADMIN → payments of own society
    List<Payment> findBySocietyIdOrderByPaymentDateDesc(Integer societyId);

    // 🔹 TENANT / STAFF → own payments
    List<Payment> findByPaidByIdOrderByPaymentDateDesc(Integer paidById);

    // 🔹 Filter by status
    List<Payment> findByStatusOrderByPaymentDateDesc(PaymentStatus status);

    // 🔹 Society + Status (very useful)
    List<Payment> findBySocietyIdAndStatusOrderByPaymentDateDesc(
            Integer societyId,
            PaymentStatus status
    );

    Optional<Payment> findByOrderId(String orderId);

}
