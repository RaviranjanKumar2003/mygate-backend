package com.example.demo.Payloads;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class VisitorCodeRequestDto {

    private Integer userId;       // owner/tenant id
    private String visitorName;   // visitor ka naam

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") // match datetime-local input
    private LocalDateTime expiryTime; // custom expiry

    private Integer societyId;

// GETTERS & SETTERS


    public Integer getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Integer societyId) {
        this.societyId = societyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}