package com.example.demo.Controllers;

import com.example.demo.Enums.StaffLevel;
import com.example.demo.Enums.StaffStatus;
import com.example.demo.Payloads.StaffDto;
import com.example.demo.Payloads.UserDto;
import com.example.demo.Services.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired private StaffService staffService;


// GET STAFF by ID
    @GetMapping("/{staffId}")
    @PreAuthorize("hasAnyRole('ADMIN','SECURITY')")
    public ResponseEntity<StaffDto> getStaff(@PathVariable Integer staffId) {
        return ResponseEntity.ok(staffService.getStaffById(staffId));
    }


// GET ALL STAFF
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECURITY')")
    public ResponseEntity<List<StaffDto>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }



// GET STAFF BY LEVEL
    @GetMapping("/level/{level}")
    @PreAuthorize("hasAnyRole('ADMIN','SECURITY')")
    public ResponseEntity<List<StaffDto>> getStaffByLevel(@PathVariable("level") StaffLevel level) {
        return ResponseEntity.ok(staffService.getStaffByLevel(level));
    }


// UPDATE STAFF
    @PutMapping("/{staffId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffDto> updateStaff(
            @PathVariable Integer staffId,
            @RequestBody StaffDto dto) {
        return ResponseEntity.ok(staffService.updateStaff(staffId, dto));
    }


// DELETE STAFF
    @DeleteMapping("/{staffId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteStaff(@PathVariable Integer staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.ok("Staff deleted successfully");
    }
}
