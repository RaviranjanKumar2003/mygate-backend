package com.example.demo.Services.Implementation;

import com.example.demo.Entities.*;
import com.example.demo.Enums.FlatStatus;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.FlatCountResponse;
import com.example.demo.Payloads.FlatDto;
import com.example.demo.Repositories.*;
import com.example.demo.Services.FlatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FlatServiceImpl implements FlatService {

    @Autowired
    private ModelMapper modelMapper;

    private final FlatRepository flatRepo;
    private final FloorRepository floorRepo;
    private BuildingRepo buildingRepo;
    private SocietyRepo societyRepo;

    public FlatServiceImpl(
            FlatRepository flatRepo,
            FloorRepository floorRepo,
            BuildingRepo buildingRepo,
            SocietyRepo societyRepo
    ) {
        this.flatRepo = flatRepo;
        this.floorRepo = floorRepo;
        this.buildingRepo = buildingRepo;
        this.societyRepo = societyRepo;
    }


// CREATE FLATS
    @Override
    public FlatDto createFlat(FlatDto dto) {

        Floor floor = floorRepo.findById(dto.getFloorId())
                .orElseThrow(() -> new RuntimeException("Floor not found"));

        Building building = buildingRepo.findById(dto.getBuildingId())
                .orElseThrow(() -> new RuntimeException("Building not found"));

        Society society = societyRepo.findById(dto.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));

        // ✅ VALIDATION: Floor belongs to building
        if (!floor.getBuilding().getId().equals(building.getId())) {
            throw new IllegalStateException("Floor does not belong to this building");
        }

        // ✅ VALIDATION: Building belongs to society
        if (!building.getSociety().getId().equals(society.getId())) {
            throw new IllegalStateException("Building does not belong to this society");
        }

        // ✅ DUPLICATE CHECK
        boolean exists = flatRepo.existsBySocietyAndBuildingAndFloorAndFlatNumber(
                society,
                building,
                floor,
                dto.getFlatNumber().trim()
        );

        if (exists) {
            throw new IllegalStateException(
                    "Flat " + dto.getFlatNumber() + " already exists on this floor"
            );
        }

        Flat flat = new Flat();
        flat.setFlatNumber(dto.getFlatNumber().trim());
        flat.setFlatStatus(
                dto.getFlatStatus() != null ? dto.getFlatStatus() : FlatStatus.VACANT
        );
        flat.setFloor(floor);
        flat.setBuilding(building);
        flat.setSociety(society);

        return mapToDto(flatRepo.save(flat));
    }



// GET FLATS BY FLOOR
    @Override
    public List<FlatDto> getFlatsByFloor(Integer floorId) {

        return flatRepo.findByFloorId(floorId).stream().map(this::mapToDto).collect(Collectors.toList());

    }


// GET FLAT BY BUILDING
    @Override
    public List<FlatDto> getFlatsByBuilding(Integer buildingId) {
        return flatRepo.findByBuildingId(buildingId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }


// GET FLAT BY SOCIETY
    @Override
    public List<FlatDto> getFlatsBySociety(Integer societyId) {
        return flatRepo.findBySocietyId(societyId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }


// GET FLATS IN A SOCIETY BY STATUS
    @Override
    public List<FlatDto> getFlatsBySocietyAndStatus(Integer societyId, FlatStatus status) {

        return flatRepo.findBySocietyIdAndFlatStatus(societyId, status)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    /* ================= COUNT ================= */

// GET FLATS COUNT IN A SOCIETY
    @Override
    public long countFlatsBySocietyAndStatus(
            Integer societyId,
            FlatStatus status
    ) {
        return flatRepo.countBySocietyIdAndFlatStatus(societyId, status);
    }


// GET FLATS COUNT IN A BUILDING
    @Override
    public long countFlatsByBuildingAndStatus(
            Integer societyId,
            Integer buildingId,
            FlatStatus status
    ) {
        return flatRepo.countBySocietyIdAndBuildingIdAndFlatStatus(
                societyId,
                buildingId,
                status
        );
    }

// GET FLATS COUNT IN A FLOOR
    @Override
    public long countFlatsByFloorAndStatus(
            Integer societyId,
            Integer buildingId,
            Integer floorId,
            FlatStatus status
    ) {
        return flatRepo.countBySocietyIdAndBuildingIdAndFloorIdAndFlatStatus(
                societyId,
                buildingId,
                floorId,
                status
        );
    }




// GET FLATS BY FLOOR AND STATUS
    @Override
    public List<FlatDto> getFlatsByFloorAndStatus(Integer floorId, FlatStatus status) {
        return flatRepo.findByFloorIdAndFlatStatus(floorId, status)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }


// GET FLATS BY ID
    @Override
    public FlatDto getFlatById(Integer flatId) {
        Flat flat = flatRepo.findById(flatId)
                .orElseThrow(() -> new RuntimeException("Flat not found"));
        return mapToDto(flat);
    }


// GET FLATS COUNT BY FLOOR
    @Override
    public FlatCountResponse getFlatCountByFloor(Integer floorId) {

        List<Object[]> result = flatRepo.countByFloorStatus(floorId);

        Map<FlatStatus, Integer> map = new EnumMap<>(FlatStatus.class);
        int total = 0;

        for (Object[] row : result) {
            FlatStatus status = (FlatStatus) row[0];
            Integer count = ((Long) row[1]).intValue();

            map.put(status, count);
            total += count;
        }

        FlatCountResponse response = new FlatCountResponse();
        response.setTotalFlats(total);
        response.setStatusWiseCount(map);

        return response;
    }


// GET FLATS COUNT BY BUILDING
    @Override
    public FlatCountResponse getFlatCountByBuilding(Integer buildingId) {

        List<Object[]> result = flatRepo.countByBuildingStatus(buildingId);

        Map<FlatStatus, Integer> map = new EnumMap<>(FlatStatus.class);
        int total = 0;

        for (Object[] row : result) {
            FlatStatus status = (FlatStatus) row[0];
            Integer count = ((Long) row[1]).intValue();

            map.put(status, count);
            total += count;
        }

        FlatCountResponse response = new FlatCountResponse();
        response.setTotalFlats(total);
        response.setStatusWiseCount(map);

        return response;
    }



// GET FLATS COUNT BY SOCIETY
    @Override
    public FlatCountResponse getFlatCountBySociety(Integer societyId) {

        List<Object[]> result = flatRepo.countBySocietyStatus(societyId);

        Map<FlatStatus, Integer> map = new EnumMap<>(FlatStatus.class);
        int total = 0;

        for (Object[] row : result) {
            FlatStatus status = (FlatStatus) row[0];
            Integer count = ((Long) row[1]).intValue();

            map.put(status, count);
            total += count;
        }

        FlatCountResponse response = new FlatCountResponse();
        response.setTotalFlats(total);
        response.setStatusWiseCount(map);

        return response;
    }



// UPDATE FLAT (SAFE)
    @Override
    public FlatDto updateFlat(Integer flatId, FlatDto dto) {

        Flat flat = flatRepo.findById(flatId)
                .orElseThrow(() -> new RuntimeException("Flat not found"));

        // update only if value is provided
        if (dto.getFlatNumber() != null) {
            flat.setFlatNumber(dto.getFlatNumber());
        }

        if (dto.getFlatStatus() != null) {
            flat.setFlatStatus(dto.getFlatStatus());
        }

        return mapToDto(flatRepo.save(flat));
    }


// UPDATE FLATS BY STATUS
    @Override
    public void updateFlatStatus(Integer flatId, FlatStatus status) {
        Flat flat = flatRepo.findById(flatId)
                .orElseThrow(() -> new ResourceNotFoundException("Flat", "id", flatId));

        flat.setFlatStatus(status);
        flatRepo.save(flat);
    }


    /* ================= SOCIETY LEVEL ================= */
    @Override
    public void blockFlatsBySociety(Integer societyId) {
        List<Flat> flats = flatRepo.findBySocietyId(societyId);
        flats.forEach(f -> f.setFlatStatus(FlatStatus.BLOCKED));
        flatRepo.saveAll(flats);
    }

    /* ================= BUILDING LEVEL ================= */
    @Override
    public void blockFlatsByBuilding(Integer buildingId) {
        List<Flat> flats = flatRepo.findByBuildingId(buildingId);
        flats.forEach(f -> f.setFlatStatus(FlatStatus.BLOCKED));
        flatRepo.saveAll(flats);
    }

    /* ================= FLOOR LEVEL ================= */
    @Override
    public void blockFlatsByFloor(Integer floorId) {
        List<Flat> flats = flatRepo.findByFloorId(floorId);
        flats.forEach(f -> f.setFlatStatus(FlatStatus.BLOCKED));
        flatRepo.saveAll(flats);
    }






// SOFT DELETE
    @Override
    public void softDeleteFlat(Integer flatId) {
        Flat flat = flatRepo.findById(flatId)
                .orElseThrow(() -> new RuntimeException("Flat not found"));

        flat.setFlatStatus(FlatStatus.BLOCKED);
        flatRepo.save(flat);
    }



// ==================== MAPPER ======================
    private FlatDto mapToDto(Flat flat) {

        FlatDto dto = new FlatDto();
        dto.setId(flat.getId());
        dto.setFlatNumber(flat.getFlatNumber());
        dto.setFlatStatus(flat.getFlatStatus());

        dto.setFloorId(flat.getFloor().getId());
        dto.setFloorNumber(flat.getFloor().getFloorNumber());

        dto.setBuildingId(flat.getBuilding().getId());
        dto.setBuildingName(flat.getBuilding().getName());

        dto.setSocietyId(flat.getSociety().getId());
        dto.setSocietyName(flat.getSociety().getName());

        return dto;
    }
}
