package com.example.demo.ECommerce.ERepositories;

import com.example.demo.ECommerce.Eentities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByBuyerId(Long buyerId);
    List<Cart> findBySocietyId(Long societyId);
    Optional<Cart> findByBuyerIdAndSocietyId(Long buyerId, Long societyId);
}