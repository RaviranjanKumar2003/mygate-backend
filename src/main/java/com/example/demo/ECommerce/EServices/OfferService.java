package com.example.demo.ECommerce.EServices;

import com.example.demo.ECommerce.Dtos.OfferDto;

import java.util.List;

public interface OfferService {

    OfferDto createOffer(OfferDto offerDto);

    OfferDto updateOffer(Long id, OfferDto offerDto);

    void deleteOffer(Long id);

    OfferDto getOfferById(Long id);

    List<OfferDto> getAllOffers();

    List<OfferDto> getOffersByProduct(Long productId);

    List<OfferDto> getOffersByBuyer(Long buyerId);
}