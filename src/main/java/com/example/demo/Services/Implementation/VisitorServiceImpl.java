package com.example.demo.Services.Implementation;

import com.example.demo.Entities.*;
import com.example.demo.Enums.VisitorPurpose;
import com.example.demo.Enums.VisitorStatus;
import com.example.demo.Enums.VisitorType;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.VisitorDto;
import com.example.demo.Repositories.*;
import com.example.demo.Services.VisitorService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private SocietyRepo societyRepo;
    @Autowired
    private BuildingRepo buildingRepo;
    @Autowired
    private FloorRepository floorRepository;
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CompanyRepository companyRepository;


// CREATE VISITOR
    @Override
    public VisitorDto createVisitor(VisitorDto dto) {

        visitorRepository.findByMobileNumber(dto.getMobileNumber())
                .ifPresent(v -> {
                    throw new RuntimeException("Visitor already exists with this mobile number");
                });

        Visitor visitor = new Visitor();
        visitor.setName(dto.getName());
        visitor.setImageURL(dto.getImageURL());
        visitor.setMobileNumber(dto.getMobileNumber());
        visitor.setVisitorPurpose(dto.getVisitorPurpose());
        if (dto.getVisitorStatus() != null) {
            visitor.setVisitorStatus(dto.getVisitorStatus());
        } else {
            visitor.setVisitorStatus(VisitorStatus.PENDING);
        }

        // Check past visits in the last 30 days
        int visitCount = visitorRepository.countByMobileNumberAndCreatedAtAfter(
                dto.getMobileNumber(),
                LocalDateTime.now().minusDays(30)
        );

        if (visitCount >= 5) {
            visitor.setVisitorType(VisitorType.FREQUENT);
        } else {
            visitor.setVisitorType(VisitorType.NORMAL);
        }

        /* ================== SET RELATIONSHIPS ================== */

        Society society = societyRepo.findById(dto.getSocietyId())
                .orElseThrow(() -> new ResourceNotFoundException("Society", "id", dto.getSocietyId()));

        Building building = buildingRepo.findById(dto.getBuildingId())
                .orElseThrow(() -> new ResourceNotFoundException("Building", "id", dto.getBuildingId()));

        Floor floor = floorRepository.findById(dto.getFloorId())
                .orElseThrow(() -> new ResourceNotFoundException("Floor", "id", dto.getFloorId()));

        Flat flat = flatRepository.findById(dto.getFlatId())
                .orElseThrow(() -> new ResourceNotFoundException("Flat", "id", dto.getFlatId()));

        visitor.setSociety(society);
        visitor.setBuilding(building);
        visitor.setFloor(floor);
        visitor.setFlat(flat);

        /* ================== COMPANY LOGIC ================== */

        if (dto.getCompanyId() != null) {
            Company company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", dto.getCompanyId()));

            visitor.setCompany(company);

            // CAB visitor → vehicle number required
            if (dto.getVisitorPurpose() == VisitorPurpose.CAB) {
                if (dto.getVehicleNumber() == null || dto.getVehicleNumber().isBlank()) {
                    throw new RuntimeException("Vehicle number is required for CAB visitor");
                }
                visitor.setVehicleNumber(dto.getVehicleNumber());
            }
        }

        /* ================== SAVE ================== */
        Visitor saved = visitorRepository.save(visitor);

        /* ================== MAP TO DTO ================== */
        VisitorDto response = mapToDto(saved);

        response.setBuildingName(saved.getFlat().getFloor().getBuilding().getName());
        response.setFloorNumber(saved.getFlat().getFloor().getFloorNumber());
        response.setFlatNumber(saved.getFlat().getFlatNumber());

        if (saved.getCompany() != null) {
            response.setCompanyId(saved.getCompany().getId());
            response.setCompanyName(saved.getCompany().getName());
            response.setCompanyLogo(saved.getCompany().getLogoUrl());
        }

        dto.setCreatedAt(visitor.getCreatedAt());
        response.setVehicleNumber(saved.getVehicleNumber());

        return response;
    }


// 1. GET ALL VISITORS IN A SOCIETY
    @Override
    public List<VisitorDto> getAllVisitors(Integer societyId) {

        return visitorRepository.findBySociety_Id(societyId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

// GET VISITORS FOR FLAT
   @Override
   public List<VisitorDto> getVisitorsForOwnerTenant( Integer societyId, Integer buildingId,Integer floorId,Integer flatId) {

       // 1️⃣ Validate full hierarchy (SECURITY + DATA CONSISTENCY)
       Flat flat = flatRepository
               .findByIdAndFloor_IdAndFloor_Building_IdAndFloor_Building_Society_Id(flatId, floorId, buildingId,societyId)
               .orElseThrow(() -> new ResourceNotFoundException( "Flat", "id", flatId));

       // 2️⃣ Fetch visitors only for this flat
       List<Visitor> visitors = visitorRepository.findByFlat_Id(flat.getId());

       // 3️⃣ Convert → DTO
       return visitors.stream().map(this::mapToDto).toList();
   }



// GET VISITORS FOR OWNER / TENANT BY STATUS
    @Override
    public List<VisitorDto> getVisitorsForOwnerTenantByStatus(
            Integer societyId,
            Integer buildingId,
            Integer floorId,
            Integer flatId,
            VisitorStatus status
    ) {

        // 1️⃣ Validate full hierarchy (SECURITY + DATA CONSISTENCY)
        Flat flat = flatRepository
                .findByIdAndFloor_IdAndFloor_Building_IdAndFloor_Building_Society_Id(
                        flatId,
                        floorId,
                        buildingId,
                        societyId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flat", "id", flatId)
                );

        // 2️⃣ Fetch visitors for this flat + status
        List<Visitor> visitors =
                visitorRepository.findByFlat_IdAndVisitorStatus(
                        flat.getId(),
                        status
                );

        // 3️⃣ Convert → DTO
        return visitors.stream()
                .map(this::mapToDto)
                .toList();
    }



// UPDATE VISITORS STATUS
   @Override
   @Transactional
   public VisitorDto updateVisitorStatus( Integer societyId, Integer visitorId, VisitorStatus status) {

    Visitor visitor = visitorRepository.findByIdAndSocietyId(visitorId, societyId)
            .orElseThrow(() -> new ResourceNotFoundException( "Visitor", "id", visitorId));

       visitor.setVisitorStatus(status);

       // Entry time (only once)
       if (status == VisitorStatus.IN && visitor.getInTime() == null) {
           visitor.setInTime(LocalDateTime.now());
       }

       // Exit time
       if (status == VisitorStatus.OUT) {
           visitor.setOutTime(LocalDateTime.now());
       }

       return mapToDto(visitorRepository.save(visitor));
   }






    // 2. GET VISITORS IN A SOCIETY BY ID
   @Override
   public VisitorDto getVisitorById(Integer societyId, Integer visitorId) {

       Visitor visitor = visitorRepository.findByIdAndSociety_Id(visitorId, societyId).orElseThrow(() -> new ResourceNotFoundException("Visitor","id", visitorId));

       return mapToDto(visitor);
   }


// 3. GET VISITORS IN A SOCIETY BY STATUS
   @Override
   public List<VisitorDto> getVisitorsBySocietyAndStatus(Integer societyId,VisitorStatus status ) {

       return visitorRepository
            .findBySociety_IdAndVisitorStatus(societyId, status).stream()
            .map(visitor -> modelMapper.map(visitor, VisitorDto.class)).toList();
   }




// 4. GET VISITORS IN A SOCIETY BY Type
    @Override
    public List<VisitorDto> getVisitorsBySocietyAndVisitorsType(Integer societyId, VisitorType visitorType) {
        List<Visitor> visitors = visitorRepository.findBySocietyIdAndVisitorType(societyId,visitorType);
        return visitors.stream()
                .map(visitor -> modelMapper.map(visitor, VisitorDto.class))
                .collect(Collectors.toList());
    }



// 5. UPDATE VISITORS
   @Override
   public VisitorDto updateVisitor( Integer societyId,Integer visitorId,VisitorDto dto) {
       // ✅ 1. Visitor must belong to same society
       Visitor visitor = visitorRepository.findByIdAndSociety_Id(visitorId, societyId)
            .orElseThrow(() -> new ResourceNotFoundException("Visitor", "id", visitorId));

       // ✅ 2. Update basic fields
       visitor.setName(dto.getName());
       visitor.setVisitorPurpose(dto.getVisitorPurpose());
       visitor.setMobileNumber(dto.getMobileNumber());

       // ❌ DO NOT allow society update
       // visitor.setSociety(...);  ❌ removed intentionally

       // ✅ 3. Update relations (same society only)
       if (dto.getBuildingId() != null) {
           visitor.setBuilding( buildingRepo.findByIdAndSociety_Id(
                        dto.getBuildingId(), societyId).orElseThrow(() ->new ResourceNotFoundException("Building", "id", dto.getBuildingId())) );
       }

       if (dto.getFloorId() != null) {
        visitor.setFloor(floorRepository.findByIdAndSociety_Id(
                        dto.getFloorId(), societyId).orElseThrow(() -> new ResourceNotFoundException("Floor", "id", dto.getFloorId())));
       }

       if (dto.getFlatId() != null) {
           visitor.setFlat( flatRepository.findByIdAndSociety_Id(
                        dto.getFlatId(), societyId ).orElseThrow(() -> new ResourceNotFoundException("Flat", "id", dto.getFlatId())));
       }

       return mapToDto(visitorRepository.save(visitor));
   }





// 6. DELETE VISITORS
@Override
@Transactional
public void deactivateVisitor(Integer visitorId, Integer societyId) {

    Visitor visitor = visitorRepository
            .findByIdAndSocietyId(visitorId, societyId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Visitor", "id", visitorId
            ));

    // 🔥 PERMANENT DELETE
    visitorRepository.delete(visitor);
}




    /*====================================== HELPER METHOD ======================================*/
    // Mapper helper
    private VisitorDto mapToDto(Visitor visitor) {
        VisitorDto dto = new VisitorDto();

        dto.setId(visitor.getId());
        dto.setName(visitor.getName());
        dto.setImageURL(visitor.getImageURL());
        dto.setMobileNumber(visitor.getMobileNumber());
        dto.setVisitorPurpose(visitor.getVisitorPurpose());
        dto.setVisitorStatus(visitor.getVisitorStatus());
        dto.setVisitorType(visitor.getVisitorType());
        dto.setVehicleNumber(visitor.getVehicleNumber());
        dto.setCreatedAt(visitor.getCreatedAt());
        dto.setInTime(visitor.getInTime());   // ✅ ye add karo
        dto.setOutTime(visitor.getOutTime());

        // ================= IDs =================
        if (visitor.getSociety() != null)
            dto.setSocietyId(visitor.getSociety().getId());

        if (visitor.getBuilding() != null)
            dto.setBuildingId(visitor.getBuilding().getId());

        if (visitor.getFloor() != null)
            dto.setFloorId(visitor.getFloor().getId());

        if (visitor.getFlat() != null)
            dto.setFlatId(visitor.getFlat().getId());

        // ================= EXTRA DISPLAY DATA =================
        if (visitor.getBuilding() != null)
            dto.setBuildingName(visitor.getBuilding().getName());

        if (visitor.getFloor() != null)
            dto.setFloorNumber(visitor.getFloor().getFloorNumber());

        if (visitor.getFlat() != null)
            dto.setFlatNumber(visitor.getFlat().getFlatNumber());

        if (visitor.getCompany() != null) {
            dto.setCompanyId(visitor.getCompany().getId());
            dto.setCompanyName(visitor.getCompany().getName());
            dto.setCompanyLogo(visitor.getCompany().getLogoUrl());
        }

        return dto;
    }



    // SEARCH VISITOR
    @Override
    public List<VisitorDto> searchVisitors(Integer societyId, String keyword) {
        return visitorRepository
                .searchVisitors(societyId, keyword)
                .stream()
                .map(visitor -> {
                    // 1️⃣ Basic mapping via ModelMapper
                    VisitorDto dto = modelMapper.map(visitor, VisitorDto.class);

                    // 2️⃣ Extra fields (relations → readable data)
                    if (visitor.getFlat() != null) {
                        Flat flat = visitor.getFlat();
                        dto.setFlatNumber(flat.getFlatNumber());

                        if (flat.getFloor() != null) {
                            Floor floor = flat.getFloor();
                            dto.setFloorNumber(floor.getFloorNumber());

                            if (floor.getBuilding() != null) {
                                Building building = floor.getBuilding();
                                dto.setBuildingName(building.getName());
                            }
                        }
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }



// =================================== IMAGE ===========================================
    @Override
    public VisitorDto updateVisitorLogo(Integer visitorId, MultipartFile image) {

        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));

        // MultipartFile -> String (path / URL)
        String imageUrl = saveFile(image);

        // ✅ CORRECT setter
        visitor.setImageURL(imageUrl);

        visitorRepository.save(visitor);
        return modelMapper.map(visitor, VisitorDto.class);
    }

    private String saveFile(MultipartFile file) {

        try {
            String uploadDir = "uploads/profile-images/";
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            Files.write(filePath, file.getBytes());

            // ✅ ONLY FILE NAME SAVE
            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Image save failed");
        }
    }


// COUNT VISITORS BY TODAY DATE SOCIETY WISE
    @Override
    public long getTodayVisitorCount(Long societyId) {

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return visitorRepository.countTodayVisitors(
                societyId,
                startOfDay,
                endOfDay
        );
    }

// COUNT VISITORS BY TODAY DATE FLAT WISE
    // 🔹 helper methods
    private LocalDateTime startOfToday() {
        return LocalDate.now(ZoneId.of("Asia/Kolkata")).atStartOfDay();
    }

    private LocalDateTime endOfToday() {
        return startOfToday().plusDays(1);
    }
    @Override
    public long getTodayVisitorCountByFlat(Long societyId, Long flatId) {
        return visitorRepository.countTodayVisitorsByFlat(
                societyId,
                flatId,
                startOfToday(),
                endOfToday()
        );
    }




}
