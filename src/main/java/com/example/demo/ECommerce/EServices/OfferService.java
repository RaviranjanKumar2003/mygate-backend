package com.example.demo.ECommerce.EServices;

import com.example.demo.ECommerce.Dtos.OfferDto;

import java.util.List;

public interface OfferService {

    OfferDto createOffer(OfferDto offerDto);

    OfferDto updateOffer(Integer id, OfferDto offerDto);

    void deleteOffer(Integer id);

    OfferDto getOfferById(Integer id);

    List<OfferDto> getAllOffers();

    List<OfferDto> getOffersByProduct(Integer productId);

    List<OfferDto> getOffersByBuyer(Integer buyerId);
}