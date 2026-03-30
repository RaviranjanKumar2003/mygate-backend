package com.example.demo.ECommerce.ERepositories;

import com.example.demo.ECommerce.Eentities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByProductId(Long productId);
    List<Offer> findByBuyerId(Long buyerId);
}