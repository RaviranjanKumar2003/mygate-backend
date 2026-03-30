package com.example.demo.ECommerce.Eentities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long buyerId;   // member/user who placed order

    private Long societyId;

    private BigDecimal totalAmount;

    private String status; // PENDING, COMPLETED, CANCELLED

    private LocalDateTime orderDate;

    @ElementCollection
    private List<Long> productIds; // List of product ids in this order

    @ElementCollection
    private List<Integer> quantities; // Corresponding quantity of each product

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private OrderAddress deliveryAddress;

    private String paymentMethod;

// Constructors
    public Order() {}

    public Order(Long id, Long buyerId, Long societyId, BigDecimal totalAmount,
                 String status, LocalDateTime orderDate, List<Long> productIds, List<Integer> quantities) {
        this.id = id;
        this.buyerId = buyerId;
        this.societyId = societyId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.productIds = productIds;
        this.quantities = quantities;
    }

// Getters & Setters


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(OrderAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Long getSocietyId() { return societyId; }
    public void setSocietyId(Long societyId) { this.societyId = societyId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }

    public List<Integer> getQuantities() { return quantities; }
    public void setQuantities(List<Integer> quantities) { this.quantities = quantities; }
}