package com.example.demo.Services;

import com.example.demo.Payloads.SocietyAdminDto;
import com.example.demo.Payloads.SocietyDto;
import com.example.demo.SuperAdminFolder.SuperAdminDto;

import java.util.List;

public interface SocietyService {

// CREATE
    SocietyDto createSociety(SocietyDto societyDto);

// GET ALL Active Society
    List<SocietyDto> getAllActiveSocieties();

// GET ALL De-Active Society
    List<SocietyDto> getAllDeActiveSocieties();

// GET BY ID
    SocietyDto getSocietyById(Integer id);

    // UPDATE
    SocietyDto updateSociety(SocietyDto societyDto, Integer id);

    // DELETE
    void deleteSociety(Integer id);

    // SEARCH
    List<SocietyDto> searchSociety(String keyword);






}
