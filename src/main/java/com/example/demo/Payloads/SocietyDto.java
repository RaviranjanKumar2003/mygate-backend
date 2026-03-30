package com.example.demo.Payloads;

import com.example.demo.Enums.SocietyStatus;
import lombok.Data;

@Data
public class SocietyDto {

    private Integer id;

    private String name;

    private String address;

    private String city;

    private SocietyStatus isActive;

    private SocietyAdminDto societyAdmin;


//=============== Getters & Setters ==============================

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

    public SocietyAdminDto getSocietyAdmin() {
        return societyAdmin;
    }

    public void setSocietyAdmin(SocietyAdminDto societyAdmin) {
        this.societyAdmin = societyAdmin;
    }
}
