package com.example.demo.Services.Implementation;

import com.example.demo.Configuration.CustomUserDetailsService;
import com.example.demo.Configuration.JwtTokenHelper;
import com.example.demo.Entities.EmailOtp;
import com.example.demo.Entities.NormalUser;
import com.example.demo.Entities.User;
import com.example.demo.Enums.NormalUserType;
import com.example.demo.Enums.UserRole;
import com.example.demo.Payloads.LoginResponse;
import com.example.demo.Repositories.EmailOtpRepository;
import com.example.demo.Repositories.NormalUserRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.EmailOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailOtpServiceImpl implements EmailOtpService {

    @Autowired
    private EmailOtpRepository emailOtpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NormalUserRepository normalUserRepository;

    // SEND OTP
    @Override
    public void sendOtp(String email) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        EmailOtp emailOtp = new EmailOtp();
        emailOtp.setEmail(email);
        emailOtp.setOtpHash(passwordEncoder.encode(otp));
        emailOtp.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        emailOtpRepository.save(emailOtp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Verification Code");
        message.setText("Your OTP is: " + otp + "\nValid for 5 minutes.");
        message.setFrom("YOUR_GMAIL@gmail.com");

        mailSender.send(message);
    }

    // VERIFY OTP + LOGIN
    @Override
    public LoginResponse verifyOtpAndLogin(String email, String otp) {

        // OTP fetch & validation
        EmailOtp emailOtp = emailOtpRepository
                .findTopByEmailOrderByExpiresAtDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (emailOtp.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");

        if (!passwordEncoder.matches(otp, emailOtp.getOtpHash()))
            throw new RuntimeException("Invalid OTP");

        // Delete used OTP
        emailOtpRepository.delete(emailOtp);

        // Load user for JWT
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        String token = jwtTokenHelper.generateToken(userDetails);

        // Get User entity
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Determine role
        String roleString = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        UserRole role = UserRole.valueOf(roleString);

        // NormalUserType fetch
        NormalUserType userType = null;
        if (role == UserRole.NORMAL_USER) {
            NormalUser normalUser = normalUserRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("NormalUser not found"));

            userType = normalUser.getNormalUserType();
        }

        // Return full LoginResponse
        return new LoginResponse(
                user.getId(),
                token,
                role,
                userType,
                user.getEmail(),
                user.getName(),
                user.getSociety() != null ? user.getSociety().getId() : null,
                user.getSociety() != null ? user.getSociety().getName() : null
        );
    }
}
