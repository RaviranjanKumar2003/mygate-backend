package com.example.demo.Controllers;

import com.example.demo.Entities.Company;
import com.example.demo.Enums.CompanyType;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.CompanyDto;
import com.example.demo.Repositories.CompanyRepository;
import com.example.demo.Services.CompanyService;
import com.example.demo.Services.FileService;
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
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/api/companies")
@CrossOrigin("*")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;

    private final String imageUploadPath = System.getProperty("user.dir") + "/images/company";

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }


// Add Company
    @PostMapping
    public ResponseEntity<CompanyDto> addCompany(@RequestBody CompanyDto companyDto) {
        return ResponseEntity.ok(companyService.addCompany(companyDto));
    }


//  GET COMPANIES BY TYPE (MANDATORY)
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CompanyDto>> getCompaniesByType(
            @PathVariable CompanyType type
    ) {
        return ResponseEntity.ok(
                companyService.getCompaniesByType(type)
        );
    }





    // Company Logo IMAGE UPLOAD

    @PostMapping("/image/upload/{companyId}")
    public ResponseEntity<CompanyDto> uploadUserImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer companyId
    ) throws IOException {

        Company company = companyRepository.findById(companyId).orElseThrow(() ->
                new ResourceNotFoundException("Company", "id", companyId));

        // delete old image if exists
        if (company.getLogoUrl() != null && !company.getLogoUrl().isEmpty()) {
            fileService.deleteImage(imageUploadPath, company.getLogoUrl());
        }

        // upload new image
        String fileName = fileService.uploadImage(imageUploadPath, image);
        company.setLogoUrl(fileName);

        companyRepository.save(company);

        return ResponseEntity.ok(modelMapper.map(company, CompanyDto.class));
    }



    // GET USERS IMAGE
    @GetMapping("/image/get/company/{companyId}")
    public void downloadUserImage(
            @PathVariable Integer companyId,
            HttpServletResponse response
    ) throws IOException {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

        if (company.getLogoUrl() == null || company.getLogoUrl().isEmpty()) {
            throw new ResourceNotFoundException("Image not found for company", "companyId", companyId);
        }

        InputStream resource = fileService.getResource(imageUploadPath, company.getLogoUrl());

        String contentType = URLConnection.guessContentTypeFromName(company.getLogoUrl());
        response.setContentType(contentType != null ? contentType : "application/octet-stream");

        StreamUtils.copy(resource, response.getOutputStream());
    }




}
