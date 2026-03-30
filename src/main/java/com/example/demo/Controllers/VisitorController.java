package com.example.demo.Controllers;

import com.example.demo.Entities.Visitor;
import com.example.demo.Enums.VisitorStatus;
import com.example.demo.Enums.VisitorType;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.ApiResponse;
import com.example.demo.Payloads.VisitorDto;
import com.example.demo.Repositories.VisitorRepository;
import com.example.demo.Services.FileService;
import com.example.demo.Services.VisitorService;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;

    private final String imageUploadPath = System.getProperty("user.dir") + "/images/visitor";


// CREATE VISITOR
   @PostMapping("/society/{societyId}/building/{buildingId}/floor/{floorId}/flat/{flatId}")
   public ResponseEntity<VisitorDto> createVisitor(
           @PathVariable Integer societyId,
           @PathVariable Integer buildingId,
           @PathVariable Integer floorId,
           @PathVariable Integer flatId,
           @RequestBody VisitorDto visitorDto) {

       visitorDto.setSocietyId(societyId);
       visitorDto.setBuildingId(buildingId);
       visitorDto.setFloorId(floorId);
       visitorDto.setFlatId(flatId);

       return new ResponseEntity<>(
               visitorService.createVisitor(visitorDto),
               HttpStatus.CREATED
       );
   }


// 1. GET ALL VISITORS IN A SOCIETY
    @GetMapping("/society/{societyId}")
    public List<VisitorDto> getAllVisitorsOfSociety(
            @PathVariable Integer societyId
    ) {
        return visitorService.getAllVisitors(societyId);
    }


// GET VISITOR FOR FLAT
    @GetMapping("/society/{societyId}/building/{buildingId}/floor/{floorId}/flat/{flatId}/visitors")
    public List<VisitorDto> getVisitors(
            @PathVariable Integer societyId,
            @PathVariable Integer buildingId,
            @PathVariable Integer floorId,
            @PathVariable Integer flatId
    ) {
        return visitorService.getVisitorsForOwnerTenant(
                societyId, buildingId, floorId, flatId
        );
    }



// GET VISITORS FOR FLAT BY STATUS
    @GetMapping("/society/{societyId}/building/{buildingId}/floor/{floorId}/flat/{flatId}/status/{status}")
    public ResponseEntity<List<VisitorDto>> getVisitorsForOwnerTenantByStatus(
            @PathVariable Integer societyId,
            @PathVariable Integer buildingId,
            @PathVariable Integer floorId,
            @PathVariable Integer flatId,
            @PathVariable VisitorStatus status
    ) {
        return ResponseEntity.ok(
                visitorService.getVisitorsForOwnerTenantByStatus(
                        societyId, buildingId, floorId, flatId, status
                )
        );
    }



// UPDATE VISITORS STATUS
   @PutMapping("/society/{societyId}/visitor/{visitorId}/status")
   public ResponseEntity<VisitorDto> updateVisitorStatus(
           @PathVariable Integer societyId,
           @PathVariable Integer visitorId,
          @RequestBody Map<String, String> request
   ) {
       VisitorStatus status = VisitorStatus.valueOf(request.get("visitorStatus"));
       return ResponseEntity.ok(
               visitorService.updateVisitorStatus(societyId, visitorId, status)
       );
   }





// 2. GET VISITORS IN A SOCIETY BY ID
   @GetMapping("/society/{societyId}/visitor/{visitorId}")
   public VisitorDto getVisitorById(
           @PathVariable Integer societyId,
           @PathVariable Integer visitorId
   ) {
       return visitorService.getVisitorById(societyId, visitorId);
   }



// 3. GET VISITORS IN SOCIETY BY STATUS
   @GetMapping("/society/{societyId}/status/{status}")
   public List<VisitorDto> getVisitorsByStatus(
           @PathVariable Integer societyId,
           @PathVariable VisitorStatus status
   ) {
       return visitorService.getVisitorsBySocietyAndStatus(societyId, status);
   }


// GET VISITORS IN SOCIETY BY TYPE
    @GetMapping("/society/{societyId}/visitorType/{visitorType}")
    public ResponseEntity<List<VisitorDto>> getVisitorsBySocietyAndVisitorsType(
            @PathVariable Integer societyId,
            @PathVariable VisitorType visitorType
    ) {
        List<VisitorDto> visitors =
                visitorService.getVisitorsBySocietyAndVisitorsType(societyId, visitorType);
        return ResponseEntity.ok(visitors);
    }



// UPDATE VISITOR
   @PutMapping("/society/{societyId}/visitor/{visitorId}")
   public VisitorDto updateVisitor(
           @PathVariable Integer societyId,
           @PathVariable Integer visitorId,
           @RequestBody VisitorDto dto
   ) {
       return visitorService.updateVisitor(societyId, visitorId, dto);
   }



// DELETE VISITOR
   @DeleteMapping("/society/{societyId}/visitor/{visitorId}")
   public ResponseEntity<ApiResponse> deleteVisitor(
           @PathVariable Integer societyId,
           @PathVariable Integer visitorId
   ) {
       visitorService.deactivateVisitor(visitorId, societyId);
       return ResponseEntity.ok(
               new ApiResponse("Visitor permanently deleted", true)
       );
   }




// SEARCH
    @GetMapping("/society/{societyId}/search")
    public ResponseEntity<List<VisitorDto>> searchVisitors(
            @PathVariable Integer societyId,
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(
                visitorService.searchVisitors(societyId, keyword)
        );
    }



// VISITORS IMAGE UPLOAD

    @PostMapping("/image/upload/{visitorId}")
    public ResponseEntity<VisitorDto> uploadUserImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer visitorId
    ) throws IOException {

        Visitor visitor = visitorRepository.findById(visitorId).orElseThrow(() ->
                new ResourceNotFoundException("Company", "id", visitorId));

        // delete old image if exists
        if (visitor.getImageURL() != null && !visitor.getImageURL().isEmpty()) {
            fileService.deleteImage(imageUploadPath, visitor.getImageURL());
        }

        // upload new image
        String fileName = fileService.uploadImage(imageUploadPath, image);
        visitor.setImageURL(fileName);

        visitorRepository.save(visitor);

        return ResponseEntity.ok(modelMapper.map(visitor, VisitorDto.class));
    }



// GET USERS IMAGE
    @GetMapping("/image/get/visitor/{visitorId}")
    public void downloadUserImage(
            @PathVariable Integer visitorId,
            HttpServletResponse response
    ) throws IOException {

        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new ResourceNotFoundException("Visitor", "id", visitorId));

        if (visitor.getImageURL() == null || visitor.getImageURL().isEmpty()) {
            throw new ResourceNotFoundException("Image not found for company", "visitorId", visitorId);
        }

        InputStream resource = fileService.getResource(imageUploadPath, visitor.getImageURL());

        String contentType = URLConnection.guessContentTypeFromName(visitor.getImageURL());
        response.setContentType(contentType != null ? contentType : "application/octet-stream");

        StreamUtils.copy(resource, response.getOutputStream());
    }



// COUNT VISITORS BY TODAY COUNT WISE SOCIETY
    @GetMapping("/society/{societyId}/count/today")
    public ResponseEntity<Map<String, Long>> getTodayVisitorCount(
            @PathVariable Long societyId
    ) {
        long count = visitorService.getTodayVisitorCount(societyId);

        Map<String, Long> response = new HashMap<>();
        response.put("count", count);

        return ResponseEntity.ok(response);
    }

// COUNT VISITORS BY TODAY DATE WISE FLAT
    @GetMapping("/society/{societyId}/flat/{flatId}/count/today")
    public ResponseEntity<Map<String, Long>> todayVisitorCountByFlat(
            @PathVariable Long societyId,
            @PathVariable Long flatId
    ) {
        long count = visitorService.getTodayVisitorCountByFlat(societyId, flatId);
        return ResponseEntity.ok(Map.of("count", count));
    }

}
