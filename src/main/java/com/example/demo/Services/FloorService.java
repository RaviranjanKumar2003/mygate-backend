package com.example.demo.Services;

import com.example.demo.Enums.FloorStatus;
import com.example.demo.Payloads.FloorDto;
import com.example.demo.Payloads.FloorSummaryDto;

import java.util.List;

public interface FloorService {

// CREATE FLOOR

    FloorDto createFloor(FloorDto dto);

// GET FLOORS IN A BUILDING

    List<FloorDto> getFloorsByBuilding(int buildingId);

// GET FLOORS BY BUILDING + STATUS

    List<FloorDto> getFloorsByBuildingAndStatus(int buildingId, FloorStatus status);

// GET FLOORS BY SOCIETY + STATUS

    List<FloorDto> getFloorsBySocietyAndStatus(int societyId, FloorStatus status);


//  SUMMARY APIs
    // Building Summary
    FloorSummaryDto getBuildingFloorSummary(int buildingId);
    // Society Summary
    FloorSummaryDto getSocietyFloorSummary(int societyId);


// UPDATE FLOOR

    FloorDto updateFloor(int floorId, FloorDto dto);

// DELETE FLOOR (SOFT DELETE)

    void deleteFloor(int floorId);
}
