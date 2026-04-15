package com.example.demo.ECommerce.Eentities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Integer productId;      // Which product this offer is for
    private Integer buyerId;        // Member placing the offer
    private BigDecimal offerPrice;

    private LocalDateTime offerTime;

    private String status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

// CONSTRUCTORS
    public Offer() {}

    public Offer(Integer id, Integer productId, Integer buyerId, BigDecimal offerPrice, LocalDateTime offerTime) {
        this.id = id;
        this.productId = productId;
        this.buyerId = buyerId;
        this.offerPrice = offerPrice;
        this.offerTime = offerTime;
    }

// GETTERS & SETTERS


    public Product getProduct() {
        return product;
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