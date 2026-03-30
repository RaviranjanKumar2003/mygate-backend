package com.example.demo.ECommerce.ERepositories;// src/main/java/com/example/demo/repository/ProductRepository.java

import com.example.demo.ECommerce.Eentities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findBySocietyId(Long societyId);

    List<Product> findBySellerId(Long sellerId);
}