package com.example.demo.SuperAdminFolder;

import com.example.demo.Configuration.JwtTokenHelper;
import com.example.demo.Enums.UserRole;
import com.example.demo.Payloads.LoginRequest;
import com.example.demo.Payloads.LoginResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/super-admin")
public class SuperAdminAuthController {

    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenHelper jwtTokenHelper;

    public SuperAdminAuthController(
            SuperAdminRepository superAdminRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenHelper jwtTokenHelper
    ) {
        this.superAdminRepository = superAdminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenHelper = jwtTokenHelper;
    }



//========================== REGISTER

    @PostMapping("/register")
    public String registerSuperAdmin(@RequestBody SuperAdmin sa) {
        sa.setPassword(passwordEncoder.encode(sa.getPassword()));
        sa.setUserRole(UserRole.SUPER_ADMIN);
        sa.setSuperAdminStatus(SuperAdminStatus.ACTIVE);

        superAdminRepository.save(sa);
        return "Super Admin registered successfully";
    }



// ===================================== LOGIN

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        // Find SuperAdmin by email
        SuperAdmin sa = superAdminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), sa.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Wrap SuperAdmin into Spring Security UserDetails for JWT
        User userDetails = new User(
                sa.getEmail(),
                sa.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + sa.getUserRole())
                )
        );

        // Generate JWT token
        String token = jwtTokenHelper.generateToken(userDetails);

        Integer societyId = null;
        String societyName = null;

        // Return token, role, and email
        return new LoginResponse(
                sa.getId(),
                token,
                sa.getUserRole(),   // ✅ enum directly
                null,               // ✅ SuperAdmin ke liye userType null
                sa.getEmail(),
                sa.getName(),
                societyId,
                societyName
        );
    }
}
