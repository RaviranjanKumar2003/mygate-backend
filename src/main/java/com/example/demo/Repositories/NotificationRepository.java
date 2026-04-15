package com.example.demo.Repositories;

import com.example.demo.Entities.Notification;
import com.example.demo.Enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Integer> {

    /* ================= ADMIN ================= */

    List<Notification> findBySocietyIdAndReceiverAdminIdOrderByCreatedAtDesc(
            Integer societyId,
            Integer receiverAdminId
    );

    /* ================= COMMON ================= */

    List<Notification> findByReferenceIdAndType(Integer referenceId, NotificationType type);

    void deleteByNotice_Id(Integer noticeId);

    boolean existsByReferenceIdAndType(Long referenceId, NotificationType type);

    void deleteByReferenceId(Long referenceId);


    /* ================= USER FETCH ================= */

    @Query("""
    SELECT n FROM Notification n
    WHERE
    (
        n.targetRole = :role
        OR n.targetRole = 'ALL'
        OR n.targetRole IS NULL
    )
    AND
    (
        n.targetSocietyId IS NULL
        OR n.targetSocietyId = :societyId
    )
    ORDER BY n.createdAt DESC
    """)
    List<Notification> findForUser(
            @Param("societyId") Integer societyId,
            @Param("role") String role
    );


    /* ================= COUNT UNREAD ================= */

    @Query("""
    SELECT COUNT(n) FROM Notification n
    WHERE 
    n.isRead = false
    AND
    (
        n.targetRole = :role
        OR n.targetRole = 'ALL'
        OR n.targetRole IS NULL
    )
    AND 
    (
        n.targetSocietyId IS NULL 
        OR n.targetSocietyId = :societyId
    )
    """)
    Long countUnread(
            @Param("societyId") Integer societyId,
            @Param("role") String role
    );


    /* ================= MARK ALL READ ================= */

    @Modifying
    @Query("""
    UPDATE Notification n 
    SET n.isRead = true
    WHERE 
    (
        n.targetSocietyId IS NULL 
        OR n.targetSocietyId = :societyId
    )
    AND 
    (
        n.targetRole IS NULL 
        OR n.targetRole = :role
        OR n.targetRole = 'ALL'
    )
    """)
    void markAllAsRead(
            @Param("societyId") Integer societyId,
            @Param("role") String role
    );

}