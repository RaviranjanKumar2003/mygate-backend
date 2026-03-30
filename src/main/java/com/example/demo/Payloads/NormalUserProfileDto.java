package com.example.demo.Payloads;

import com.example.demo.Enums.NormalUserType;
import com.example.demo.Enums.UserStatus;

public class NormalUserProfileDto {

    private Integer id;
    private String name;
    private String email;
    private String mobileNumber;
    private NormalUserType normalUserType;
    private String imageUrl;

    private UserStatus userStatus;

    // getters & setters


    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
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

    public NormalUserType getNormalUserType() {
        return normalUserType;
    }

    public void setNormalUserType(NormalUserType normalUserType) {
        this.normalUserType = normalUserType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
