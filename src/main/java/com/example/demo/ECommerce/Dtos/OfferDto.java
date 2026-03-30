package com.example.demo.ECommerce.Dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OfferDto {

    private Long id;
    private Long productId;
    private Long buyerId;
    private BigDecimal offerPrice;
    private LocalDateTime offerTime;

    private String status;

    public OfferDto() {}

    public OfferDto(Long id, Long productId, Long buyerId, BigDecimal offerPrice, LocalDateTime offerTime) {
        this.id = id;
        this.productId = productId;
        this.buyerId = buyerId;
        this.offerPrice = offerPrice;
        this.offerTime = offerTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }

    public LocalDateTime getOfferTime() { return offerTime; }
    public void setOfferTime(LocalDateTime offerTime) { this.offerTime = offerTime; }
}