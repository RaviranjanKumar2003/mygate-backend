package com.example.demo.Payloads;

import com.example.demo.Enums.SocietyStatus;

public class SocietyAdminDto {

    private int id;

    private String adminName;

    private String adminEmail;

    private String adminPassword;

    private String mobileNumber;

    private String imageURL;

    private SocietyStatus societyAdminStatus;

    private Integer societyId;


// GETTERS & SETTERS

    public SocietyAdminDto(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public SocietyStatus getSocietyAdminStatus() {
        return societyAdminStatus;
    }

    public void setSocietyAdminStatus(SocietyStatus societyAdminStatus) {
        this.societyAdminStatus = societyAdminStatus;
    }

    public Integer getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Integer societyId) {
        this.societyId = societyId;
    }
}
