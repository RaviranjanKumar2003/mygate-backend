package com.example.demo.Controllers;

import com.example.demo.Enums.NoticeCreatedByRole;
import com.example.demo.Enums.TargetAudience;
import com.example.demo.Payloads.NoticeDto;
import com.example.demo.Repositories.NotificationRepository;
import com.example.demo.Services.NoticeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @Autowired
    private NotificationRepository notificationRepository;

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }


// 1.  CREATE NOTICE BY SUPER ADMIN  FOR ALL SOCIETIES
    @PostMapping("/superAdminId/{superAdminId}/create")
    public ResponseEntity<NoticeDto> createNoticeBySuperAdmin(
            @PathVariable Integer superAdminId,
            @RequestBody NoticeDto dto
    ) throws IOException {
        return ResponseEntity.ok(
                noticeService.createNotice(dto, superAdminId, NoticeCreatedByRole.SUPER_ADMIN, null, null)
        );
    }


// 1.2 SUPER ADMIN → CREATE NOTICE FOR A SPECIFIC SOCIETY
    @PostMapping("/superAdminId/{superAdminId}/society/{societyId}/create")
    public ResponseEntity<NoticeDto> createNoticeBySuperAdminForSociety(
            @PathVariable Integer superAdminId,
            @PathVariable Integer societyId,
            @RequestBody NoticeDto dto
    ) throws IOException {

        return ResponseEntity.ok(
                noticeService.createNotice(
                        dto,
                        superAdminId,
                        NoticeCreatedByRole.SUPER_ADMIN,
                        societyId,
                        null
                )
        );
    }


// 2. CREATE NOTICE BY SOCIETY ADMIN
   @PostMapping("/society/{societyId}/societyAdminId/{societyAdminId}/create")
   public ResponseEntity<NoticeDto> createNoticeBySocietyAdmin(
           @PathVariable Integer societyId,
           @PathVariable Integer societyAdminId,
           @RequestBody NoticeDto dto
) throws IOException {

    return ResponseEntity.ok(
            noticeService.createNotice(
                    dto,
                    societyAdminId,
                    NoticeCreatedByRole.SOCIETY_ADMIN,
                    societyId,
                    null
            )
    );
}




// GET SUPER ADMIN NOTICE BY SOCIETY ADMIN
    @GetMapping("/society/{societyId}/societyAdminId/{societyAdminId}")
    public ResponseEntity<List<NoticeDto>> getSocietyNotices(
            @PathVariable Integer societyId,
            @PathVariable Integer societyAdminId
    ) {
        List<NoticeDto> notices = noticeService.getNoticesForSociety(societyId);
        return ResponseEntity.ok(notices);
    }


// GET SUPER ADMIN NOTICE BY ITSELF

    @GetMapping("/superAdminId/{superAdminId}")
    public ResponseEntity<List<NoticeDto>> getNoticesCreatedBySuperAdmin(
            @PathVariable Integer superAdminId
    ) {
        List<NoticeDto> notices =
                noticeService.getNoticesCreatedBySuperAdmin(superAdminId);
        return ResponseEntity.ok(notices);
    }


// GET SOCIETY ADMIN NOTICE BY ITSELF
    @GetMapping("/societyAdminId/{societyAdminId}")
    public ResponseEntity<List<NoticeDto>> getNoticesCreatedBySocietyAdmin(
            @PathVariable Integer societyAdminId
    ) {
        List<NoticeDto> notices =
                noticeService.getNoticesCreatedBySocietyAdmin(societyAdminId);
        return ResponseEntity.ok(notices);
    }


// GET SOCIETY ADMIN NOTICE BY STAFFS
   @GetMapping("/society/{societyId}/staff/{staffId}")
   public ResponseEntity<List<NoticeDto>> getNoticesForStaff(
           @PathVariable Integer societyId,
           @PathVariable Integer staffId
   ) {
       List<NoticeDto> notices = noticeService.getNoticesForStaff(societyId);
       return ResponseEntity.ok(notices);
   }





    // Get global notices
    @GetMapping("/admin/global")
    public ResponseEntity<List<NoticeDto>> getGlobalAdminNotices() {
        return ResponseEntity.ok(noticeService.getGlobalAdminNotices());
    }


    @GetMapping("/normal-user")
    public ResponseEntity<List<NoticeDto>> getUserNotices(
            @RequestParam Integer societyId
    ) {
        return ResponseEntity.ok(
                noticeService.getNoticesForNormalUser(societyId)
        );
    }



// Update notice
    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeDto> updateNotice(
            @PathVariable Integer noticeId,
            @RequestPart("dto") String dtoString,
            @RequestParam Integer userId,
            @RequestParam TargetAudience role,
            @RequestParam(required = false) Integer societyId,
            @RequestPart(required = false) MultipartFile attachment
    ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        NoticeDto dto = mapper.readValue(dtoString, NoticeDto.class);

        return ResponseEntity.ok(
                noticeService.updateNotice(noticeId, dto, userId, role, societyId, attachment)
        );
    }




// Delete notice
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<String> deleteNotice(
            @PathVariable Integer noticeId,
            @RequestParam Integer userId,
            @RequestParam TargetAudience role,
            @RequestParam(required = false) Integer societyId
    ) {
        noticeService.deleteNotice(noticeId, userId, role, societyId);
        return ResponseEntity.ok("Notice deleted successfully");
    }








}
