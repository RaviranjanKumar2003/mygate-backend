package com.example.demo.Services.Implementation;

import com.example.demo.Entities.Society;
import com.example.demo.Entities.SocietyAdmin;
import com.example.demo.Enums.SocietyStatus;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.SocietyAdminDto;
import com.example.demo.Payloads.SocietyDto;
import com.example.demo.Repositories.SocietyAdminRepository;
import com.example.demo.Repositories.SocietyRepo;
import com.example.demo.Services.SocietyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocietyServiceImpl implements SocietyService {

    @Autowired
    private SocietyRepo societyRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SocietyAdminRepository societyAdminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SocietyAdminDto societyAdminDto;


// CREATE SOCIETY + SOCIETY ADMIN
@Override
public SocietyDto createSociety(SocietyDto societyDto) {

    // Map Society DTO to Entity
    Society society = new Society();
    society.setName(societyDto.getName());
    society.setAddress(societyDto.getAddress());
    society.setCity(societyDto.getCity());
    society.setIsActive(societyDto.getIsActive() != null ? societyDto.getIsActive() : SocietyStatus.ACTIVE);

    // Save Society first
    Society savedSociety = societyRepo.save(society);

    SocietyAdminDto adminResponse = null;

    // Create Society Admin if provided
    SocietyAdminDto adminDto = societyDto.getSocietyAdmin();
    if (adminDto != null
            && adminDto.getAdminEmail() != null
            && !adminDto.getAdminEmail().isEmpty()
            && adminDto.getAdminPassword() != null
            && !adminDto.getAdminPassword().isEmpty()) {

        if (societyAdminRepository.existsByAdminEmail(adminDto.getAdminEmail())) {
            throw new RuntimeException("Admin with this email already exists");
        }

        SocietyAdmin admin = new SocietyAdmin();
        admin.setAdminName(adminDto.getAdminName());
        admin.setAdminEmail(adminDto.getAdminEmail());
        admin.setAdminPassword(passwordEncoder.encode(adminDto.getAdminPassword()));
        admin.setMobileNumber(adminDto.getMobileNumber());
        admin.setSociety(savedSociety);

        SocietyAdmin savedAdmin = societyAdminRepository.save(admin);

        // Map admin to DTO for response
        adminResponse = new SocietyAdminDto();
        adminResponse.setAdminName(savedAdmin.getAdminName());
        adminResponse.setAdminEmail(savedAdmin.getAdminEmail());
        adminResponse.setMobileNumber(savedAdmin.getMobileNumber());
        adminResponse.setSocietyId(savedSociety.getId());
    }

    // Map society to DTO
    SocietyDto responseDto = new SocietyDto();
    responseDto.setId(savedSociety.getId());
    responseDto.setName(savedSociety.getName());
    responseDto.setAddress(savedSociety.getAddress());
    responseDto.setCity(savedSociety.getCity());
    responseDto.setIsActive(savedSociety.getIsActive());
    responseDto.setSocietyAdmin(adminResponse);

    return responseDto;
}




// GET ALL Active SOCIETIES
    @Override
    public List<SocietyDto> getAllActiveSocieties() {

        return societyRepo.findByIsActive(SocietyStatus.ACTIVE)
                .stream()
                .map(society -> {
                    SocietyDto dto = modelMapper.map(society, SocietyDto.class);

                    if (society.getSocietyAdmin() != null) {
                        dto.setSocietyAdmin(
                                modelMapper.map(
                                        society.getSocietyAdmin(),
                                        SocietyAdminDto.class
                                )
                        );
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }




// GET ALL IN

    @Override
    public List<SocietyDto> getAllDeActiveSocieties() {

        return societyRepo.findByIsActive(SocietyStatus.INACTIVE)
                .stream()
                .map(s -> modelMapper.map(s, SocietyDto.class))
                .collect(Collectors.toList());
    }


// GET SOCIETY BY ID
@Override
public SocietyDto getSocietyById(Integer societyId) {

    Society society = societyRepo.findByIdAndIsActive(
            societyId, SocietyStatus.ACTIVE
    ).orElseThrow(() ->
            new ResourceNotFoundException("Society", "Society Id", societyId)
    );

    SocietyDto dto = modelMapper.map(society, SocietyDto.class);

    // ✅ MAP SOCIETY ADMIN
    if (society.getSocietyAdmin() != null) {
        SocietyAdminDto adminDto =
                modelMapper.map(
                        society.getSocietyAdmin(),
                        SocietyAdminDto.class
                );
        dto.setSocietyAdmin(adminDto);
    }

    return dto;
}





// UPDATE SOCIETY
   //@Override
   //public SocietyDto updateSociety(SocietyDto dto, Integer societyId) {

       //Society society = societyRepo.findByIdAndIsActive(societyId, SocietyStatus.PENDING)
               //.orElseThrow(() ->new ResourceNotFoundException("Society","Society Id",societyId));

       //modelMapper.map(dto, society);

       //Society updated = societyRepo.save(society);
       //return modelMapper.map(updated, SocietyDto.class);
  // }

    @Override
    public SocietyDto updateSociety(SocietyDto dto, Integer societyId) {

        Society society = societyRepo.findByIdAndIsActive(societyId, SocietyStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Society","Society Id",societyId));

        // ❌ modelMapper.map(dto, society);  // REMOVE

        // ✅ Manual field update
        society.setName(dto.getName());
        society.setAddress(dto.getAddress());
        society.setCity(dto.getCity());
        // aur jitne bhi fields update karne hain

        Society updated = societyRepo.save(society);
        return modelMapper.map(updated, SocietyDto.class);
    }



    // DELETE SOCIETY
   @Override
   public void deleteSociety(Integer societyId) {

       Society society = societyRepo
               .findByIdAndIsActive(societyId, SocietyStatus.ACTIVE)
               .orElseThrow(() ->new ResourceNotFoundException("Society","Society Id",societyId));

        //society.setIsActive(SocietyStatus.IN);
       //societyRepo.save(society);
       societyRepo.delete(society);
   }




// SEARCH SOCIETIES
    @Override
    public List<SocietyDto> searchSociety(String keyword) {

        return societyRepo
                .findByNameContainingIgnoreCaseOrCityContainingIgnoreCaseAndIsActive(
                        keyword, keyword, SocietyStatus.ACTIVE)
                .stream()
                .map(s -> modelMapper.map(s, SocietyDto.class))
                .collect(Collectors.toList());
    }

}
