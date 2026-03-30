package com.example.demo.Services.Implementation;

import com.example.demo.Entities.User;
import com.example.demo.Entities.VerificationCode;
import com.example.demo.Entities.Visitor;
import com.example.demo.Enums.VisitorStatus;
import com.example.demo.Payloads.VerificationCodeDto;
import com.example.demo.Payloads.VisitorCodeRequestDto;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Repositories.VerificationCodeRepository;
import com.example.demo.Repositories.VisitorRepository;
import com.example.demo.Services.VerificationCodeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    private String generateRandom6DigitCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    // ================= RESIDENT CODE =================
    @Override
    public VerificationCodeDto generateCode(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VerificationCode vCode = new VerificationCode();
        vCode.setUser(user);
        vCode.setCode(generateRandom6DigitCode());
        vCode.setExpiryTime(null); // no expiry
        vCode.setUsed(false);

        verificationCodeRepository.save(vCode);

        return new VerificationCodeDto(
                vCode.getId(),
                vCode.getCode(),
                null,
                false,
                user.getId(),
                null
        );
    }

    // ================= VISITOR CODE =================
    @Override
    public VerificationCodeDto generateVisitorCode(VisitorCodeRequestDto request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getSociety().getId().equals(request.getSocietyId())) {
            throw new RuntimeException("User does not belong to this society");
        }

        VerificationCode vCode = new VerificationCode();
        vCode.setUser(user);
        vCode.setCode(generateRandom6DigitCode());
        vCode.setVisitorName(request.getVisitorName());
        vCode.setExpiryTime(
                request.getExpiryTime() != null
                        ? request.getExpiryTime()
                        : LocalDateTime.now().plusMinutes(5)
        );
        vCode.setUsed(false);

        verificationCodeRepository.save(vCode);

        return new VerificationCodeDto(
                vCode.getId(),
                vCode.getCode(),
                vCode.getExpiryTime(),
                false,
                user.getId(),
                vCode.getVisitorName()
        );
    }

    @Override
    @Transactional
    public boolean verifyCode(Integer userId, String code) {
        VerificationCode vCode =
                verificationCodeRepository
                        .findByUser_IdAndCodeAndUsedFalse(userId, code)
                        .orElse(null);

        if (vCode == null) return false;

        // Check expiry if set
        if (vCode.getExpiryTime() != null &&
                vCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        vCode.setUsed(true);
        verificationCodeRepository.save(vCode);
        return true;
    }


    // ================= GATE VERIFY (🔥 MAIN FIX) =================
    @Override
    @Transactional
    public boolean verifyAnyCode(String code) {

        VerificationCode vCode =
                verificationCodeRepository
                        .findTopByCodeAndUsedFalseOrderByExpiryTimeDesc(code)
                        .orElse(null);

        if (vCode == null) {
            return false;
        }

        if (vCode.getExpiryTime() != null &&
                vCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        // code used
        vCode.setUsed(true);
        verificationCodeRepository.save(vCode);

        // 🔥 visitor status update
        Visitor visitor =
                visitorRepository.findTopByNameAndVisitorStatus(
                        vCode.getVisitorName(),
                        VisitorStatus.PENDING
                ).orElse(null);

        if (visitor != null) {
            visitor.setVisitorStatus(VisitorStatus.IN);
            visitor.setInTime(LocalDateTime.now());
            visitorRepository.save(visitor);
        }

        return true;
    }

    // ================= OPTIONAL =================
    @Override
    public boolean verifyVisitorCode(String code) {
        return verifyAnyCode(code);
    }
}