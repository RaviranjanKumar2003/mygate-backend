package com.example.demo.Payloads;

import java.time.LocalDateTime;

public class VerificationCodeDto {

    private Integer id;
    private String code;
    private LocalDateTime expiryTime;
    private boolean used;
    private Integer userId;

    private String visitorName;


// Constructors
    public VerificationCodeDto() {}

    public VerificationCodeDto(Integer id, String code, LocalDateTime expiryTime, boolean used, Integer userId, String visitorName) {
        this.id = id;
        this.code = code;
        this.expiryTime = expiryTime;
        this.used = used;
        this.userId = userId;
        this.visitorName = visitorName;
    }

    // Getters & Setters


    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public LocalDateTime getExpiryTime() { return expiryTime; }
    public void setExpiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public Integer getUserId() {
        return userId;
    }
}