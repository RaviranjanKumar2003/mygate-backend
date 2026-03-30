package com.example.demo.SuperAdminFolder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Integer> {

    Optional<SuperAdmin> findByEmail(String email);

    boolean existsByEmail(String email);




}
