package com.example.demo.Services.Implementation;

import com.example.demo.Entities.Building;
import com.example.demo.Entities.Floor;
import com.example.demo.Entities.Society;
import com.example.demo.Enums.BuildingStatus;
import com.example.demo.Enums.FloorStatus;
import com.example.demo.Enums.SocietyStatus;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.FloorDto;
import com.example.demo.Payloads.FloorSummaryDto;
import com.example.demo.Repositories.BuildingRepo;
import com.example.demo.Repositories.FloorRepository;
import com.example.demo.Repositories.SocietyRepo;
import com.example.demo.Services.FloorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FloorServiceImpl implements FloorService {

    private final FloorRepository floorRepo;
    private final BuildingRepo buildingRepo;
    private final SocietyRepo societyRepo;


    public FloorServiceImpl(
            FloorRepository floorRepo,
            BuildingRepo buildingRepo,
            SocietyRepo societyRepo) {
        this.floorRepo = floorRepo;
        this.buildingRepo = buildingRepo;
        this.societyRepo = societyRepo;
    }

    @Override
    public FloorDto createFloor(FloorDto dto) {

        // ================= FETCH SOCIETY =================
        Society society = societyRepo.findById(dto.getSocietyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Society", "id", dto.getSocietyId()));

        if (society.getIsActive() != SocietyStatus.ACTIVE) {
            throw new IllegalStateException("Society is IN");
        }

        // ================= FETCH BUILDING =================
        Building building = buildingRepo.findById(dto.getBuildingId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Building", "id", dto.getBuildingId()));

        if (building.getIsActive() != BuildingStatus.ACTIVE) {
            throw new IllegalStateException("Building is IN");
        }

        if (!building.getSociety().getId().equals(society.getId())) {
            throw new IllegalStateException("Building does not belong to given Society");
        }

        // ================= FLOOR NUMBER LOGIC =================
        String floorNumber = dto.getFloorNumber();

        if (floorNumber == null || floorNumber.isBlank()) {
            floorNumber = "Ground Floor";
        }

        // 🚫 DUPLICATE FLOOR CHECK (IMPORTANT)
        if (floorRepo.existsByBuildingAndFloorNumber(building, floorNumber)) {
            throw new IllegalStateException(
                    "Floor '" + floorNumber + "' already exists in this building"
            );
        }

        // ================= CREATE FLOOR =================
        Floor floor = new Floor();
        floor.setFloorNumber(floorNumber);
        floor.setFloorStatus(FloorStatus.ACTIVE);
        floor.setBuilding(building);
        floor.setSociety(society);

        return mapToDto(floorRepo.save(floor));
    }



// GET FLOORS BY BUILDING
    @Override
    public List<FloorDto> getFloorsByBuilding(int buildingId) {

        Building building = buildingRepo.findById(buildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Building", "id", buildingId));

        return floorRepo.findByBuilding(building).stream().map(this::mapToDto).toList();
    }


// GET FLOORS BY BUILDING + STATUS
    @Override
    public List<FloorDto> getFloorsByBuildingAndStatus(int buildingId, FloorStatus status) {

        Building building = buildingRepo.findById(buildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Building", "id", buildingId));

        return floorRepo.findByBuildingAndFloorStatus(building, status)
                .stream().map(this::mapToDto).toList();
    }


// GET FLOORS BY SOCIETY + STATUS
    @Override
    public List<FloorDto> getFloorsBySocietyAndStatus(int societyId, FloorStatus status) {
        Society society = societyRepo.findById(societyId)
                .orElseThrow(() -> new ResourceNotFoundException("Society", "id", societyId));

        return floorRepo.findBySocietyAndFloorStatus(society, status).stream().map(this::mapToDto).toList();
    }


// GET BUILDING FLOOR SUMMARY
    @Override
    public FloorSummaryDto getBuildingFloorSummary(int buildingId) {

        Building building = buildingRepo.findById(buildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Building", "id", buildingId));

        long total = floorRepo.countByBuilding(building);
        long active = floorRepo.countByBuildingAndFloorStatus(building, FloorStatus.ACTIVE);
        long inactive = floorRepo.countByBuildingAndFloorStatus(building, FloorStatus.INACTIVE);

        return new FloorSummaryDto(total, active, inactive);
    }


// GET SOCIETY FLOOR SUMMARY
    @Override
    public FloorSummaryDto getSocietyFloorSummary(int societyId) {

        Society society = societyRepo.findById(societyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Society", "id", societyId));

        long total = floorRepo.countBySociety(society);
        long active = floorRepo.countBySocietyAndFloorStatus(society, FloorStatus.ACTIVE);
        long inactive = floorRepo.countBySocietyAndFloorStatus(society, FloorStatus.INACTIVE);

        return new FloorSummaryDto(total, active, inactive);
    }


// UPDATE FLOOR
   @Override
   public FloorDto updateFloor(int floorId, FloorDto dto) {
       Floor floor = floorRepo.findByIdAndBuildingIsActive(floorId, BuildingStatus.ACTIVE)
               .orElseThrow(() -> new ResourceNotFoundException("Floor", "id", floorId));

       if (dto.getFloorNumber() != null) {
           floor.setFloorNumber(dto.getFloorNumber());
       }
       if (dto.getFloorStatus() != null) {
           floor.setFloorStatus(dto.getFloorStatus());
       }

       return mapToDto(floorRepo.save(floor));
   }


// DELETE FLOOR (SOFT DELETE)
   @Override
   public void deleteFloor(int floorId) {
       Floor floor = floorRepo.findByIdAndBuildingIsActive(floorId, BuildingStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Floor", "id", floorId));

       floor.setFloorStatus(FloorStatus.INACTIVE);
       floorRepo.save(floor);
   }

    // DELETE FLOOR (HARD DELETE)
//    @Override
//    public void deleteFloor(int floorId) {
//        Floor floor = floorRepo.findById(floorId)
//                .orElseThrow(() -> new ResourceNotFoundException("Floor", "id", floorId));
//
//        floorRepo.delete(floor);
//    }



    // MAPPER HELPER METHOD
    private FloorDto mapToDto(Floor floor) {
        FloorDto dto = new FloorDto();
        dto.setId(floor.getId());
        dto.setFloorNumber(floor.getFloorNumber());
        dto.setFloorStatus(floor.getFloorStatus());
        dto.setBuildingId(floor.getBuilding().getId());
        dto.setSocietyId(floor.getSociety().getId());
        return dto;
    }
}
