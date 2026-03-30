package com.example.demo.Services.Implementation;

import com.example.demo.Entities.Notification;
import com.example.demo.Enums.NotificationType;
import com.example.demo.Payloads.NotificationDto;
import com.example.demo.Repositories.NotificationRepository;
import com.example.demo.Services.NotificationService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper modelMapper;


// CREATE NOTIFICATION
//    @Override
//    public void createNotification(NotificationDto dto) {
//        Notification notification = modelMapper.map(dto, Notification.class);
//        notification.setRead(false);
//        notification.setCreatedAt(LocalDateTime.now());
//        notificationRepository.save(notification);
//    }

    @Override
    public void createNotification(NotificationDto dto) {
        Notification notification = modelMapper.map(dto, Notification.class);

        // 🔥 IMPORTANT
        if (notification.getType() == null) {
            throw new RuntimeException("Notification type is required");
        }

        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }



    // GET NOTIFICATION
    @Override
    public List<NotificationDto> getNotificationsForAdmin(
            Integer societyId,
            Integer adminId
    ) {
        return notificationRepository
                .findBySocietyIdAndReceiverAdminIdOrderByCreatedAtDesc(
                        societyId, adminId
                )
                .stream()
                .map(n -> modelMapper.map(n, NotificationDto.class))
                .collect(Collectors.toList());
    }



// ================= FETCH NOTIFICATIONS =================
@Override
public List<NotificationDto> getNotificationsForUser(
        Integer societyId,
        String userRole
) {
    return notificationRepository
            .findForUser(societyId, userRole)
            .stream()
            .map(n -> modelMapper.map(n, NotificationDto.class))
            .collect(Collectors.toList());
}



    @Override
    @Transactional
    public void deleteNotificationByNoticeId(Integer noticeId) {
        notificationRepository.deleteByNotice_Id(noticeId);
    }



    // MARK AS READ NOTIFICATION
    @Override
    public void markAsRead(Long notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setRead(true);
        notificationRepository.save(n);
    }
}
