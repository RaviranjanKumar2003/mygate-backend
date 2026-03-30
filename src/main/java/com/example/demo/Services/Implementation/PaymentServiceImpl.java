package com.example.demo.Services.Implementation;

import com.example.demo.Entities.Payment;
import com.example.demo.Enums.PaymentMode;
import com.example.demo.Enums.PaymentStatus;
import com.example.demo.Payloads.PaymentDto;
import com.example.demo.PaymentDueReminder.Reminder.MonthlyBill;
import com.example.demo.PaymentDueReminder.Reminder.MonthlyBillRepository;
import com.example.demo.Repositories.PaymentRepository;
import com.example.demo.Repositories.SocietyAdminRepository;
import com.example.demo.Repositories.SocietyRepo;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.PaymentService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SocietyRepo societyRepo;

    @Autowired
    private SocietyAdminRepository societyAdminRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final PaymentRepository paymentRepository;
    private final MonthlyBillRepository billRepo;

    public PaymentServiceImpl(
            PaymentRepository paymentRepo,
            MonthlyBillRepository billRepo
    ) {
        this.paymentRepository = paymentRepo;
        this.billRepo = billRepo;
    }

    /* ================= CREATE ORDER (NO RAZORPAY) ================= */

    @Override
    public Map<String, Object> createOrder(PaymentDto dto) {

        MonthlyBill bill = billRepo.findById(dto.getBillId())
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        double amountToPay = bill.getTotalAmount() - bill.getPaidAmount();

        if (amountToPay <= 0) {
            throw new RuntimeException("Bill already paid");
        }

        // Direct payment (OFFLINE)
        Payment payment = new Payment();
        payment.setBillId(bill.getId());
        payment.setAmount(amountToPay);
        payment.setPaymentMode(
                PaymentMode.valueOf(dto.getPaymentMode().toUpperCase())
        );
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setProvider("OFFLINE");
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // Update bill
        bill.setPaidAmount(bill.getPaidAmount() + amountToPay);

        if (bill.getPaidAmount() >= bill.getTotalAmount()) {
            bill.setStatus(PaymentStatus.COMPLETED);
        }

        billRepo.save(bill);

        return Map.of(
                "message", "Payment successful",
                "amount", amountToPay
        );
    }

    /* ================= VERIFY PAYMENT (DISABLED) ================= */

    @Override
    public void verifyPayment(PaymentDto dto) {
        throw new RuntimeException("Online payment disabled");
    }

    /* ================= CREATE PAYMENT ================= */

    @Override
    public PaymentDto createPayment(PaymentDto dto) {

        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        // payerName
        if (dto.getPaidById() != null && dto.getPaidByRole() != null) {
            switch (dto.getPaidByRole()) {
                case "SOCIETY_ADMIN":
                    societyAdminRepository.findById(dto.getPaidById())
                            .ifPresent(admin -> payment.setPayerName(admin.getAdminName()));
                    break;
                default:
                    userRepository.findById(dto.getPaidById())
                            .ifPresent(u -> payment.setPayerName(u.getName()));
                    break;
            }
        }

        // societyName
        if (dto.getSocietyId() != null) {
            societyRepo.findById(dto.getSocietyId())
                    .ifPresent(s -> payment.setSocietyName(s.getName()));
        }

        Payment saved = paymentRepository.save(payment);
        return mapToDto(saved);
    }

    @Override
    public List<PaymentDto> getPaymentsForUser(Integer userId, String role, Integer societyId) {

        List<Payment> payments;

        switch (role) {
            case "SUPER_ADMIN":
                payments = paymentRepository.findByReceivedByRoleOrderByPaymentDateDesc("SUPER_ADMIN");
                break;

            case "SOCIETY_ADMIN":
                payments = paymentRepository.findBySocietyIdOrderByPaymentDateDesc(societyId);
                break;

            default:
                payments = paymentRepository.findByPaidByIdOrderByPaymentDateDesc(userId);
                break;
        }

        return payments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDto getPaymentById(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return mapToDto(payment);
    }

    @Override
    public PaymentDto updatePaymentStatus(
            Integer paymentId,
            PaymentStatus status,
            Integer userId,
            String role,
            Integer societyId
    ) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if ("SUPER_ADMIN".equals(role)
                && "SUPER_ADMIN".equals(payment.getReceivedByRole())) {

            payment.setStatus(status);

        } else if ("SOCIETY_ADMIN".equals(role)
                && "SOCIETY_ADMIN".equals(payment.getReceivedByRole())
                && payment.getSocietyId().equals(societyId)) {

            payment.setStatus(status);

        } else {
            throw new RuntimeException("Not authorized");
        }

        payment.setUpdatedAt(LocalDateTime.now());
        return mapToDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto updatePayment(Integer paymentId, PaymentDto dto) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setAmount(dto.getAmount());
        payment.setDescription(dto.getDescription());
        payment.setStatus(dto.getStatus());
        payment.setUpdatedAt(LocalDateTime.now());

        return mapToDto(paymentRepository.save(payment));
    }

    @Override
    public boolean deletePayment(Integer paymentId, Integer userId, String role) {

        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment == null) return false;

        if (payment.getPaidById().equals(userId) || "SUPER_ADMIN".equals(role)) {
            paymentRepository.delete(payment);
            return true;
        }

        return false;
    }

    private PaymentDto mapToDto(Payment payment) {
        PaymentDto dto = modelMapper.map(payment, PaymentDto.class);

        if(payment.getPaidById() != null) {
            userRepository.findById(payment.getPaidById())
                    .ifPresent(u -> dto.setPaidByName(u.getName()));
        }

        if(payment.getReceivedById() != null) {
            userRepository.findById(payment.getReceivedById())
                    .ifPresent(u -> dto.setReceivedByName(u.getName()));
        }

        return dto;
    }
}