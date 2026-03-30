package com.example.demo.ECommerce.Dtos;

import java.util.List;

public class CartDto {

    private Long id;
    private Long buyerId;
    private Long societyId;
    private List<Long> productIds;
    private List<Integer> quantities;

    public CartDto() {}

    public CartDto(Long id, Long buyerId, Long societyId, List<Long> productIds, List<Integer> quantities) {
        this.id = id;
        this.buyerId = buyerId;
        this.societyId = societyId;
        this.productIds = productIds;
        this.quantities = quantities;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Long getSocietyId() { return societyId; }
    public void setSocietyId(Long societyId) { this.societyId = societyId; }

    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }

    public List<Integer> getQuantities() { return quantities; }
    public void setQuantities(List<Integer> quantities) { this.quantities = quantities; }
}