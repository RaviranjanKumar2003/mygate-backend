package com.example.demo.Controllers;

import com.example.demo.Entities.User;
import com.example.demo.Entities.VerificationCode;
import com.example.demo.Payloads.VerificationCodeDto;
import com.example.demo.Payloads.VisitorCodeRequestDto;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Repositories.VerificationCodeRepository;
import com.example.demo.Services.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/verification")
public class VerificationCodeController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private UserRepository userRepository;



// OWNER/TENANT APNE VISITOES KE LIYE CODE GENERATE KAREGA
   @PostMapping("/society/{societyId}/generate-visitor")
   public ResponseEntity<VerificationCodeDto> generateVisitorCode(@RequestBody VisitorCodeRequestDto request){
       VerificationCodeDto dto = verificationCodeService.generateVisitorCode(request);
       return ResponseEntity.ok(dto);
   }



// VERIFY VISITORS CREATED BY OWNER/TENANT
    @PostMapping("/verify-visitor")
    public ResponseEntity<?> verifyVisitorCode(@RequestParam String code) {
        boolean success = verificationCodeService.verifyVisitorCode(code);

        if (success) {
            return ResponseEntity.ok("Entry Granted ✅");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired code ❌");
        }
    }



    @PostMapping("/verify-any")
    public ResponseEntity<?> verifyAny(@RequestParam String code) {

        // 1️⃣ Resident check
        Optional<User> userOpt = userRepository.findByEntryCode(code);

        if (userOpt.isPresent()) {
            return ResponseEntity.ok("Resident Entry Granted ✅");
        }

        // 2️⃣ Visitor check
        boolean visitorOk = verificationCodeService.verifyVisitorCode(code);

        if (visitorOk) {
            return ResponseEntity.ok("Visitor Entry Granted ✅");
        }

        return ResponseEntity.badRequest()
                .body("Invalid or expired code ❌");
    }

}