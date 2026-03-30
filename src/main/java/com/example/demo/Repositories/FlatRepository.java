package com.example.demo.Repositories;

import com.example.demo.Entities.*;
import com.example.demo.Enums.FlatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FlatRepository extends JpaRepository<Flat, Integer> {

    List<Flat> findByFloorId(Integer floorId);

    List<Flat> findByBuildingId(Integer buildingId);

    List<Flat> findBySocietyId(Integer societyId);

    List<Flat> findByFloorIdAndFlatStatus(Integer floorId, FlatStatus status);

    boolean existsBySocietyAndBuildingAndFloorAndFlatNumber(
            Society society,
            Building building,
            Floor floor,
            String flatNumber
    );


    List<Flat> findBySocietyIdAndFlatStatus(Integer societyId, FlatStatus flatStatus);

    // ✅ SOCIETY LEVEL
    long countBySocietyIdAndFlatStatus(Integer societyId, FlatStatus status);

    // ✅ BUILDING LEVEL
    long countBySocietyIdAndBuildingIdAndFlatStatus(
            Integer societyId,
            Integer buildingId,
            FlatStatus flatStatus
    );

    // ✅ FLOOR LEVEL
    long countBySocietyIdAndBuildingIdAndFloorIdAndFlatStatus(
            Integer societyId,
            Integer buildingId,
            Integer floorId,
            FlatStatus status
    );

    Optional<Flat> findByIdAndSociety_Id(Integer id, Integer societyId);

    Optional<Flat> findByIdAndFloor_IdAndFloor_Building_IdAndFloor_Building_Society_Id(
            Integer flatId,
            Integer floorId,
            Integer buildingId,
            Integer societyId
    );


    boolean existsByIdAndSociety_Id(Integer userId, Integer societyId);





    // COUNT FLATS IN FLOOR
    @Query("""
    SELECT f.flatStatus, COUNT(f)
    FROM Flat f
    WHERE f.floor.id = :floorId
    GROUP BY f.flatStatus
    """)
    List<Object[]> countByFloorStatus(Integer floorId);


    // 🔹 BUILDING
    @Query("""
        SELECT f.flatStatus, COUNT(f)
        FROM Flat f
        WHERE f.building.id = :buildingId
        GROUP BY f.flatStatus
    """)
    List<Object[]> countByBuildingStatus(@Param("buildingId") Integer buildingId);

    // 🔹 SOCIETY
    @Query("""
        SELECT f.flatStatus, COUNT(f)
        FROM Flat f
        WHERE f.society.id = :societyId
        GROUP BY f.flatStatus
    """)
    List<Object[]> countBySocietyStatus(@Param("societyId") Integer societyId);


}
