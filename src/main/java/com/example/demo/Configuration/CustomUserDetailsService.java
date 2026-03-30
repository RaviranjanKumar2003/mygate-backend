package com.example.demo.Configuration;

import com.example.demo.Entities.SocietyAdmin;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.SocietyAdminRepository;
import com.example.demo.Repositories.StaffRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.SuperAdminFolder.SuperAdmin;
import com.example.demo.SuperAdminFolder.SuperAdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SuperAdminRepository superAdminRepository;
    private final SocietyAdminRepository societyAdminRepository;
    private final StaffRepository staffRepository;

    public CustomUserDetailsService(
            UserRepository userRepository,
            SuperAdminRepository superAdminRepository,
            SocietyAdminRepository societyAdminRepository,
            StaffRepository staffRepository
    ) {
        this.userRepository = userRepository;
        this.superAdminRepository = superAdminRepository;
        this.societyAdminRepository = societyAdminRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1️⃣ SUPER ADMIN
        Optional<SuperAdmin> superAdmin = superAdminRepository.findByEmail(email);
        if (superAdmin.isPresent()) {
            return new CustomUserDetails(
                    superAdmin.get().getId(),
                    superAdmin.get().getEmail(),
                    superAdmin.get().getPassword(),
                    "SUPER_ADMIN",
                    null // global, no societyId
            );
        }

        // 2️⃣ SOCIETY ADMIN
        Optional<SocietyAdmin> societyAdmin = societyAdminRepository.findByAdminEmail(email);
        if (societyAdmin.isPresent()) {
            return new CustomUserDetails(
                    societyAdmin.get().getId(),
                    societyAdmin.get().getAdminEmail(),
                    societyAdmin.get().getAdminPassword(),
                    "SOCIETY_ADMIN",
                    societyAdmin.get().getSociety().getId()
            );
        }

        // 3️⃣ STAFF
        // Uncomment if Staff repository ready
    /*
    Optional<Staff> staff = staffRepository.findByEmail(email);
    if(staff.isPresent()){
        return new CustomUserDetails(
            staff.get().getId(),
            staff.get().getEmail(),
            staff.get().getPassword(),
            "STAFF",
            staff.get().getSociety().getId()
        );
    }
    */

        // 4️⃣ NORMAL USER
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getUserRole().name(),
                user.getSociety().getId()
        );
    }


    // ============================== HELPER ==============================

    private UserDetails buildUser(
            String email,
            String password,
            String role) {

        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(password)
                .authorities("ROLE_" + role)
                .build();
    }
}












//package com.example.demo.Configuration;
//
//import com.example.demo.Entities.SocietyAdmin;
//import com.example.demo.Entities.User;
//import com.example.demo.Repositories.SocietyAdminRepository;
//import com.example.demo.Repositories.StaffRepository;
//import com.example.demo.Repositories.UserRepository;
//import com.example.demo.SuperAdminFolder.SuperAdmin;
//import com.example.demo.SuperAdminFolder.SuperAdminRepository;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//    private final SuperAdminRepository superAdminRepository;
//    private final SocietyAdminRepository societyAdminRepository;
//    private final StaffRepository staffRepository;
//
//    public CustomUserDetailsService(
//            UserRepository userRepository,
//            SuperAdminRepository superAdminRepository,
//            SocietyAdminRepository societyAdminRepository,
//            StaffRepository staffRepository
//    ) {
//        this.userRepository = userRepository;
//        this.superAdminRepository = superAdminRepository;
//        this.societyAdminRepository = societyAdminRepository;
//        this.staffRepository = staffRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email)
//            throws UsernameNotFoundException {
//
//        // 1️⃣ SUPER ADMIN
//        Optional<SuperAdmin> superAdmin = superAdminRepository.findByEmail(email);
//        if (superAdmin.isPresent()) {
//            return buildUser(
//                    superAdmin.get().getEmail(),
//                    superAdmin.get().getPassword(),
//                    "SUPER_ADMIN"
//            );
//        }
//
//        // 2️⃣ SOCIETY ADMIN
//        Optional<SocietyAdmin> societyAdmin = societyAdminRepository.findByAdminEmail(email);
//        if (societyAdmin.isPresent()) {
//            return buildUser(
//                    societyAdmin.get().getAdminEmail(),
//                    societyAdmin.get().getAdminPassword(),
//                    "SOCIETY_ADMIN"
//            );
//        }
//
//        // 3️⃣ STAFF
//        //Optional<Staff> staff = staffRepository.findByEmail(email);
//        //if (staff.isPresent()) {
//            //return buildUser(
//                    //staff.get().getEmail(),
//                    //staff.get().getPassword(),
//                    //"STAFF"
//            //);
//        //}
//
//        // 4️⃣ NORMAL USER
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("User not found"));
//
//        return buildUser(
//                user.getEmail(),
//                user.getPassword(),
//                user.getUserRole().name()
//        );
//    }
//
//    // ============================== HELPER ==============================
//
//    private UserDetails buildUser(
//            String email,
//            String password,
//            String role) {
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(email)
//                .password(password)
//                .authorities("ROLE_" + role)
//                .build();
//    }
//}
