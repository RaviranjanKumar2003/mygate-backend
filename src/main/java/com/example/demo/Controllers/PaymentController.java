package com.example.demo.Controllers;

import com.example.demo.Enums.PaymentStatus;
import com.example.demo.Payloads.PaymentDto;
import com.example.demo.Services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Configuration.CustomUserDetails;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


/*====================================================================================================*/

    /* ================= CREATE ORDER ================= */

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(
            @RequestBody PaymentDto dto
    ) {
        return ResponseEntity.ok(paymentService.createOrder(dto));
    }

    /* ================= VERIFY ================= */

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment() {
        return ResponseEntity.ok("Online payment disabled");
    }

/*====================================================================================================*/


    // 🔹 Create Payment
    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto dto, Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        dto.setPaidById(user.getId());
        dto.setPaidByRole(user.getRole());

        if("SOCIETY_ADMIN".equals(user.getRole())) {
            // SocietyAdmin pays SuperAdmin
            dto.setReceivedByRole("SUPER_ADMIN");
        } else if("TENANT".equals(user.getRole()) || "STAFF".equals(user.getRole())) {
            dto.setReceivedByRole("SOCIETY_ADMIN");
            dto.setReceivedById(user.getSocietyId()); // optional if needed
            dto.setSocietyId(user.getSocietyId());
        }

        PaymentDto saved = paymentService.createPayment(dto);
        return ResponseEntity.ok(saved);
    }

    // 🔹 Get payments for current user
    @GetMapping
    public ResponseEntity<List<PaymentDto>> getPayments(Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return ResponseEntity.ok(
                paymentService.getPaymentsForUser(user.getId(), user.getRole(), user.getSocietyId())
        );
    }





    @PatchMapping("/{paymentId}/status")
    public ResponseEntity<PaymentDto> updatePaymentStatus(
            @PathVariable Integer paymentId,
            @RequestParam PaymentStatus status,
            Authentication auth
    ) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        PaymentDto updated = paymentService.updatePaymentStatus(
                paymentId,
                status,
                user.getId(),
                user.getRole(),
                user.getSocietyId()
        );

        return ResponseEntity.ok(updated);
    }







    // 🔹 Update payment
    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable Integer paymentId, @RequestBody PaymentDto dto) {
        return ResponseEntity.ok(paymentService.updatePayment(paymentId, dto));
    }



    // 🔹 Delete payment
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<String> deletePayment(Authentication auth, @PathVariable Integer paymentId) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        boolean deleted = paymentService.deletePayment(paymentId, user.getId(), user.getRole());
        return ResponseEntity.ok(deleted ? "Deleted successfully" : "Not authorized");
    }
}
