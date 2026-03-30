package com.example.demo.Entities;

import com.example.demo.Enums.FlatStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "flats", uniqueConstraints = {@UniqueConstraint(columnNames = { "society_id","building_id","floor_id","flat_number" })})
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "flat_number", nullable = false)
    private String flatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "flat_status", nullable = false)
    private FlatStatus flatStatus = FlatStatus.VACANT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;


    @PrePersist
    public void prePersist() {
        if (flatStatus == null) {
            flatStatus = FlatStatus.VACANT;
        }
    }

    // kis Society ka hai
    @ManyToOne
    private Society society;

    // kis Building ka hai
    @ManyToOne
    private Building building;

// getters & setters


    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public FlatStatus getFlatStatus() {
        return flatStatus;
    }

    public void setFlatStatus(FlatStatus flatStatus) {
        this.flatStatus = flatStatus;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }
}
