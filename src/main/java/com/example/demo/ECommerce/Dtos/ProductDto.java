package com.example.demo.ECommerce.Dtos;

import java.math.BigDecimal;
import java.util.List;

public class ProductDto {

    private Long id;
    private String title;
    private String description;
    private List<String> images;
    private BigDecimal price;
    private Integer stock;
    private Long sellerId;
    private Long societyId;
    private String category;

    private Boolean codAvailable;

// Constructors
    public ProductDto() {}

    public ProductDto(Long id, String title, String description, List<String> images,
                      BigDecimal price, Integer stock, Long sellerId, Long societyId, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.images = images;
        this.price = price;
        this.stock = stock;
        this.sellerId = sellerId;
        this.societyId = societyId;
        this.category = category;
    }

// Getters & Setters


    public Boolean getCodAvailable() {
        return codAvailable;
    }

    public void setCodAvailable(Boolean codAvailable) {
        this.codAvailable = codAvailable;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public Long getSocietyId() { return societyId; }
    public void setSocietyId(Long societyId) { this.societyId = societyId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}