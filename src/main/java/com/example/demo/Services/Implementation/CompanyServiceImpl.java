package com.example.demo.Services.Implementation;

import com.example.demo.Entities.Company;
import com.example.demo.Enums.CompanyType;
import com.example.demo.Payloads.CompanyDto;
import com.example.demo.Repositories.CompanyRepository;
import com.example.demo.Services.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

// Add Company
    @Override
    public CompanyDto addCompany(CompanyDto companyDto) {
        Company company = new Company();
        company.setName(companyDto.getName());
        company.setCompanyType(companyDto.getCompanyType());
        company.setActive(true);
        company.setLogoUrl(company.getLogoUrl());

        Company saved = companyRepository.save(company);
        return mapToDto(saved);
    }

    @Override
    public List<CompanyDto> getCompaniesByType(CompanyType companyType) {

        return companyRepository
                .findByCompanyTypeAndActiveTrue(companyType)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CompanyDto mapToDto(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setLogoUrl(company.getLogoUrl());
        dto.setCompanyType(company.getCompanyType());
        dto.setActive(company.getActive());
        return dto;
    }


    // ==================== IMAGE ====================
    @Override
    public CompanyDto updateCompanyLogo(Integer userId, MultipartFile image) {

        Company company = companyRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // MultipartFile -> String (path / URL)
        String imageUrl = saveFile(image);

        // ✅ CORRECT setter
        company.setLogoUrl(imageUrl);

        companyRepository.save(company);
        return modelMapper.map(company, CompanyDto.class);
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



}
