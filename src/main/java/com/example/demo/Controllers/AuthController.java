package com.example.demo.Controllers;

import com.example.demo.Configuration.CustomUserDetailsService;
import com.example.demo.Configuration.JwtTokenHelper;
import com.example.demo.Entities.NormalUser;
import com.example.demo.Entities.SocietyAdmin;
import com.example.demo.Entities.Staff;
import com.example.demo.Enums.NormalUserType;
import com.example.demo.Enums.UserRole;
import com.example.demo.Payloads.LoginRequest;
import com.example.demo.Payloads.LoginResponse;
import com.example.demo.Payloads.RegisterRequest;
import com.example.demo.Repositories.NormalUserRepository;
import com.example.demo.Repositories.SocietyAdminRepository;
import com.example.demo.Repositories.StaffRepository;
import com.example.demo.SuperAdminFolder.SuperAdmin;
import com.example.demo.SuperAdminFolder.SuperAdminRepository;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SuperAdminRepository superAdminRepository;
    private final SocietyAdminRepository societyAdminRepository;
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenHelper jwtTokenHelper;
    private final NormalUserRepository normalUserRepository;

    public AuthController(
            SuperAdminRepository superAdminRepository,
            SocietyAdminRepository societyAdminRepository,
            StaffRepository staffRepository,
            UserRepository userRepository,
            CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder,
            JwtTokenHelper jwtTokenHelper, NormalUserRepository normalUserRepository
    ) {
        this.superAdminRepository = superAdminRepository;
        this.societyAdminRepository = societyAdminRepository;
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenHelper = jwtTokenHelper;
        this.normalUserRepository = normalUserRepository;
    }

    // ============================== LOGIN ==============================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(request.getEmail());

        if (!passwordEncoder.matches(
                request.getPassword(),
                userDetails.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtTokenHelper.generateToken(userDetails);

        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        Integer userId;
        String email = request.getEmail();
        String name = null;
        Integer societyId = null;
        String societyName = null;

        // ⭐ ADD THIS
        NormalUserType userType = null;

        switch (role) {

            case "SUPER_ADMIN" -> {

                SuperAdmin sa = superAdminRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("SuperAdmin not found"));

                userId = sa.getId();
            }

            case "SOCIETY_ADMIN" -> {

                SocietyAdmin soc = societyAdminRepository
                        .findByAdminEmail(email)
                        .orElseThrow(() -> new RuntimeException("SocietyAdmin not found"));

                userId = soc.getId();
                name = soc.getAdminName();
                societyId = soc.getSociety().getId();
                societyName = soc.getSociety().getName();

                userType = NormalUserType.SOCIETY_ADMIN;
            }

            case "STAFF" -> {

                Staff staff = staffRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Staff not found"));

                userId = staff.getId();

                User user = userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Staff Not Found"));

                name = user.getName();
                societyId = user.getSociety().getId();
                societyName = user.getSociety().getName();
            }

            case "NORMAL_USER" -> {

                User user = userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                userId = user.getId();
                name = user.getName();
                societyId = user.getSociety().getId();
                societyName = user.getSociety().getName();

                NormalUser normalUser = normalUserRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() -> new RuntimeException("NormalUser not found"));

                userType = normalUser.getNormalUserType();
            }

            default -> throw new RuntimeException("Unsupported role");
        }

        UserRole userRole = UserRole.valueOf(role);
        return ResponseEntity.ok(
                new LoginResponse(
                        userId,
                        token,
                        userRole,
                        userType,   // ⭐ ADD THIS
                        email,
                        name,
                        societyId,
                        societyName
                )
        );
    }

    // ============================== SUPER ADMIN REGISTER ==============================

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {

        if (superAdminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("SuperAdmin already exists");
        }

        SuperAdmin sa = new SuperAdmin();
        sa.setName(request.getName());
        sa.setEmail(request.getEmail());
        sa.setMobileNumber(request.getMobileNumber());
        sa.setPassword(passwordEncoder.encode(request.getPassword()));

        superAdminRepository.save(sa);

        return ResponseEntity.ok("SuperAdmin registered successfully");
    }
}
