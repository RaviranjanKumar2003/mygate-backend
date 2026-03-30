package com.example.demo.Controllers;

import com.example.demo.Configuration.CustomUserDetails;
import com.example.demo.Enums.ComplaintStatus;
import com.example.demo.Payloads.ComplaintDto;
import com.example.demo.Services.ComplaintService;
import com.example.demo.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    @Autowired
    private NotificationService notificationService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // ================= CREATE COMPLAINT =================
    @PostMapping
    public ResponseEntity<ComplaintDto> createComplaint(@RequestBody ComplaintDto dto) {

        CustomUserDetails user =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        dto.setCreatedById(user.getId());
        dto.setCreatedByRole(user.getRole());
        dto.setSocietyId(user.getSocietyId());

        ComplaintDto saved = complaintService.createComplaint(dto);

        return ResponseEntity.ok(saved);
    }

    // ================= GET COMPLAINTS =================
    @GetMapping
    public ResponseEntity<List<ComplaintDto>> getComplaints() {

        CustomUserDetails user =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return ResponseEntity.ok(
                complaintService.getComplaints(
                        user.getSocietyId(),
                        user.getId(),
                        user.getRole()
                )
        );
    }

// UPDATE COMPLAINT STATUS
@PutMapping("/{id}/status")
public ResponseEntity<ComplaintDto> updateComplaintStatus(
        @PathVariable Integer id,
        @RequestParam ComplaintStatus status
) {
    // get currently authenticated user
    CustomUserDetails user =
            (CustomUserDetails) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();

    ComplaintDto updated = complaintService.updateComplaintStatus(
            id,
            status,
            user.getRole()
    );

    return ResponseEntity.ok(updated);
}


// UPDATE COMPLAINT
    @PutMapping("/{id}")
    public ResponseEntity<ComplaintDto> updateComplaint(
            @PathVariable Integer id,
            @RequestBody ComplaintDto dto,
            Authentication authentication
    ) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        ComplaintDto updated = complaintService.updateComplaint(
                id,
                dto,
                user.getId(),
                user.getRole()
        );

        return ResponseEntity.ok(updated);
    }





    // ================= DELETE COMPLAINT =================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaint(@PathVariable Integer id) {

        CustomUserDetails user =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        boolean deleted = complaintService.deleteComplaint(
                id,
                user.getId(),
                user.getRole()
        );

        if (deleted) {
            return ResponseEntity.ok("Complaint deleted successfully");
        }
        return ResponseEntity.status(403)
                .body("You are not authorized to delete this complaint");
    }
}