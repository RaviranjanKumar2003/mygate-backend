package com.example.demo.SuperAdminFolder;

import com.example.demo.Enums.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminSeeder implements CommandLineRunner {

    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;

    public SuperAdminSeeder(
            SuperAdminRepository superAdminRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.superAdminRepository = superAdminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // ✅ Check if any SuperAdmin exists
        if (superAdminRepository.count() == 0) {

            SuperAdmin superAdmin = new SuperAdmin();
            superAdmin.setName("Super Admin");
            superAdmin.setEmail("superadmin@domain.com");
            superAdmin.setMobileNumber("9264260560");
            superAdmin.setPassword(passwordEncoder.encode("Super@123"));
            superAdmin.setImageURL("NO_IMAGE");
            superAdmin.setUserRole(UserRole.SUPER_ADMIN);
            superAdmin.setSuperAdminStatus(SuperAdminStatus.ACTIVE);

            superAdminRepository.save(superAdmin);

            System.out.println("✅ SUPER_ADMIN created successfully!");
        } else {
            System.out.println("ℹ️ SUPER_ADMIN already exists.");
        }
    }
}
