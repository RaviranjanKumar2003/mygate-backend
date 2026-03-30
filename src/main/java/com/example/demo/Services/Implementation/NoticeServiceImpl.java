package com.example.demo.Services.Implementation;

import com.example.demo.Entities.Notice;
import com.example.demo.Entities.Notification;
import com.example.demo.Entities.Society;
import com.example.demo.Entities.SocietyAdmin;
import com.example.demo.Enums.NoticeCreatedByRole;
import com.example.demo.Enums.TargetAudience;
import com.example.demo.Enums.NotificationType;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Payloads.NoticeDto;
import com.example.demo.Repositories.NoticeRepository;
import com.example.demo.Repositories.NotificationRepository;
import com.example.demo.Repositories.SocietyAdminRepository;
import com.example.demo.Repositories.SocietyRepo;
import com.example.demo.Services.FileService;
import com.example.demo.Services.NoticeService;
import com.example.demo.Services.NotificationService;
import com.example.demo.SuperAdminFolder.SuperAdmin;
import com.example.demo.SuperAdminFolder.SuperAdminRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private SocietyAdminRepository societyAdminRepository;

    @Autowired
    private FileService fileService;

    private NotificationType notificationType;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SocietyRepo societyRepo;


    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }



// 1. CREATE NOTICE BY SUPER ADMIN & SOCIETY ADMIN
@Override
public NoticeDto createNotice(
        NoticeDto dto,
        Integer userId,
        NoticeCreatedByRole role,
        Integer societyId,
        MultipartFile attachment
) throws IOException {

    // ================= VALIDATIONS =================
    if (dto.getTitle() == null || dto.getTitle().isBlank())
        throw new IllegalArgumentException("Title is required");

    if (dto.getMessage() == null || dto.getMessage().isBlank())
        throw new IllegalArgumentException("Message is required");

    if (dto.getNoticeType() == null)
        throw new IllegalArgumentException("Notice type is required");

    if (dto.getPriority() == null)
        throw new IllegalArgumentException("Priority is required");

    if (dto.getTargetRole() == null)
        dto.setTargetRole(TargetAudience.ALL);

    // ================= CREATE ENTITY =================
    Notice notice = new Notice();
    notice.setTitle(dto.getTitle());
    notice.setMessage(dto.getMessage());
    notice.setNoticeType(dto.getNoticeType());
    notice.setPriority(dto.getPriority());
    notice.setTargetRole(dto.getTargetRole());
    notice.setIsActive(true);
    notice.setCreatedAt(LocalDateTime.now());
    notice.setUpdatedAt(LocalDateTime.now());

    notice.setCreatedById(userId);
    notice.setCreatedByRole(role);

    // ================= ROLE BASED LOGIC =================
    if (role == NoticeCreatedByRole.SUPER_ADMIN) {

        SuperAdmin admin = superAdminRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("SuperAdmin", "id", userId));

        notice.setCreatedByName(admin.getName());

        // SUPER ADMIN → PARTICULAR SOCIETY
        if (societyId != null) {

            Society society = societyRepo.findById(societyId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Society", "id", societyId));

            SocietyAdmin societyAdmin = society.getSocietyAdmin();

            notice.setTargetSocietyId(societyId);
            notice.setRecievedById(societyAdmin.getId());
            notice.setRecievedByRole(NoticeCreatedByRole.SOCIETY_ADMIN);
            notice.setReceivedByName(societyAdmin.getAdminName());

        } else {
            // GLOBAL NOTICE
            notice.setTargetSocietyId(null);
            notice.setRecievedById(0);
            notice.setRecievedByRole(null);
            notice.setReceivedByName("ALL SOCIETIES");
        }

    }
    else if (role == NoticeCreatedByRole.SOCIETY_ADMIN) {

        SocietyAdmin admin = societyAdminRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("SocietyAdmin", "id", userId));

        if (societyId == null)
            throw new IllegalArgumentException("societyId required for Society Admin");

        notice.setCreatedByName(admin.getAdminName());
        notice.setTargetSocietyId(societyId);

        notice.setRecievedById(0);
        notice.setRecievedByRole(null);

        // 🔹 GROUP TARGET NAME
        switch (dto.getTargetRole()) {
            case OWNER:
                notice.setReceivedByName("ALL OWNERS");
                break;
            case STAFF:
                notice.setReceivedByName("ALL STAFF");
                break;
            case RESIDENT:
                notice.setReceivedByName("ALL RESIDENTS");
                break;
            case ALL:
            default:
                notice.setReceivedByName("ALL USERS");
        }
    }

    // ================= ATTACHMENT =================
    if (attachment != null && !attachment.isEmpty()) {
        String fileName = fileService.uploadImage("notice-images", attachment);
        notice.setAttachmentUrl(fileName);
    }

    // ================= SAVE =================
    Notice savedNotice = noticeRepository.save(notice);

    // ================= NOTIFICATION =================
    Notification notification = new Notification();
    notification.setTitle(savedNotice.getTitle());
    notification.setMessage("New notice posted by " + savedNotice.getCreatedByName());
    notification.setType(NotificationType.NOTICE);
    notification.setTargetSocietyId(savedNotice.getTargetSocietyId());
    notification.setNotice(savedNotice);

    notificationRepository.save(notification);

    return mapToDto(savedNotice);
}


// GET NOTICES FOR SOCIETY (global + society-specific)
    @Override
    public List<NoticeDto> getNoticesForSociety(Integer societyId) {
        List<Notice> notices = noticeRepository.findNoticesForSociety(societyId);
        System.out.println("Fetched notices for society " + societyId + ": " + notices.size());
        return notices.stream().map(this::mapToDto).collect(Collectors.toList());
    }


// GET SUPER ADMIN NOTICE BY ITSELF
    @Override
    public List<NoticeDto> getNoticesCreatedBySuperAdmin(Integer superAdminId) {

        List<Notice> notices =
                noticeRepository.findByCreatedByIdAndCreatedByRole(
                        superAdminId,
                        NoticeCreatedByRole.SUPER_ADMIN
                );

        return notices.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


// GET SOCIETY ADMIN NOTICE BY ITSELF
    @Override
    public List<NoticeDto> getNoticesCreatedBySocietyAdmin(Integer societyAdminId) {

        List<Notice> notices =
                noticeRepository.findByCreatedByIdAndCreatedByRole(
                        societyAdminId,
                        NoticeCreatedByRole.SOCIETY_ADMIN
                );

        return notices.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



// GET SOCIETY ADMIN NOTICE BY STAFF
   public List<NoticeDto> getNoticesForStaff(Integer societyId) {
       // STAFF role
       List<Notice> notices = noticeRepository.findNoticesForStaff(societyId, TargetAudience.STAFF);
       return notices.stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
   }





    // 3. GET GLOBAL NOTICES
    @Override
    public List<NoticeDto> getGlobalAdminNotices() {
        return noticeRepository.findByTargetSocietyIdIsNullAndTargetRoleAndIsActiveTrue(TargetAudience.SOCIETY_ADMIN)
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    @Override
    public List<NoticeDto> getNoticesForNormalUser(Integer societyId) {

        List<Notice> notices =
                noticeRepository.findNoticesForNormalUser(societyId);

        return notices.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



// 4.  UPDATE NOTICE
    @Override
    public NoticeDto updateNotice(Integer noticeId,
                                  NoticeDto dto,
                                  Integer userId,
                                  TargetAudience role,
                                  Integer societyId,
                                  MultipartFile attachment) throws IOException {

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        if (!notice.getCreatedById().equals(userId))
            throw new RuntimeException("You are not allowed to edit this notice");

        if (role == TargetAudience.SOCIETY_ADMIN &&
                !notice.getTargetSocietyId().equals(societyId))
            throw new RuntimeException("Cross society edit not allowed");

        // Update fields
        notice.setTitle(dto.getTitle());
        notice.setMessage(dto.getMessage());
        notice.setPriority(dto.getPriority());
        notice.setNoticeType(dto.getNoticeType());
        notice.setExpiryDate(dto.getExpiryDate());

        if (attachment != null && !attachment.isEmpty()) {
            String fileName = fileService.uploadImage("notice-images", attachment);
            notice.setAttachmentUrl(fileName);
        }

        Notice updated = noticeRepository.save(notice);
        return mapToDto(updated);
    }






// 5. DELETE NOTICE SOFT DELETE
//    @Override
//    public void deleteNotice(Integer noticeId,
//                             Integer userId,
//                             NoticeRole role,
//                             Integer societyId) {
//
//        Notice notice = noticeRepository.findById(noticeId)
//                .orElseThrow(() -> new RuntimeException("Notice not found"));
//
//        if (!notice.getCreatedById().equals(userId))
//            throw new RuntimeException("You are not allowed to delete this notice");
//
//        if (role == NoticeRole.SOCIETY_ADMIN &&
//                !notice.getTargetSocietyId().equals(societyId))
//            throw new RuntimeException("Cross society delete not allowed");
//
//        // Soft delete
//        notice.setIsActive(false);
//        noticeRepository.save(notice);

//    }

// 5. DELETE NOTICE HARD DELETE
    @Transactional
    @Override
    public void deleteNotice(Integer noticeId,
                             Integer userId,
                             TargetAudience role,
                             Integer societyId) {

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        // Only creator can delete
        if (!notice.getCreatedById().equals(userId))
            throw new RuntimeException("You are not allowed to delete this notice");

        // Society admin cannot delete cross-society notices
        if (role == TargetAudience.SOCIETY_ADMIN &&
                !notice.getTargetSocietyId().equals(societyId))
            throw new RuntimeException("Cross society delete not allowed");

        // Hard delete
        notificationService.deleteNotificationByNoticeId(noticeId);
        noticeRepository.delete(notice);

    }


    // HELPER
    private NoticeDto mapToDto(Notice notice) {
        NoticeDto dto = new NoticeDto();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setMessage(notice.getMessage());
        dto.setCreatedById(notice.getCreatedById());
        dto.setCreatedByName(notice.getCreatedByName());
        dto.setCreatedByRole(notice.getCreatedByRole());
        dto.setTargetRole(notice.getTargetRole());
        dto.setTargetSocietyId(notice.getTargetSocietyId());
        dto.setPriority(notice.getPriority());
        dto.setNoticeType(notice.getNoticeType());
        dto.setAttachmentUrl(notice.getAttachmentUrl());
        dto.setIsActive(notice.getIsActive());
        dto.setExpiryDate(notice.getExpiryDate());
        dto.setCreatedAt(notice.getCreatedAt());
        dto.setUpdatedAt(notice.getUpdatedAt());
        return dto;
    }

}









// 5. DELETE NOTICE SOFT DELETE
//    @Override
//    public void deleteNotice(Integer noticeId,
//                             Integer userId,
//                             NoticeRole role,
//                             Integer societyId) {
//
//        Notice notice = noticeRepository.findById(noticeId)
//                .orElseThrow(() -> new RuntimeException("Notice not found"));
//
//        if (!notice.getCreatedById().equals(userId))
//            throw new RuntimeException("You are not allowed to delete this notice");
//
//        if (role == NoticeRole.SOCIETY_ADMIN &&
//                !notice.getTargetSocietyId().equals(societyId))
//            throw new RuntimeException("Cross society delete not allowed");
//
//        // Soft delete
//        notice.setIsActive(false);
//        noticeRepository.save(notice);
//    }