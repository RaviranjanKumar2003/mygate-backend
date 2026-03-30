package com.example.demo.Services;

import com.example.demo.Enums.StaffLevel;
import com.example.demo.Enums.StaffStatus;
import com.example.demo.Payloads.StaffDto;
import com.example.demo.Payloads.UserDto;

import java.util.List;

public interface StaffService {


// GET BY ID
    StaffDto getStaffById(Integer staffId);


// GET ALL
    List<StaffDto> getAllStaff();


//  GET STAFF BY LEVEL (FLAT / BUILDING / SOCIETY)
    List<StaffDto> getStaffByLevel(StaffLevel staffLevel);


    // UPDATE
    StaffDto updateStaff(Integer staffId, StaffDto dto);

    // DELETE
    void deleteStaff(Integer staffId);
}
