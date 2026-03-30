package com.example.demo.Services;

import com.example.demo.Enums.NoticeCreatedByRole;
import com.example.demo.Payloads.NoticeDto;
import com.example.demo.Enums.TargetAudience;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NoticeService {

    // Create notice
    NoticeDto createNotice(
            NoticeDto dto,
            Integer userId,
            NoticeCreatedByRole role,
            Integer societyId,
            MultipartFile attachment
    ) throws IOException;

    // Update notice
    NoticeDto updateNotice(
            Integer noticeId,
            NoticeDto dto,
            Integer userId,
            TargetAudience role,
            Integer societyId,
            MultipartFile attachment
    ) throws IOException;

    // Society Admin / Resident notices
    List<NoticeDto> getNoticesForSociety(Integer societyId);


    List<NoticeDto> getNoticesCreatedBySuperAdmin(Integer superAdminId);

    List<NoticeDto> getNoticesCreatedBySocietyAdmin(Integer societyAdminId);

    List<NoticeDto> getNoticesForStaff(Integer societyId);

    List<NoticeDto> getNoticesForNormalUser(Integer societyId);


    // Super Admin → Society Admin notices
    List<NoticeDto> getGlobalAdminNotices();

    // Delete notice
    void deleteNotice(
            Integer noticeId,
            Integer loggedInUserId,
            TargetAudience loggedInRole,
            Integer societyId
    );



}
