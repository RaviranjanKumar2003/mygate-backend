package com.example.demo.SocietyAdmin;

import com.example.demo.Payloads.StaffDto;
import com.example.demo.Payloads.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }



//================================== USER =============================

//  Get all pending users
    @GetMapping("/pending-users")
    public ResponseEntity<List<UserDto>> getPendingUsers() {

        return ResponseEntity.ok(adminService.getPendingUsers());
    }

//  Approve user (PENDING → PENDING)
    @PutMapping("/approve/{userId}")
    public ResponseEntity<UserDto> approveUser(
            @PathVariable Integer userId,
            @RequestBody UserDto dto
    ) {
        return ResponseEntity.ok(
                adminService.approveUser(userId, dto.getUserRole())
        );
    }

//  Deactivate user
    @PutMapping("/deactivate/{userId}")
    public ResponseEntity<UserDto> deactivateUser(
            @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(adminService.deactivateUser(userId));
    }



// =========================== STAFF ==========================


}
