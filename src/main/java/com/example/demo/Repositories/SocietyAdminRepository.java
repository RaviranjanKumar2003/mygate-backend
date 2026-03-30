package com.example.demo.Repositories;

import com.example.demo.Entities.SocietyAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocietyAdminRepository extends JpaRepository<SocietyAdmin, Integer> {

    boolean existsByAdminEmail(String adminEmail);

    SocietyAdmin findFirstBySocietyId(Integer societyId);

    Optional<SocietyAdmin> findByAdminEmail(String adminEmail);

    Optional<SocietyAdmin> findByIdAndSociety_Id(
            Integer adminId,
            Integer societyId
    );


}
