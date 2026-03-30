package com.example.demo.Services;

import com.example.demo.Enums.CompanyType;
import com.example.demo.Payloads.CompanyDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyService {

    CompanyDto addCompany(CompanyDto companyDto);

    List<CompanyDto> getCompaniesByType(CompanyType companyType);

    CompanyDto updateCompanyLogo(Integer companyId, MultipartFile image);

}
