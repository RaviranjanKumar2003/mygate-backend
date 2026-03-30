package com.example.demo.Repositories;

import com.example.demo.Entities.Notice;
import com.example.demo.Enums.NoticeCreatedByRole;
import com.example.demo.Enums.TargetAudience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    List<Notice> findByTargetSocietyIdAndTargetRoleInAndIsActiveTrue( Integer societyId, List<TargetAudience> roles );

    List<Notice> findByTargetSocietyIdIsNullAndTargetRoleAndIsActiveTrue( TargetAudience role);


    List<Notice> findByCreatedByIdAndCreatedByRole(Integer createdById, NoticeCreatedByRole createdByRole);

    List<Notice> findByTargetSocietyId(Integer societyId);

    @Query("SELECT n FROM Notice n " +
            "WHERE n.isActive = true " +
            "AND (n.targetSocietyId IS NULL OR n.targetSocietyId = :societyId) " +
            "ORDER BY n.createdAt DESC")
    List<Notice> findNoticesForSociety(@Param("societyId") Integer societyId);


    @Query("SELECT n FROM Notice n " +
            "WHERE n.isActive = true " +
            "AND (n.targetRole = :role OR n.targetRole = com.example.demo.Enums.TargetAudience.ALL) " +
            "AND (n.targetSocietyId IS NULL OR n.targetSocietyId = :societyId) " +
            "ORDER BY n.createdAt DESC")
    List<Notice> findNoticesForStaff(
            @Param("societyId") Integer societyId,
            @Param("role") TargetAudience role
    );



    @Query("""
        SELECT n FROM Notice n
        WHERE n.isActive = true
        AND (
             n.targetSocietyId = :societyId
             OR n.targetSocietyId IS NULL
        )
        AND n.createdByRole IN (
             com.example.demo.Enums.NoticeCreatedByRole.SUPER_ADMIN,
             com.example.demo.Enums.NoticeCreatedByRole.SOCIETY_ADMIN
        )
        ORDER BY n.createdAt DESC
    """)
    List<Notice> findNoticesForNormalUser(@Param("societyId") Integer societyId);





    // Notices for STAFF in a society or global notices
    List<Notice> findByTargetSocietyIdAndTargetRoleOrTargetRoleIsNull(
            Integer societyId,
            TargetAudience targetRole
    );



}
