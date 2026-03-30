package com.example.demo.Services.Implementation;

import com.example.demo.Entities.Complaint;
import com.example.demo.Entities.Notification;
import com.example.demo.Enums.ComplaintStatus;
import com.example.demo.Enums.NotificationType;
import com.example.demo.Enums.TargetAudience;
import com.example.demo.Payloads.ComplaintDto;
import com.example.demo.Repositories.*;
import com.example.demo.Services.ComplaintService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SocietyAdminRepository societyAdminRepository;

    @Autowired
    private SocietyRepo societyRepo;

    @Autowired
    private ModelMapper modelMapper;


// CREATE COMPLAINT, SAVE COMPLAINT, AND AUTO CREATE NOTIFICATION
@Override
public ComplaintDto createComplaint(ComplaintDto dto) {

    // ================== VALIDATIONS ==================
    if (dto.getTitle() == null || dto.getTitle().isBlank())
        throw new IllegalArgumentException("Title is required");

    if (dto.getDescription() == null || dto.getDescription().isBlank())
        throw new IllegalArgumentException("Description is required");

    if (dto.getPriority() == null)
        throw new IllegalArgumentException("Priority is required");

    if (dto.getComplainType() == null)
        throw new IllegalArgumentException("Complaint type is required");

    if (dto.getCreatedByRole() == null)
        throw new IllegalArgumentException("CreatedByRole is required");

    if (dto.getCreatedById() == null)
        throw new IllegalArgumentException("CreatedById is required");

    if (dto.getSocietyId() == null)
        throw new IllegalArgumentException("SocietyId is required");


    // ================== CREATE COMPLAINT ==================
    Complaint complaint = modelMapper.map(dto, Complaint.class);

    complaint.setStatus(ComplaintStatus.PENDING);
    complaint.setCreatedAt(LocalDateTime.now());
    complaint.setUpdatedAt(LocalDateTime.now());
    complaint.setPriority(dto.getPriority());
    complaint.setComplainType(dto.getComplainType());
    complaint.setCreatedById(dto.getCreatedById());
    complaint.setCreatedByRole(dto.getCreatedByRole());
    complaint.setSocietyId(dto.getSocietyId());

    Complaint savedComplaint = complaintRepository.save(complaint);


    // ================== AUTO CREATE NOTIFICATION ==================
    Notification notification = new Notification();

    notification.setTitle("New Complaint Raised");
    notification.setMessage(savedComplaint.getTitle());
    notification.setType(NotificationType.COMPLAINT);
    notification.setReferenceId(savedComplaint.getId()); // complaintId
    notification.setRead(false);
    notification.setCreatedAt(LocalDateTime.now());


    // ================== ROLE BASED TARGET ==================
    TargetAudience createdRole =
            TargetAudience.valueOf(dto.getCreatedByRole());
    if (createdRole == TargetAudience.SOCIETY_ADMIN) {
        // 🔴 Complaint raised by SocietyAdmin → Notify SuperAdmin
        notification.setTargetRole(TargetAudience.SUPER_ADMIN.name());
        notification.setTargetSocietyId(null);
    } else {
        // 🔵 Complaint raised by Staff / Owner / Tenant → Notify SocietyAdmin
        notification.setTargetRole(TargetAudience.SOCIETY_ADMIN.name());
        notification.setTargetSocietyId(savedComplaint.getSocietyId());
    }


    // ================== SAVE NOTIFICATION ==================
    notificationRepository.save(notification);


    // ================== RETURN DTO ==================
    return modelMapper.map(savedComplaint, ComplaintDto.class);
}





    // GET COMPLAINT
    @Override
    public List<ComplaintDto> getComplaints(
            Integer societyId,
            Integer userId,
            String role
    ) {

        List<Complaint> complaints;

        // 🔥 SUPER ADMIN → sirf SocietyAdmin ke complaints
        if (TargetAudience.SUPER_ADMIN.name().equals(role)) {

            complaints = complaintRepository
                    .findByCreatedByRoleOrderByCreatedAtDesc(
                            TargetAudience.SOCIETY_ADMIN.name()
                    );
        }

        // 🔥 SOCIETY ADMIN → apni society ke saare complaints
        else if (TargetAudience.SOCIETY_ADMIN.name().equals(role)) {

            complaints = complaintRepository
                    .findBySocietyIdOrderByCreatedAtDesc(societyId);
        }

        // 🔥 NORMAL USER (OWNER / TENANT)
        else {

            complaints = complaintRepository
                    .findByCreatedByIdAndCreatedByRoleOrderByCreatedAtDesc(
                            userId,
                            role
                    );
        }

        // 🔥 Map + enrich
        return complaints.stream().map(c -> {

            ComplaintDto dto = modelMapper.map(c, ComplaintDto.class);

            // Society name
            if (c.getSocietyId() != null) {
                societyRepo.findById(c.getSocietyId())
                        .ifPresent(s -> dto.setSocietyName(s.getName()));
            }

            // Created by name
            if (c.getCreatedById() != null && c.getCreatedByRole() != null) {

                if (TargetAudience.SOCIETY_ADMIN.name().equals(c.getCreatedByRole())) {

                    societyAdminRepository.findById(c.getCreatedById())
                            .ifPresent(a -> dto.setCreatedByName(a.getAdminName()));
                } else {

                    userRepository.findById(c.getCreatedById())
                            .ifPresent(u -> dto.setCreatedByName(u.getName()));
                }
            }

            return dto;

        }).collect(Collectors.toList());
    }

// UPDATE COMPLAINT STATUS
// ================= UPDATE COMPLAINT STATUS =================
@Override
public ComplaintDto updateComplaintStatus(
        Integer complaintId,
        ComplaintStatus status,
        String role
) {

    // 🔹 Only SUPER_ADMIN or SOCIETY_ADMIN allowed
    if (!TargetAudience.SUPER_ADMIN.name().equals(role)
            && !TargetAudience.SOCIETY_ADMIN.name().equals(role)) {
        throw new RuntimeException("Not authorized to update complaint status");
    }

    // 🔹 Fetch complaint
    Complaint complaint = complaintRepository.findById(complaintId)
            .orElseThrow(() -> new RuntimeException("Complaint not found"));

    // 🔹 Update status
    complaint.setStatus(status);
    complaint.setUpdatedAt(LocalDateTime.now());

    Complaint updatedComplaint = complaintRepository.save(complaint);

    // 🔹 Create notification when resolved
    if (status == ComplaintStatus.RESOLVED) {

        Notification notification = new Notification();
        notification.setTitle("Complaint Resolved");
        notification.setMessage(updatedComplaint.getTitle());
        notification.setType(NotificationType.COMPLAINT);
        notification.setReferenceId(updatedComplaint.getId());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        // Notify complaint creator
        notification.setTargetRole(updatedComplaint.getCreatedByRole());
        notification.setTargetSocietyId(updatedComplaint.getSocietyId());

        notificationRepository.save(notification);
    }

    return modelMapper.map(updatedComplaint, ComplaintDto.class);
}


// UPDATE COMPLAINT
    @Override
    public ComplaintDto updateComplaint(
            Integer complaintId,
            ComplaintDto dto,
            Integer userId,
            String role
    ) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        // 🔐 Only creator (NORMAL_USER) can update
        if (!complaint.getCreatedById().equals(userId)) {
            throw new RuntimeException("Not authorized to update this complaint");
        }

        // 🔒 Resolved complaint cannot be edited
        if (complaint.getStatus() == ComplaintStatus.RESOLVED) {
            throw new RuntimeException("Resolved complaint cannot be edited");
        }

        // ✅ Update allowed fields
        complaint.setTitle(dto.getTitle());
        complaint.setDescription(dto.getDescription());
        complaint.setPriority(dto.getPriority());
        complaint.setComplainType(dto.getComplainType());
        complaint.setUpdatedAt(LocalDateTime.now());

        Complaint updated = complaintRepository.save(complaint);

        return modelMapper.map(updated, ComplaintDto.class);
    }



// DELETE COMPLAINT

    @Override
    public boolean deleteComplaint(Integer complaintId, Integer userId, String role) {

        // 🔹 Fetch complaint from DB
        Complaint complaint = complaintRepository.findById(complaintId).orElse(null);

        if (complaint == null) {
            return false; // complaint not found
        }

        // 🔹 Only creator or SUPER_ADMIN can delete
        if (complaint.getCreatedById().equals(userId) || "SUPER_ADMIN".equals(role)) {

            // 🔹 Delete associated notifications
            List<Notification> notifications = notificationRepository
                    .findByReferenceIdAndType(complaintId, NotificationType.COMPLAINT);

            if (!notifications.isEmpty()) {
                notificationRepository.deleteAll(notifications);
            }

            // 🔹 Delete complaint
            complaintRepository.delete(complaint);

            return true;
        }

        // 🔹 Not authorized
        return false;
    }




}
