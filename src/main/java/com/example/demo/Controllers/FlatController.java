package com.example.demo.Controllers;

import com.example.demo.Enums.FlatStatus;
import com.example.demo.Payloads.FlatCountResponse;
import com.example.demo.Payloads.FlatDto;
import com.example.demo.Services.FlatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flats")
@CrossOrigin
public class FlatController {

    private final FlatService flatService;

    public FlatController(FlatService flatService) {
        this.flatService = flatService;
    }


// CREATE FLATS
    @PostMapping("/create")
    public ResponseEntity<FlatDto> createFlat(@RequestBody FlatDto dto) {
        return ResponseEntity.ok(flatService.createFlat(dto));
    }


// GET ALL FLATS by ID
    @GetMapping("/flat/{flatId}")
    public ResponseEntity<FlatDto> getFlat(@PathVariable Integer flatId) {
        return ResponseEntity.ok(flatService.getFlatById(flatId));
    }


// GET FLATS BY FLOOR
    @GetMapping("/society/{societyId}/building/{buildingId}/floor/{floorId}")
    public ResponseEntity<List<FlatDto>> getByFloor(@PathVariable Integer floorId) {
        return ResponseEntity.ok(flatService.getFlatsByFloor(floorId));
    }


// GET FLATS BY BUILDING
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<List<FlatDto>> getByBuilding(@PathVariable Integer buildingId) {
        return ResponseEntity.ok(flatService.getFlatsByBuilding(buildingId));
    }


// GET FLATS BY SOCIETY
    @GetMapping("/society/{societyId}")
    public ResponseEntity<List<FlatDto>> getBySociety(@PathVariable Integer societyId) {
        return ResponseEntity.ok(flatService.getFlatsBySociety(societyId));
    }

    @GetMapping("/society/{societyId}/status/{status}")
    public ResponseEntity<List<FlatDto>> getFlatsBySocietyAndStatus(
            @PathVariable Integer societyId,
            @PathVariable String status
    ) {
        FlatStatus flatStatus = FlatStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(
                flatService.getFlatsBySocietyAndStatus(societyId, flatStatus)
        );
    }



    //// GET ALL FLATS IN A SOCIETY
//    @GetMapping("/society/{societyId}/status/{status}")
//    public ResponseEntity<List<FlatDto>> getFlatsBySociety(
//            @PathVariable Integer societyId,
//            @RequestParam(required = false) FlatStatus status
//    ) {
//        if (status != null) {
//            return ResponseEntity.ok(
//                    flatService.getFlatsBySocietyAndStatus(societyId, status)
//            );
//        }
//
//        return ResponseEntity.ok(
//                flatService.getFlatsBySociety(societyId)
//        );
//    }




    /* ================= COUNT APIs ================= */

// GET FLATS COUNT IN A SOCIETY BY STATUS
    @GetMapping("/society/{societyId}/status/{status}/count")
    public ResponseEntity<Long> getSocietyFlatCount(
            @PathVariable Integer societyId,
            @PathVariable FlatStatus status
    ) {
        return ResponseEntity.ok(
                flatService.countFlatsBySocietyAndStatus(societyId, status)
        );
    }


// GET FLATS COUNT IN A BUILDING
    @GetMapping("/society/{societyId}/building/{buildingId}/status/{status}/count")
    public ResponseEntity<Long> getBuildingFlatCount(
            @PathVariable Integer societyId,
            @PathVariable Integer buildingId,
            @PathVariable FlatStatus status
    ) {
        return ResponseEntity.ok(
                flatService.countFlatsByBuildingAndStatus(
                        societyId,
                        buildingId,
                        status
                )
        );
    }

// GET FLATS COUNT IN A FLOOR
    @GetMapping("/society/{societyId}/building/{buildingId}/floor/{floorId}/status/{status}/count")
    public ResponseEntity<Long> getFloorFlatCount(
            @PathVariable Integer societyId,
            @PathVariable Integer buildingId,
            @PathVariable Integer floorId,
            @PathVariable FlatStatus status
    ) {
        return ResponseEntity.ok(
                flatService.countFlatsByFloorAndStatus(
                        societyId,
                        buildingId,
                        floorId,
                        status
                )
        );
    }



// GET FLATS IN FLOOR BY STATUS
    @GetMapping("society/{societyId}/building/{buildingId}/floor/{floorId}/status/{status}")
    public ResponseEntity<List<FlatDto>> getByFloorAndStatus(
            @PathVariable Integer floorId,
            @PathVariable FlatStatus status
    ) {
        return ResponseEntity.ok(flatService.getFlatsByFloorAndStatus(floorId, status));
    }


// GET FLATS COUNT BY FLOOR
    @GetMapping("society/{societyId}/building/{buildingId}/floor/{floorId}/count")
    public ResponseEntity<FlatCountResponse> getFlatCountByFloor(
            @PathVariable Integer floorId
    ) {
        return ResponseEntity.ok(flatService.getFlatCountByFloor(floorId));
    }


// GET FLATS COUNT BY BUILDING
    @GetMapping("/society/{societyId}/building/{buildingId}/count")
    public ResponseEntity<FlatCountResponse> getFlatCountByBuilding(
            @PathVariable Integer buildingId
    ) {
        return ResponseEntity.ok(flatService.getFlatCountByBuilding(buildingId));
    }

// GET FLATS COUNT BY FLOOR
    @GetMapping("/society/{societyId}/count")
    public ResponseEntity<FlatCountResponse> getFlatCountBySociety(
            @PathVariable Integer societyId
    ) {
        return ResponseEntity.ok(flatService.getFlatCountBySociety(societyId));
    }



// UPDATE FLAT
    @PutMapping("/society/{societyId}/building/{buildingId}/floor/{floorId}/flat/{flatId}")
    public ResponseEntity<FlatDto> updateFlat(
            @PathVariable("societyId") Integer societyId,
            @PathVariable("buildingId") Integer buildingId,
            @PathVariable("floorId") Integer floorId,
            @PathVariable("flatId") Integer flatId,
            @RequestBody FlatDto dto
    ) {
        // Optional: hierarchy validation later
        return ResponseEntity.ok(flatService.updateFlat(flatId, dto));
    }

// UPDATE FLAT STATUS
@PutMapping("/society/{societyId}/building/{buildingId}/floor/{floorId}/flat/{flatId}/status")
public ResponseEntity<String> updateFlatStatus(
        @PathVariable Integer flatId,
        @RequestBody FlatDto dto
) {
    if (dto.getFlatStatus() == null) {
        throw new IllegalArgumentException("Flat status is required");
    }

    flatService.updateFlatStatus(flatId, dto.getFlatStatus());
    return ResponseEntity.ok("Flat status updated successfully");
}




    @PutMapping("/society/{societyId}/block")
    public ResponseEntity<String> blockSocietyFlats(
            @PathVariable Integer societyId
    ) {
        flatService.blockFlatsBySociety(societyId);
        return ResponseEntity.ok("All flats blocked for society");
    }


    @PutMapping("/society/{societyId}/building/{buildingId}/block")
    public ResponseEntity<String> blockBuildingFlats(
            @PathVariable Integer buildingId
    ) {
        flatService.blockFlatsByBuilding(buildingId);
        return ResponseEntity.ok("All flats blocked for building");
    }


    @PutMapping("/society/{societyId}/building/{buildingId}/floor/{floorId}/block")
    public ResponseEntity<String> blockFloorFlats(
            @PathVariable Integer floorId
    ) {
        flatService.blockFlatsByFloor(floorId);
        return ResponseEntity.ok("All flats blocked for floor");
    }









    // DELETE FLATS
    @DeleteMapping("society/{societyId}/building/{buildingId}/floor/{floorId}/flat/{flatId}")
    public ResponseEntity<String> deleteFlat(@PathVariable Integer flatId) {
        flatService.softDeleteFlat(flatId);
        return ResponseEntity.ok("Flat BLOCKED successfully");
    }
}
