package com.example.demo.ECommerce.EServices.ESImpl;

import com.example.demo.ECommerce.Dtos.OfferDto;
import com.example.demo.ECommerce.Eentities.Offer;
import com.example.demo.ECommerce.ERepositories.OfferRepository;
import com.example.demo.ECommerce.EServices.OfferService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    private OfferDto mapToDto(Offer offer) {
        OfferDto dto = new OfferDto(
                offer.getId(),
                offer.getProductId(),
                offer.getBuyerId(),
                offer.getOfferPrice(),
                offer.getOfferTime()
        );

        dto.setStatus(offer.getStatus()); // ✅ ADD THIS

        return dto;
    }

    private Offer mapToEntity(OfferDto dto) {
        Offer offer = new Offer(
                dto.getId(),
                dto.getProductId(),
                dto.getBuyerId(),
                dto.getOfferPrice(),
                dto.getOfferTime()
        );

        offer.setStatus(dto.getStatus()); // ✅ ADD THIS

        return offer;
    }

// CREATE OFFER
    @Override
    public OfferDto createOffer(OfferDto offerDto) {

        Offer offer = mapToEntity(offerDto);

        // ✅ NULL CHECK (important)
        if (offer.getOfferPrice() == null) {
            throw new RuntimeException("Offer price is required");
        }

        // ✅ DEFAULT VALUES SET
        if (offer.getOfferTime() == null) {
            offer.setOfferTime(java.time.LocalDateTime.now());
        }

        if (offer.getStatus() == null) {
            offer.setStatus("PENDING"); // PENDING / ACCEPTED / REJECTED
        }

        Offer saved = offerRepository.save(offer);

        return mapToDto(saved);
    }

    @Override
    public OfferDto updateOffer(Long id, OfferDto offerDto) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
        offer.setOfferPrice(offerDto.getOfferPrice());
        Offer updated = offerRepository.save(offer);
        return mapToDto(updated);
    }

    @Override
    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }

    @Override
    public OfferDto getOfferById(Long id) {
        return offerRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
    }

    @Override
    public List<OfferDto> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        List<OfferDto> dtos = new ArrayList<>();
        for (Offer o : offers) dtos.add(mapToDto(o));
        return dtos;
    }

    @Override
    public List<OfferDto> getOffersByProduct(Long productId) {
        List<Offer> offers = offerRepository.findByProductId(productId);
        List<OfferDto> dtos = new ArrayList<>();
        for (Offer o : offers) dtos.add(mapToDto(o));
        return dtos;
    }

    @Override
    public List<OfferDto> getOffersByBuyer(Long buyerId) {
        List<Offer> offers = offerRepository.findByBuyerId(buyerId);
        List<OfferDto> dtos = new ArrayList<>();
        for (Offer o : offers) dtos.add(mapToDto(o));
        return dtos;
    }
}