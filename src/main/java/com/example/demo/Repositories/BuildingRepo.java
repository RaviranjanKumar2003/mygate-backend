package com.example.demo.Repositories;

import com.example.demo.Entities.Building;
import com.example.demo.Enums.BuildingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuildingRepo extends JpaRepository<Building,Integer> {

    List<Building> findBySociety_IdAndIsActive(Integer societyId,BuildingStatus status);

    Optional<Building> findByIdAndIsActive(Integer id,BuildingStatus status);

    List<Building> findByNameContainingIgnoreCaseAndIsActive(String name,BuildingStatus status);

    Optional<Building> findByIdAndSociety_Id(Integer id, Integer societyId);

}
