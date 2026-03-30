package com.example.demo.Repositories;

import com.example.demo.Entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository
        extends JpaRepository<VerificationCode, Integer> {

    Optional<VerificationCode>
    findByUser_IdAndCode(Integer userId, String code);

    Optional<VerificationCode>
    findByCodeAndUsedFalseAndVisitorNameIsNotNull(String code);

    Optional<VerificationCode>
    findTopByUser_IdAndCodeAndUsedFalseOrderByExpiryTimeDesc(
            Integer userId,
            String code
    );

    Optional<VerificationCode> findByUser_IdAndCodeAndUsedFalse(Integer userId, String code);

    Optional<VerificationCode>
    findTopByCodeAndUsedFalseOrderByExpiryTimeDesc(String code);

    Optional<VerificationCode> findTopByCodeAndUsedFalse(String code);

    Optional<VerificationCode> findByCodeAndUsedFalse(String code);

    Optional<VerificationCode> findByCode(String code);

    Optional<VerificationCode> findTopByCodeOrderByExpiryTimeDesc(String code);


}