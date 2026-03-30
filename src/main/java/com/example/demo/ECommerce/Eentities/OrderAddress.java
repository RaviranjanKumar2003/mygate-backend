package com.example.demo.ECommerce.Eentities;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class OrderAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // कौन सा यूजर यह address use करेगा

    private String name; // उदाहरण: "Home", "Office"
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phone;

// Constructors
    public OrderAddress() {}

    public OrderAddress(Long id, Long userId, String name, String line1, String line2,
                        String city, String state, String zip, String country, String phone) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.phone = phone;
    }

// Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLine1() { return line1; }
    public void setLine1(String line1) { this.line1 = line1; }

    public String getLine2() { return line2; }
    public void setLine2(String line2) { this.line2 = line2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}