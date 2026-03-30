package com.example.demo.Services;

import com.example.demo.Payloads.SocietyAdminDto;

import java.util.List;

public interface SocietyAdminService {

    SocietyAdminDto updateSocietyAdminImage(Integer societyAdminId, String imageURL);


    // Get all society admins
    List<SocietyAdminDto> getAllSocietyAdmins();

    // Gell Society Admin By Id
    SocietyAdminDto getSocietyAdminById(Integer adminId);

    // Update society admin
    SocietyAdminDto updateSocietyAdmin(SocietyAdminDto dto, Integer adminId);

}
