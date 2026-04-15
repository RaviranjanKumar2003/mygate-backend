package com.example.demo.Controllers;

import com.example.demo.Repositories.NotificationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.Configuration.CustomUserDetails;
import com.example.demo.Payloads.NotificationDto;
import com.example.demo.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;


// GET notifications for society admin
    @GetMapping("/society/{societyId}/admin/{adminId}")
    public ResponseEntity<List<NotificationDto>> getNotifications(
            @PathVariable Integer societyId,
            @PathVariable Integer adminId
    ) {
        return ResponseEntity.ok(
                notificationService.getNotificationsForAdmin(societyId, adminId)
        );
    }


    @GetMapping("/user")
    public ResponseEntity<List<NotificationDto>> getUserNotifications() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        String userRole = null;
        Integer societyId = null;

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails user = (CustomUserDetails) principal;
            userRole = user.getRole();
            societyId = user.getSocietyId();  // user’s society
        }

        List<NotificationDto> notifications = notificationService.getNotificationsForUser(societyId, userRole);
        return ResponseEntity.ok(notifications);
    }


    // Mark notification as read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Integer notificationId
    ) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/mark-all-read")
    public ResponseEntity<Void> markAllRead() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        notificationRepository.markAllAsRead(
                user.getSocietyId(),
                user.getRole()
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        Long count = notificationRepository.countUnread(
                user.getSocietyId(),
                user.getRole()
        );

        return ResponseEntity.ok(count);
    }


    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().build();
    }

}
