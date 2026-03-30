package com.example.demo.Entities;

import com.example.demo.Enums.SocietyStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "societies")
public class Society {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String address;

    private String city;

    @Enumerated(EnumType.STRING)
    private SocietyStatus isActive = SocietyStatus.ACTIVE;

    @PrePersist                               // Jab Society ko add karenge to defaulter status Active hoga
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = SocietyStatus.ACTIVE;
        }
    }

    @OneToOne(mappedBy = "society", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SocietyAdmin societyAdmin;



// Getters & Setters


    public SocietyAdmin getSocietyAdmin() {
        return societyAdmin;
    }

    public void setSocietyAdmin(SocietyAdmin societyAdmin) {
        this.societyAdmin = societyAdmin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public SocietyStatus getIsActive() {
        return isActive;
    }

    public void setIsActive(SocietyStatus isActive) {
        this.isActive = isActive;
    }
}
