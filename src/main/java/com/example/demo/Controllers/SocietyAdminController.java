package com.example.demo.Controllers;

import com.example.demo.Entities.SocietyAdmin;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.SocietyAdminDto;
import com.example.demo.Repositories.SocietyAdminRepository;
import com.example.demo.Services.FileService;
import com.example.demo.Services.SocietyAdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@RestController
@RequestMapping("/api/society-admins")
public class SocietyAdminController {

    @Autowired
    private SocietyAdminRepository societyAdminRepository;

    @Autowired
    private SocietyAdminService societyAdminService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;



// GET ALL SOCIETY ADMINS
    @GetMapping
    public ResponseEntity<List<SocietyAdminDto>> getAllSocietyAdmins() {
        List<SocietyAdminDto> admins = societyAdminService.getAllSocietyAdmins();
        return ResponseEntity.ok(admins);
    }


// GET BY ID
    @GetMapping("/{adminId}")
    public ResponseEntity<SocietyAdminDto> getSocietyAdminById(
            @PathVariable Integer adminId
    ) {
        return ResponseEntity.ok(
                societyAdminService.getSocietyAdminById(adminId)
        );
    }


// UPDATE SOCIETY ADMIN
    @PutMapping("/{adminId}/update")
    public ResponseEntity<SocietyAdminDto> updateSocietyAdmin(
            @RequestBody SocietyAdminDto dto,
            @PathVariable Integer adminId
    ) {
        SocietyAdminDto updatedAdmin = societyAdminService.updateSocietyAdmin(dto, adminId);
        return ResponseEntity.ok(updatedAdmin);
    }



// UPLOAD IMAGE
    @PostMapping("/image/upload/{adminId}")
    public ResponseEntity<SocietyAdminDto> uploadImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer adminId
    ) throws IOException {

        SocietyAdmin admin = societyAdminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("SocietyAdmin", "id", adminId));

        if (admin.getImageURL() != null) {
            fileService.deleteImage(path, admin.getImageURL());
        }

        String fileName = fileService.uploadImage(path, image);
        admin.setImageURL(fileName);

        societyAdminRepository.save(admin);

        return ResponseEntity.ok(modelMapper.map(admin, SocietyAdminDto.class));
    }

// GET IMAGE
    @GetMapping("/image/get/society-admin/{societyAdminId}")
    public void downloadSocietyAdminImage(
            @PathVariable Integer societyAdminId,
            HttpServletResponse response
    ) throws IOException {

        SocietyAdmin societyAdmin = societyAdminRepository.findById(societyAdminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "SocietyAdmin", "id", societyAdminId
                        )
                );

        String imageName = societyAdmin.getImageURL();
        InputStream resource = fileService.getResource(path, imageName);

        String contentType = URLConnection.guessContentTypeFromName(imageName);
        response.setContentType(
                contentType != null ? contentType : "application/octet-stream"
        );

        StreamUtils.copy(resource, response.getOutputStream());
    }


}
