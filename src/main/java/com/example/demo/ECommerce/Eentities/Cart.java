package com.example.demo.ECommerce.Eentities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long buyerId; // member/user id

    private Long societyId;

    @ElementCollection
    private List<Long> productIds; // product ids in cart

    @ElementCollection
    private List<Integer> quantities; // corresponding quantity of each product

    public Cart() {}

    public Cart(Long id, Long buyerId, Long societyId, List<Long> productIds, List<Integer> quantities) {
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