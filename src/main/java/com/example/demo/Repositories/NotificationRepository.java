package com.example.demo.Repositories;

import com.example.demo.Entities.Notification;
import com.example.demo.Enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findBySocietyIdAndReceiverAdminIdOrderByCreatedAtDesc(
            Integer societyId,
            Integer receiverAdminId
    );

    List<Notification> findByReferenceIdAndType(Integer referenceId, NotificationType type);

    void deleteByNotice_Id(Integer noticeId);


    @Query("""
SELECT n FROM Notification n
WHERE
    n.targetRole = :role
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



}
