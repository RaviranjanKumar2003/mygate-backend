package com.example.demo.SuperAdminFolder;

import com.example.demo.Enums.UserRole;

import java.time.LocalDateTime;

public class SuperAdminDto {

    private int id;

    private String name;

    private String email;

    private String password;

    private String mobileNumber;

    private String imageURL;

    private UserRole userRole;

    private SuperAdminStatus superAdminStatus;

    private LocalDateTime createdAt;

    // GETTERS & SETTERS


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public SuperAdminStatus getSuperAdminStatus() {
        return superAdminStatus;
    }

    public void setSuperAdminStatus(SuperAdminStatus superAdminStatus) {
        this.superAdminStatus = superAdminStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
