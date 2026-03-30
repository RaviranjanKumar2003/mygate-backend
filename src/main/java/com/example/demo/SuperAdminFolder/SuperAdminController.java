package com.example.demo.SuperAdminFolder;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Services.FileService;
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

@RestController
@RequestMapping("/api/super-admins")
public class SuperAdminController {

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private SuperAdminService superAdminService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

// CREATE SUPER ADMIN
    @PostMapping
    public ResponseEntity<SuperAdminDto> create(@RequestBody SuperAdminDto dto) {
        return ResponseEntity.ok(superAdminService.createSuperAdmin(dto));
    }


// GET SUPER ADMIN BY ID
    @GetMapping("/{superAdminId}")
    public ResponseEntity<SuperAdminDto> getById(@PathVariable int superAdminId) {
        return ResponseEntity.ok(superAdminService.getSuperAdminById(superAdminId));
    }


// GET ALL SUPER ADMINS
    @GetMapping
    public ResponseEntity<List<SuperAdminDto>> getAll() {
        return ResponseEntity.ok(superAdminService.getAllSuperAdmins());
    }


// UPDATE SUPER ADMIN
    @PutMapping("/{superAdminId}")
    public ResponseEntity<SuperAdminDto> update(
            @PathVariable int superAdminId,
            @RequestBody SuperAdminDto dto
    ) {
        return ResponseEntity.ok(superAdminService.updateSuperAdmin(superAdminId, dto));
    }


// DELETE SUPER ADMIN
    @DeleteMapping("/{superAdminId}")
    public ResponseEntity<String> deactivate(@PathVariable int superAdminId) {
        superAdminService.deactivateSuperAdmin(superAdminId);
        return ResponseEntity.ok("Super Admin deactivated successfully");
    }



// UPLOAD / UPDATE SUPER ADMIN IMAGE
    @PostMapping("/image/upload/super-admin/{superAdminId}")
    public ResponseEntity<SuperAdminDto> uploadSuperAdminImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer superAdminId
    ) throws IOException {

        SuperAdmin superAdmin = superAdminRepository.findById(superAdminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("SuperAdmin", "id", superAdminId));

        //  Delete old image (if exists)
        if (superAdmin.getImageURL() != null) {
            fileService.deleteImage(path, superAdmin.getImageURL());
        }

        //  Upload new image
        String fileName = fileService.uploadImage(path, image);

        //  Update DB
        SuperAdminDto updated = superAdminService.updateSuperAdminImage(superAdminId, fileName);

        return ResponseEntity.ok(updated);
    }


// GET IMAGE
    @GetMapping("/image/get/super-admin/{superAdminId}")
    public void downloadSuperAdminImage(
            @PathVariable Integer superAdminId,
            HttpServletResponse response
    ) throws IOException {

        SuperAdmin superAdmin = superAdminRepository.findById(superAdminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("SuperAdmin", "id", superAdminId));

        String imageName = superAdmin.getImageURL();

        InputStream resource = fileService.getResource(path, imageName);

        String contentType = URLConnection.guessContentTypeFromName(imageName);
        response.setContentType(
                contentType != null ? contentType : "application/octet-stream"
        );

        StreamUtils.copy(resource, response.getOutputStream());
    }




}
