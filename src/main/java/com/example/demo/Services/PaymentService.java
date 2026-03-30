package com.example.demo.Services;

import com.example.demo.Enums.PaymentStatus;
import com.example.demo.Payloads.PaymentDto;
import java.util.List;
import java.util.Map;

public interface PaymentService {

    PaymentDto createPayment(PaymentDto dto);

    List<PaymentDto> getPaymentsForUser(Integer userId, String role, Integer societyId);

    PaymentDto getPaymentById(Integer paymentId);

    PaymentDto updatePayment(Integer paymentId, PaymentDto dto);

    boolean deletePayment(Integer paymentId, Integer userId, String role);



    PaymentDto updatePaymentStatus(
            Integer paymentId,
            PaymentStatus status,
            Integer userId,
            String role,
            Integer societyId
    );

    Map<String, Object> createOrder(PaymentDto dto);

    void verifyPayment(PaymentDto dto);


}
