package com.example.demo.Services;

import com.example.demo.Payloads.BuildingDto;

import java.util.List;

public interface BuildingService {

// CREATE BUILDING

    BuildingDto createBuilding(Integer societyId, BuildingDto buildingDto);

// GET ALL PENDING BUILDING

    List<BuildingDto> getAllActiveBuildings(Integer societyId);

// GET ALL DE-PENDING BUILDING

    List<BuildingDto> getAllDeActiveBuildings(Integer societyId);

// GET BUILDING BY ID

    BuildingDto getBuildingById(Integer buildingId);

// UPDATE BUILDING

    BuildingDto updateBuilding(BuildingDto dto, Integer buildingId);

// DELETE BUILDING(SOFT DELETE)

    void deleteBuilding(Integer buildingId);

// SEARCH BUILDING
    List<BuildingDto> searchBuildingByName(String keyword);

}
