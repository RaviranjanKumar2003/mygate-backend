package com.example.demo.Controllers;

import com.example.demo.Entities.VerificationCode;
import com.example.demo.Payloads.GuardVerifyRequestDto;
import com.example.demo.Payloads.GuardVerifyResponseDto;
import com.example.demo.Repositories.VerificationCodeRepository;
import com.example.demo.Services.GuardService;
import com.example.demo.Services.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/guard")
public class GuardController {

    @Autowired
    private GuardService guardService;

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> body) {

        String code = body.get("code");

        System.out.println("CODE RECEIVED: " + code);

        boolean success = guardService.verifyGateEntry(code);

        System.out.println("VERIFY RESULT: " + success);

        if (success) {
            return ResponseEntity.ok("Entry Granted ✅");
        }

        return ResponseEntity.badRequest().body("Invalid code ❌");
    }

}