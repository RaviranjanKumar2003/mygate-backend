package com.example.demo.Services;

import com.example.demo.Payloads.GuardVerifyRequestDto;
import com.example.demo.Payloads.GuardVerifyResponseDto;

public interface GuardService {

    GuardVerifyResponseDto verifyUserEntry(GuardVerifyRequestDto request);

    boolean verifyGateEntry(String code);

}