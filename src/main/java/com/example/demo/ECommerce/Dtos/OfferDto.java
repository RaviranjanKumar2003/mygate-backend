package com.example.demo.ECommerce.Dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OfferDto {

    private Integer id;
    private Integer productId;
    private Integer buyerId;
    private BigDecimal offerPrice;
    private LocalDateTime offerTime;

    private String status;

    private String buyerName;
    private String buyerEmail;
    private String buyerMobile;

    public OfferDto() {}

    public OfferDto(Integer id, Integer productId, Integer buyerId, BigDecimal offerPrice, LocalDateTime offerTime) {
        this.id = id;
        this.productId = productId;
        this.buyerId = buyerId;
        this.offerPrice = offerPrice;
        this.offerTime = offerTime;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }

    public LocalDateTime getOfferTime() { return offerTime; }
    public void setOfferTime(LocalDateTime offerTime) { this.offerTime = offerTime; }
}