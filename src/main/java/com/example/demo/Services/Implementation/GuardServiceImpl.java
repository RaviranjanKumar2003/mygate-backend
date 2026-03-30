package com.example.demo.Services.Implementation;

import com.example.demo.Entities.User;
import com.example.demo.Entities.VerificationCode;
import com.example.demo.Payloads.GuardVerifyRequestDto;
import com.example.demo.Payloads.GuardVerifyResponseDto;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Repositories.VerificationCodeRepository;
import com.example.demo.Services.GuardService;
import com.example.demo.Services.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class GuardServiceImpl implements GuardService {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Override
    public boolean verifyGateEntry(String code) {

        try {

            if (code == null || code.trim().isEmpty()) {
                return false;
            }

            String cleanedCode = code.trim();

            Optional<User> userOpt = userRepository.findByEntryCode(cleanedCode);

            if (userOpt.isPresent()) {
                return true;
            }

            return verificationCodeService.verifyVisitorCode(cleanedCode);

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public GuardVerifyResponseDto verifyUserEntry(GuardVerifyRequestDto request) {
        Optional<User> userOpt = userRepository.findByEntryCode(request.getCode());

        GuardVerifyResponseDto response = new GuardVerifyResponseDto();

        if (userOpt.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("Invalid code");
            return response;
        }

        User user = userOpt.get();

        // 🔹 Generate temporary verification code
        String tempCode = generateRandom6DigitCode();
        VerificationCode vCode = new VerificationCode();
        vCode.setUser(user);
        vCode.setCode(tempCode);
        vCode.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        vCode.setUsed(false);

        verificationCodeRepository.save(vCode);

        // 🔹 Prepare response
        response.setSuccess(true);
        response.setMessage("Entry allowed. Temporary code generated.");
        response.setUserId(user.getId());
        response.setUserName(user.getName());
        response.setTemporaryCode(tempCode);

        return response;
    }

    // ✅ 6-digit random code generator
    private String generateRandom6DigitCode() {
        Random random = new Random();
        int num = 100000 + random.nextInt(900000); // ensures 6-digit
        return String.valueOf(num);
    }
}