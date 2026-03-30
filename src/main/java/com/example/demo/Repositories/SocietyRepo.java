package com.example.demo.Repositories;

import com.example.demo.Entities.Society;
import com.example.demo.Enums.SocietyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocietyRepo extends JpaRepository<Society,Integer> {


    List<Society> findByIsActive(SocietyStatus status);

    Optional<Society> findByIdAndIsActive(Integer id, SocietyStatus status);

    List<Society> findByNameContainingIgnoreCaseOrCityContainingIgnoreCaseAndIsActive(
            String name,
            String city,
            SocietyStatus status
    );



}
