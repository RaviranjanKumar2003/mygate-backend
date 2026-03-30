package com.example.demo.Services.Implementation;

import com.example.demo.Entities.SocietyAdmin;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.SocietyAdminDto;
import com.example.demo.Repositories.SocietyAdminRepository;
import com.example.demo.Services.SocietyAdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocietyAdminServiceImpl implements SocietyAdminService {

    @Autowired
    private SocietyAdminRepository societyAdminRepository;

    @Autowired
    private ModelMapper modelMapper;


// GET ALL SOCIETY ADMIN
    @Override
    public List<SocietyAdminDto> getAllSocietyAdmins() {
        List<SocietyAdmin> admins = societyAdminRepository.findAll();
        return admins.stream()
                .map(admin -> modelMapper.map(admin, SocietyAdminDto.class))
                .collect(Collectors.toList());
    }

// GET BY ID
    @Override
    public SocietyAdminDto getSocietyAdminById(Integer adminId) {
        SocietyAdmin admin = societyAdminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("SocietyAdmin", "id", adminId));

        return modelMapper.map(admin, SocietyAdminDto.class);
    }


// UPDATE SOCIETY ADMIN
    @Override
    public SocietyAdminDto updateSocietyAdmin(SocietyAdminDto dto, Integer adminId) {
        SocietyAdmin admin = societyAdminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("SocietyAdmin", "id", adminId));

        // Update fields
        admin.setAdminName(dto.getAdminName() != null ? dto.getAdminName() : admin.getAdminName());
        admin.setAdminEmail(dto.getAdminEmail() != null ? dto.getAdminEmail() : admin.getAdminEmail());
        admin.setMobileNumber(dto.getMobileNumber() != null ? dto.getMobileNumber() : admin.getMobileNumber());
        // Add any other fields you want to update

        SocietyAdmin updated = societyAdminRepository.save(admin);
        return modelMapper.map(updated, SocietyAdminDto.class);
    }






    // ================= IMAGE UPDATE =================
    @Override
    public SocietyAdminDto updateSocietyAdminImage(Integer societyAdminId, String imageURL) {

        SocietyAdmin admin = societyAdminRepository.findById(societyAdminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SocietyAdmin",
                        "id",
                        societyAdminId
                ));

        admin.setImageURL(imageURL);

        // Save updated entity
        SocietyAdmin updated = societyAdminRepository.save(admin);

        // Directly create DTO (null-safe)
        SocietyAdminDto dto = new SocietyAdminDto();
        dto.setAdminName(updated.getAdminName());
        dto.setAdminEmail(updated.getAdminEmail());  // Will be null only if entity field is null
        dto.setMobileNumber(updated.getMobileNumber());
        dto.setImageURL(updated.getImageURL());
        dto.setSocietyAdminStatus(updated.getSocietyAdminStatus());
        dto.setSocietyId(updated.getSociety().getId());

        return dto;
    }

}
