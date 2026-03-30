package com.example.demo.Services.Implementation;

import com.example.demo.Entities.Building;
import com.example.demo.Entities.Society;
import com.example.demo.Enums.BuildingStatus;
import com.example.demo.Enums.SocietyStatus;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.BuildingDto;
import com.example.demo.Repositories.BuildingRepo;
import com.example.demo.Repositories.SocietyRepo;
import com.example.demo.Services.BuildingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingRepo buildingRepo;

    @Autowired
    private SocietyRepo societyRepo;

    @Autowired
    private ModelMapper modelMapper;



// CREATE BUILDING
    @Override
    public BuildingDto createBuilding(Integer societyId, BuildingDto buildingDto) {

        Society society = societyRepo.findByIdAndIsActive(societyId, SocietyStatus.ACTIVE).orElseThrow(() ->new ResourceNotFoundException("Society", "Society Id", societyId));

        Building building = new Building();
        building.setName(buildingDto.getName());
        building.setSociety(society);

        Building saved = buildingRepo.save(building);
        return modelMapper.map(saved, BuildingDto.class);
    }


// GET ALL PENDING BUILDINGS by Society
@Override
public List<BuildingDto> getAllActiveBuildings(Integer societyId) {

    return buildingRepo.findBySociety_IdAndIsActive(societyId, BuildingStatus.ACTIVE)
            .stream()
            .map(b -> {
                BuildingDto dto = modelMapper.map(b, BuildingDto.class);
                dto.setSocietyId(b.getSociety().getId());
                return dto;
            })
            .collect(Collectors.toList());
}


    // GET ALL IN BUILDINGS
    @Override
    public List<BuildingDto> getAllDeActiveBuildings(Integer societyId) {

        return buildingRepo.findBySociety_IdAndIsActive(societyId, BuildingStatus.INACTIVE)
                .stream()
                .map(b -> {
                    BuildingDto dto = modelMapper.map(b, BuildingDto.class);
                    dto.setSocietyId(b.getSociety().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // GET BUILDING BY ID (PENDING ONLY)
    @Override
    public BuildingDto getBuildingById(Integer buildingId) {

        Building building = buildingRepo
                .findByIdAndIsActive(buildingId, BuildingStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Building", "Building Id", buildingId
                        ));

        BuildingDto dto = modelMapper.map(building, BuildingDto.class);
        dto.setSocietyId(building.getSociety().getId());
        return dto;
    }

    // UPDATE BUILDING
    @Override
    public BuildingDto updateBuilding(BuildingDto dto, Integer buildingId) {

        Building building = buildingRepo
                .findByIdAndIsActive(buildingId, BuildingStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Building", "Building Id", buildingId
                        ));

        building.setName(dto.getName());

        Building updated = buildingRepo.save(building);
        return modelMapper.map(updated, BuildingDto.class);
    }

    // SOFT DELETE BUILDING
    @Override
    public void deleteBuilding(Integer buildingId) {

        Building building = buildingRepo
                .findByIdAndIsActive(buildingId, BuildingStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Building", "Building Id", buildingId
                        ));

        //building.setIsActive(BuildingStatus.IN);
        //buildingRepo.save(building);
        buildingRepo.delete(building);
    }

    // SEARCH BUILDING (PENDING ONLY)
    @Override
    public List<BuildingDto> searchBuildingByName(String keyword) {

        return buildingRepo
                .findByNameContainingIgnoreCaseAndIsActive(
                        keyword,
                        BuildingStatus.ACTIVE
                )
                .stream()
                .map(b -> {
                    BuildingDto dto = modelMapper.map(b, BuildingDto.class);
                    dto.setSocietyId(b.getSociety().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
