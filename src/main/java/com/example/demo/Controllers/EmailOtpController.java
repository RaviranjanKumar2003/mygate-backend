package com.example.demo.Controllers;

import com.example.demo.Payloads.EmailOtpDto;
import com.example.demo.Payloads.LoginResponse;
import com.example.demo.Services.EmailOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin("*")
public class EmailOtpController {

    @Autowired
    private EmailOtpService emailOtpService;

    // SEND OTP
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody EmailOtpDto dto) {
        emailOtpService.sendOtp(dto.getEmail());
        return ResponseEntity.ok("OTP sent successfully");
    }

    // VERIFY OTP
    @PostMapping("/verify")
    public ResponseEntity<LoginResponse> verifyOtp(@RequestBody EmailOtpDto dto) {
        LoginResponse response = emailOtpService.verifyOtpAndLogin(dto.getEmail(), dto.getOtp());
        return ResponseEntity.ok(response);
    }
}
