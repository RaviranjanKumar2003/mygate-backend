package com.example.demo.Repositories;

import com.example.demo.Entities.Building;
import com.example.demo.Entities.Floor;
import com.example.demo.Entities.Society;
import com.example.demo.Enums.BuildingStatus;
import com.example.demo.Enums.FloorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FloorRepository extends JpaRepository<Floor, Integer> {


    List<Floor> findByBuilding(Building building);

    List<Floor> findByBuildingAndFloorStatus(Building building, FloorStatus status);

    List<Floor> findBySocietyAndFloorStatus(Society society, FloorStatus status);

    boolean existsByBuildingAndFloorNumber(Building building, String floorNumber);


    // FOR COUNT
    long countByBuilding(Building building);

    long countByBuildingAndFloorStatus(Building building, FloorStatus status);

    long countBySociety(Society society);

    long countBySocietyAndFloorStatus(Society society, FloorStatus status);


    Optional<Floor> findByIdAndBuildingIsActive(Integer id, BuildingStatus status);

    Optional<Floor> findByIdAndSociety_Id(Integer id, Integer societyId);






}
