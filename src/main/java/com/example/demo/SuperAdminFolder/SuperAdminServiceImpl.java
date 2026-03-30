package com.example.demo.SuperAdminFolder;

import com.example.demo.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


// CREATE SUPER ADMIN
    @Override
    public SuperAdminDto createSuperAdmin(SuperAdminDto dto) {

        if (superAdminRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Super Admin already exists with this email");
        }

        SuperAdmin admin = new SuperAdmin();
        admin.setName(dto.getName());
        admin.setEmail(dto.getEmail());
        admin.setMobileNumber(dto.getMobileNumber());
        admin.setImageURL(dto.getImageURL());
        admin.setSuperAdminStatus(SuperAdminStatus.ACTIVE);
        admin.setCreatedAt(LocalDateTime.now());

        // ✅ Set password (must not be null)
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));  // << important!

        return mapToDto(superAdminRepository.save(admin));
    }



// GET SUPER ADMIN BY ID
    @Override
    public SuperAdminDto getSuperAdminById(int id) {

        SuperAdmin admin = superAdminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SuperAdmin", "id", id));

        return mapToDto(admin);
    }


// GET ALL SUPER ADMINS
    @Override
    public List<SuperAdminDto> getAllSuperAdmins() {
        return superAdminRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


// UPDATE SUPER ADMIN
    @Override
    public SuperAdminDto updateSuperAdmin(int id, SuperAdminDto dto) {

        SuperAdmin admin = superAdminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SuperAdmin", "id", id));

        admin.setName(dto.getName());
        admin.setMobileNumber(dto.getMobileNumber());

        return mapToDto(superAdminRepository.save(admin));
    }


// DELETE SUPER ADMIN
    @Override
    public void deactivateSuperAdmin(int superAdminId) {

        SuperAdmin admin = superAdminRepository.findById(superAdminId)
                .orElseThrow(() -> new ResourceNotFoundException("SuperAdmin", "id", superAdminId));

        admin.setSuperAdminStatus(SuperAdminStatus.INACTIVE);
        superAdminRepository.save(admin);
    }


// UPDATE SUPER ADMIN IMAGE
    @Override
    public SuperAdminDto updateSuperAdminImage(Integer superAdminId, String imageName) {

        SuperAdmin superAdmin = superAdminRepository.findById(superAdminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("SuperAdmin", "id", superAdminId));

        superAdmin.setImageURL(imageName);

        SuperAdmin updated = superAdminRepository.save(superAdmin);

        return mapToDto(updated);
    }



// ================= HELPER =================
    private SuperAdminDto mapToDto(SuperAdmin admin) {

        SuperAdminDto dto = new SuperAdminDto();
        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setEmail(admin.getEmail());
        dto.setMobileNumber(admin.getMobileNumber());
        dto.setImageURL(admin.getImageURL());
        dto.setUserRole(admin.getUserRole());
        dto.setSuperAdminStatus(admin.getSuperAdminStatus());
        dto.setCreatedAt(admin.getCreatedAt());

        return dto;
    }
}
