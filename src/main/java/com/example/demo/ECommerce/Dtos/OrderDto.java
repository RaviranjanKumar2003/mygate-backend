package com.example.demo.ECommerce.Dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    private Long id;
    private Long buyerId;
    private Long societyId;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private List<Long> productIds;
    private List<Integer> quantities;

    private Long addressId;
    private OrderAddressDto address;

    private String paymentMethod;

    // Constructors
    public OrderDto() {}

    public OrderDto(Long id, Long buyerId, Long societyId, BigDecimal totalAmount, String status,
                    LocalDateTime orderDate, List<Long> productIds, List<Integer> quantities) {
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

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public OrderAddressDto getAddress() {
        return address;
    }

    public void setAddress(OrderAddressDto address) {
        this.address = address;
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