package com.example.demo.Services;

import com.example.demo.Payloads.LoginResponse;

public interface EmailOtpService {

    void sendOtp(String email);

    LoginResponse verifyOtpAndLogin(String email, String otp);
}
