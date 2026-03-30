package com.example.demo.Services;

import com.example.demo.Enums.FlatStatus;
import com.example.demo.Payloads.FlatCountResponse;
import com.example.demo.Payloads.FlatDto;

import java.util.List;
import java.util.Map;

public interface FlatService {

// 1. CREATE FLATS
    FlatDto createFlat(FlatDto dto);


// 2. GET ALL FLATS BY FLOOR
    List<FlatDto> getFlatsByFloor(Integer floorId);


// 3.  GET ALL FLATS BY BUILDING
    List<FlatDto> getFlatsByBuilding(Integer buildingId);


// 4.  GET ALL FLATS BY SOCIETY
    List<FlatDto> getFlatsBySociety(Integer societyId);


// 5. GET ALL FLATS IN A SOCIETY BY STATUS
    List<FlatDto> getFlatsBySocietyAndStatus(Integer societyId, FlatStatus status);


    /* ================= COUNT ================= */

// 6. GET FLATS COUNT IN A SOCIETY
    long countFlatsBySocietyAndStatus(
            Integer societyId,
            FlatStatus status
    );



// 7. GET FLATS COUNT IN A BUILDING
    long countFlatsByBuildingAndStatus(
            Integer societyId,
            Integer buildingId,
            FlatStatus status
    );



// 8. GET FLATS COUNT IN A FLOOR
    long countFlatsByFloorAndStatus(
            Integer societyId,
            Integer buildingId,
            Integer floorId,
            FlatStatus status
    );



// 9.  GET FLATS BY FLOOR AND STATUS
    List<FlatDto> getFlatsByFloorAndStatus(Integer floorId, FlatStatus status);


// 10.  GET FLATS BY ID
    FlatDto getFlatById(Integer flatId);


// 11.  GET TOTAL FLAT COUNT BY FLOOR
    FlatCountResponse getFlatCountByFloor(Integer floorId);


// 12.  GET TOTAL FLAT COUNT BY BUILDING
    FlatCountResponse getFlatCountByBuilding(Integer buildingId);

// 13.  GET TOTAL FLAT COUNT BY SOCIETY
    FlatCountResponse getFlatCountBySociety(Integer societyId);


// 14.  UPDATE FLATS
    FlatDto updateFlat(Integer flatId, FlatDto dto);


    void updateFlatStatus(Integer flatId, FlatStatus status);

    void blockFlatsBySociety(Integer societyId);

    void blockFlatsByBuilding(Integer buildingId);

    void blockFlatsByFloor(Integer floorId);





// 15.  DELETE FLATS
    void softDeleteFlat(Integer flatId); // BLOCK


}
