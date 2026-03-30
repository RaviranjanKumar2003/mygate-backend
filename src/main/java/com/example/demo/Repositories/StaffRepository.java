package com.example.demo.Repositories;

import com.example.demo.Entities.NormalUser;
import com.example.demo.Entities.Staff;
import com.example.demo.Entities.User;
import com.example.demo.Enums.StaffLevel;
import com.example.demo.Enums.StaffStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

    // GET staff by level
    List<Staff> findByStaffLevel(StaffLevel staffLevel);


    Optional<Staff> findByUser(User user);



    Optional<Staff> findByEmail(String email);



}
