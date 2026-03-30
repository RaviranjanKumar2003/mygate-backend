package com.example.demo.SuperAdminFolder;

import java.util.List;

public interface SuperAdminService {

    SuperAdminDto createSuperAdmin(SuperAdminDto dto);

    SuperAdminDto getSuperAdminById(int id);

    List<SuperAdminDto> getAllSuperAdmins();

    SuperAdminDto updateSuperAdmin(int id, SuperAdminDto dto);

    void deactivateSuperAdmin(int id);


    SuperAdminDto updateSuperAdminImage(Integer superAdminId, String imageName);


}
