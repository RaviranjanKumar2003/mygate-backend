package com.example.demo.Payloads;

import com.example.demo.Enums.BuildingStatus;

public class BuildingDto {

    private Integer id;

    private String name;

    private Integer societyId;

    private BuildingStatus isActive;



// GETTERS & SETTERS

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

    public Integer getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Integer societyId) {
        this.societyId = societyId;
    }

    public BuildingStatus getIsActive() {
        return isActive;
    }

    public void setIsActive(BuildingStatus isActive) {
        this.isActive = isActive;
    }
}
