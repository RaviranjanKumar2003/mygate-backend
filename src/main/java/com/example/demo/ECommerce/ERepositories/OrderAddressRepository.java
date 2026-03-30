package com.example.demo.ECommerce.ERepositories;

import com.example.demo.ECommerce.Eentities.OrderAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderAddressRepository extends JpaRepository<OrderAddress, Long> {
    List<OrderAddress> findByUserId(Long userId);
}