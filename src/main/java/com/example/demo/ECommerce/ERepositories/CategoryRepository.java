package com.example.demo.ECommerce.ERepositories;

import com.example.demo.ECommerce.Eentities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findBySocietyId(Long societyId);
}