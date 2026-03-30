package com.example.demo.Controllers;

import com.example.demo.Entities.SocietyAdmin;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.SocietyAdminDto;
import com.example.demo.Payloads.SocietyDto;
import com.example.demo.Repositories.SocietyAdminRepository;
import com.example.demo.Services.FileService;
import com.example.demo.Services.SocietyService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/societies")
public class SocietyController {

    @Autowired
    private SocietyService societyService;

    @Autowired
    private SocietyAdminRepository societyAdminRepository;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

// CREATE SOCIETY
    @PostMapping
    public ResponseEntity<SocietyDto> createSociety(
            @RequestBody SocietyDto dto
    ) {
        SocietyDto created = societyService.createSociety(dto);
        return ResponseEntity.ok(created);
    }


// GET ALL PENDING SOCIETY
    @GetMapping
    public ResponseEntity<List<SocietyDto>> getAllActiveSocieties() {
        return ResponseEntity.ok(societyService.getAllActiveSocieties());
    }


// GET ALL IN SOCIETY
    @GetMapping("/inactive")
    public ResponseEntity<List<SocietyDto>> getAllDeActiveSocieties() {
        return ResponseEntity.ok(societyService.getAllDeActiveSocieties());
    }


// GET SOCIETY BY ID
    @GetMapping("/{societyId}")
    public ResponseEntity<SocietyDto> getSocietyById(
            @PathVariable Integer societyId
    ) {
        return ResponseEntity.ok(societyService.getSocietyById(societyId));
    }


// UPDATE SOCIETY
    @PutMapping("/{societyId}")
    public ResponseEntity<SocietyDto> updateSociety(
            @RequestBody SocietyDto dto,
            @PathVariable Integer societyId
    ) {
        return ResponseEntity.ok(societyService.updateSociety(dto, societyId));
    }



// DELETE (SOFT)
    @DeleteMapping("/{societyId}")
    public ResponseEntity<String> deleteSociety(
            @PathVariable Integer societyId
    ) {
        societyService.deleteSociety(societyId);
        return ResponseEntity.ok("Society soft deleted successfully");
    }



// SEARCH
    @GetMapping("/search")
    public ResponseEntity<List<SocietyDto>> searchSociety(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(societyService.searchSociety(keyword));
    }

}
