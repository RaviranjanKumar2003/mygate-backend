package com.example.demo.Repositories;

import com.example.demo.Entities.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {


    // Society ke complaints (Staff / Owner / Tenant wale)
    List<Complaint> findBySocietyIdOrderByCreatedAtDesc(Integer societyId);

    // SocietyAdmin ke dwara create ki gayi complaints
    List<Complaint> findByCreatedByRoleOrderByCreatedAtDesc(String role);

    // NormalUser → apne complaints
    List<Complaint> findByCreatedByIdAndCreatedByRoleOrderByCreatedAtDesc(
            Integer createdById,
            String role
    );

}
