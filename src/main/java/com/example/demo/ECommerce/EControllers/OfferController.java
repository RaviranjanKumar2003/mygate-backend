package com.example.demo.ECommerce.EControllers;

import com.example.demo.ECommerce.Dtos.OfferDto;
import com.example.demo.ECommerce.EServices.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<OfferDto> createOffer(@RequestBody OfferDto offerDto) {
        return ResponseEntity.ok(offerService.createOffer(offerDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferDto> updateOffer(@PathVariable Long id, @RequestBody OfferDto offerDto) {
        return ResponseEntity.ok(offerService.updateOffer(id, offerDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.ok("Offer deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDto> getOffer(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getOfferById(id));
    }

    @GetMapping
    public ResponseEntity<List<OfferDto>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OfferDto>> getOffersByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(offerService.getOffersByProduct(productId));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<OfferDto>> getOffersByBuyer(@PathVariable Long buyerId) {
        return ResponseEntity.ok(offerService.getOffersByBuyer(buyerId));
    }
}