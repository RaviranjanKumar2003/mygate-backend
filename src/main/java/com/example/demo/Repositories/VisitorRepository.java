package com.example.demo.Repositories;

import com.example.demo.Entities.Building;
import com.example.demo.Entities.Visitor;
import com.example.demo.Enums.VisitorStatus;
import com.example.demo.Enums.VisitorType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitorRepository extends JpaRepository<Visitor, Integer> {

    Optional<Visitor> findByMobileNumber(String mobileNumber);

    List<Visitor> findBySocietyIdAndVisitorStatus(Integer societyId, VisitorStatus status);

    List<Visitor> findBySocietyIdAndVisitorType(Integer societyId, VisitorType visitorType);

    int countByMobileNumberAndCreatedAtAfter(String mobileNumber, LocalDateTime dateTime);

    List<Visitor> findBySociety_Id(Integer societyId);

    Optional<Visitor> findByIdAndSociety_Id(Integer visitorId, Integer societyId);

    List<Visitor> findByFlat_Id(Integer flatId);

    List<Visitor> findBySociety_IdAndVisitorStatus(
            Integer societyId,
            VisitorStatus visitorStatus
    );

    List<Visitor> findByFlat_IdAndVisitorStatus(
            Integer flatId,
            VisitorStatus visitorStatus
    );

    Optional<Visitor> findByIdAndSocietyId(Integer id, Integer societyId);



    // 🔍 SEARCH (name / mobile / id)
    @Query("""
        SELECT v FROM Visitor v
        WHERE v.society.id = :societyId
        AND (
            LOWER(v.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR v.mobileNumber LIKE CONCAT('%', :keyword, '%')
            OR CAST(v.id AS string) = :keyword
        )
    """)
    List<Visitor> searchVisitors(
            @Param("societyId") Integer societyId,
            @Param("keyword") String keyword
    );



    @Modifying
    @Transactional
    @Query("update Visitor v set v.visitorStatus = :status where v.id = :id")
    void updateVisitorStatus(@Param("id") Integer id, @Param("status") VisitorStatus status);


// COUNT VISITOR BY TODAY DATE SOCIETY WISE
    @Query("""
        SELECT COUNT(v)
        FROM Visitor v
        WHERE v.society.id = :societyId
        AND v.createdAt >= :start
        AND v.createdAt < :end
    """)
    long countTodayVisitors(
            @Param("societyId") Long societyId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // FLAT WISE
    @Query("""
        SELECT COUNT(v)
        FROM Visitor v
        WHERE v.society.id = :societyId
        AND v.flat.id = :flatId
        AND v.createdAt >= :start
        AND v.createdAt < :end
    """)
    long countTodayVisitorsByFlat(
            Long societyId,
            Long flatId,
            LocalDateTime start,
            LocalDateTime end
    );

    Optional<Visitor> findTopByNameAndVisitorStatus(String name, VisitorStatus status);

}

