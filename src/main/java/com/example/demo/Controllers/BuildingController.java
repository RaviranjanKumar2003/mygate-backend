package com.example.demo.Controllers;

import com.example.demo.Payloads.BuildingDto;
import com.example.demo.Services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/societies/{societyId}/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;


// CREATE
    @PostMapping
    public ResponseEntity<BuildingDto> createBuilding(
            @PathVariable Integer societyId,
            @RequestBody BuildingDto dto) {

        return ResponseEntity.ok(
                buildingService.createBuilding(societyId, dto)
        );
    }


// GET ALL Active Building by Society
    @GetMapping
    public ResponseEntity<List<BuildingDto>> getAllActiveBuildings(@PathVariable Integer societyId) {
        return ResponseEntity.ok(buildingService.getAllActiveBuildings(societyId));
    }

// GET ALL De-Active Building By Society
    @GetMapping("/inactive")
    public ResponseEntity<List<BuildingDto>> getAllDeActiveBuildings(@PathVariable Integer societyId) {
        return ResponseEntity.ok(buildingService.getAllDeActiveBuildings(societyId));
    }

// GET BY ID
    @GetMapping("/{buildingId}")
    public ResponseEntity<BuildingDto> getBuildingById(@PathVariable Integer buildingId) {
        return ResponseEntity.ok(buildingService.getBuildingById(buildingId));
    }


// UPDATE
    @PutMapping("/{buildingId}")
    public ResponseEntity<BuildingDto> updateBuilding(
            @RequestBody BuildingDto dto,
            @PathVariable Integer buildingId) {
        return ResponseEntity.ok(buildingService.updateBuilding(dto, buildingId));
    }


// SOFT DELETE
    @DeleteMapping("/{buildingId}")
    public ResponseEntity<String> deleteBuilding(@PathVariable Integer buildingId) {
        buildingService.deleteBuilding(buildingId);
        return ResponseEntity.ok("Building deleted successfully (soft delete)");
    }


// SEARCH
    @GetMapping("/search")
    public ResponseEntity<List<BuildingDto>> searchBuilding(
            @RequestParam String keyword) {
        return ResponseEntity.ok(buildingService.searchBuildingByName(keyword));
    }
}
