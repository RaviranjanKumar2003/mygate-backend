package com.example.demo.ECommerce.EServices.ESImpl;

import com.example.demo.ECommerce.Dtos.OfferDto;
import com.example.demo.ECommerce.ERepositories.ProductRepository;
import com.example.demo.ECommerce.Eentities.Offer;
import com.example.demo.ECommerce.ERepositories.OfferRepository;
import com.example.demo.ECommerce.EServices.OfferService;
import com.example.demo.ECommerce.Eentities.Product;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ProductRepository productRepository;

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

        if (offer.getBuyerId() != null) {
            userRepository.findById(offer.getBuyerId()).ifPresent(user -> {
                dto.setBuyerName(user.getName());
                dto.setBuyerEmail(user.getEmail());
                dto.setBuyerMobile(user.getMobileNumber());
            });
        }

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

        // 🔥 product fetch karo
        Product product = productRepository.findById(
                Long.valueOf(offer.getProductId())  // ✅ FIX
        ).orElseThrow(() -> new RuntimeException("Product not found"));

// 🔥 sellerId nikalo
        Long sellerId = product.getSellerId();

// 🔔 notification bhejo
        messagingTemplate.convertAndSend(
                "/topic/offer/" + sellerId,
                "New Offer on product: " + product.getTitle()
        );

        return mapToDto(saved);
    }

    @Override
    public OfferDto updateOffer(Integer id, OfferDto offerDto) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
        offer.setOfferPrice(offerDto.getOfferPrice());
        Offer updated = offerRepository.save(offer);
        return mapToDto(updated);
    }

    @Override
    public void deleteOffer(Integer id) {
        offerRepository.deleteById(id);
    }

    @Override
    public OfferDto getOfferById(Integer id) {
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
    public List<OfferDto> getOffersByProduct(Integer productId) {
        List<Offer> offers = offerRepository.findByProductId(productId);
        List<OfferDto> dtos = new ArrayList<>();
        for (Offer o : offers) dtos.add(mapToDto(o));
        return dtos;
    }

    @Override
    public List<OfferDto> getOffersByBuyer(Integer buyerId) {
        List<Offer> offers = offerRepository.findByBuyerId(buyerId);
        List<OfferDto> dtos = new ArrayList<>();
        for (Offer o : offers) dtos.add(mapToDto(o));
        return dtos;
    }
}