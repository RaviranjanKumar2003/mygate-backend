package com.example.demo.Entities;

import com.example.demo.Enums.BuildingStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "buildings")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "society_id")
    private Society society;

    @Enumerated(EnumType.STRING)
    private BuildingStatus isActive = BuildingStatus.ACTIVE;

    @PrePersist                               // Jab Society ko add karenge to defaulter status Active hoga
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = BuildingStatus.ACTIVE;
        }
    }


// Getters & Setters


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

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public BuildingStatus getIsActive() {
        return isActive;
    }

    public void setIsActive(BuildingStatus isActive) {
        this.isActive = isActive;
    }
}