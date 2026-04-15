package com.example.demo.Services.Implementation;

import com.example.demo.Entities.NormalUser;
import com.example.demo.Entities.NoticeSeen;
import com.example.demo.Entities.SocietyAdmin;
import com.example.demo.Payloads.NoticeSeenDto;
import com.example.demo.Repositories.NormalUserRepository;
import com.example.demo.Repositories.NoticeSeenRepository;
import com.example.demo.Repositories.SocietyAdminRepository;
import com.example.demo.Services.NoticeSeenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeSeenServiceImpl implements NoticeSeenService {

    @Autowired
    private NoticeSeenRepository noticeSeenRepository;

    @Autowired
    private NormalUserRepository normalUserRepository;

    @Autowired
    private SocietyAdminRepository societyAdminRepository;

    // ================= MARK AS SEEN =================
    @Override
    public void markAsSeen(Integer noticeId, Integer userId, String role) {

        boolean exists = noticeSeenRepository
                .existsByNoticeIdAndUserIdAndUserRole(noticeId, userId, role);

        if (exists) {
            return;
        }

        NoticeSeen ns = new NoticeSeen();
        ns.setNoticeId(noticeId);
        ns.setUserId(userId);
        ns.setUserRole(role);
        ns.setSeenAt(LocalDateTime.now());

        noticeSeenRepository.save(ns);
    }

    // ================= GET SEEN USERS =================
    @Override
    public List<NoticeSeenDto> getSeenUsers(Integer noticeId) {

        List<NoticeSeen> list = noticeSeenRepository.findByNoticeId(noticeId);

        return list.stream().map(ns -> {

            NoticeSeenDto dto = new NoticeSeenDto();

            dto.setUserId(ns.getUserId());
            dto.setUserRole(ns.getUserRole());

            // ================= SOCIETY ADMIN =================
            if ("SOCIETY_ADMIN".equals(ns.getUserRole())) {

                SocietyAdmin admin = societyAdminRepository
                        .findById(ns.getUserId())
                        .orElse(null);

                if (admin != null) {
                    dto.setUserName(admin.getAdminName());

                    if (admin.getSociety() != null) {
                        dto.setSocietyName(admin.getSociety().getName());
                    }
                }
            }

            // ================= NORMAL USER =================
            else if ("NORMAL_USER".equals(ns.getUserRole())) {

                NormalUser user = normalUserRepository
                        .findById(ns.getUserId())
                        .orElse(null);

                if (user != null) {

                    dto.setUserName(user.getUser().getName());

                    dto.setUserType(
                            user.getNormalUserType() != null
                                    ? user.getNormalUserType().name()
                                    : null
                    );

                    if (user.getBuilding() != null) {
                        dto.setBuilding(user.getBuilding().getName());
                    }

                    if (user.getFloor() != null) {
                        dto.setFloor(user.getFloor().getFloorNumber());
                    }

                    if (user.getFlat() != null) {
                        dto.setFlat(user.getFlat().getFlatNumber());
                    }
                }
            }

            return dto;

        }).toList();
    }
}