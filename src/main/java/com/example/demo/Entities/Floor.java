package com.example.demo.Entities;

import com.example.demo.Enums.BuildingStatus;
import com.example.demo.Enums.FloorStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "floors")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String floorNumber;

    @Enumerated(EnumType.STRING)
    private FloorStatus floorStatus=FloorStatus.ACTIVE;

    // By Default Agar floorNumber khali raha to Ground Floor add ho jayega or PENDING
    @PrePersist
    protected void onCreate() {
        if (this.floorStatus == null) {
            this.floorStatus = FloorStatus.ACTIVE;
        }
    }


    // kis Society ka hai
    @ManyToOne
    private Society society;

    // kis Building ka hai
    @ManyToOne
    private Building building;


// GETTERS & SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public FloorStatus getFloorStatus() {
        return floorStatus;
    }

    public void setFloorStatus(FloorStatus floorStatus) {
        this.floorStatus = floorStatus;
    }

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
}
