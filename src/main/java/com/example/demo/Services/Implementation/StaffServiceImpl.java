package com.example.demo.Services.Implementation;

import com.example.demo.Entities.Staff;
import com.example.demo.Enums.StaffLevel;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.StaffDto;
import com.example.demo.Repositories.*;
import com.example.demo.Services.StaffService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired private StaffRepository staffRepository;
    @Autowired private FlatRepository flatRepo;
    @Autowired private BuildingRepo buildingRepo;
    @Autowired private SocietyRepo societyRepo;
    @Autowired private UserRepository userRepository;
    @Autowired private ModelMapper mapper;



// GET STAFF BY ID
    @Override
    public StaffDto getStaffById(Integer staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", staffId));
        return mapper.map(staff, StaffDto.class);
    }


// GET ALL STAFF
    @Override
    public List<StaffDto> getAllStaff() {
        return staffRepository.findAll()
                .stream()
                .map(staff -> mapper.map(staff, StaffDto.class))
                .collect(Collectors.toList());
    }

// GET STAFF BY LEVEL
    @Override
    public List<StaffDto> getStaffByLevel(StaffLevel staffLevel) {
        return staffRepository.findByStaffLevel(staffLevel)
                .stream()
                .map(staff -> mapper.map(staff, StaffDto.class))
                .collect(Collectors.toList());
    }




// UPDATE STAFF
    @Override
    public StaffDto updateStaff(Integer staffId, StaffDto dto) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", staffId));

        if (dto.getDutyTiming() != null) staff.setDutyTiming(dto.getDutyTiming());
        if (dto.getSalary() != 0) staff.setSalary(dto.getSalary());

        return mapper.map(staffRepository.save(staff), StaffDto.class);
    }


// DELETE STAFF
   @Override
   public void deleteStaff(Integer staffId) {
       Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new ResourceNotFoundException("Staff", "id", staffId));
   }

}
