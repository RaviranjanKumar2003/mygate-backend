package com.example.demo.Payloads;

import com.example.demo.Enums.NormalUserType;
import com.example.demo.Enums.UserRole;

public class LoginResponse {

    private Integer id;
    private String token;
    private UserRole role;
    private NormalUserType userType;
    private String email;
    private String name;

    private Integer societyId;
    private String societyName;

    // 3-args constructor
    public LoginResponse(Integer id, String token, UserRole role, NormalUserType userType, String email, String name, Integer societyId, String societyName) {
        this.id=id;
        this.token = token;
        this.role = role;
        this.userType=userType;
        this.email = email;
        this.name=name;
        this.societyId=societyId;
        this.societyName=societyName;

    }


// GETTERS & SETTERS


    public NormalUserType getUserType() {
        return userType;
    }

    public void setUserType(NormalUserType userType) {
        this.userType = userType;
    }

    public Integer getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Integer societyId) {
        this.societyId = societyId;
    }

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
