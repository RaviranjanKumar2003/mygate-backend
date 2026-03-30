package com.example.demo.Payloads;

import com.example.demo.Enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {

    private Long id;
    private String title;
    private String message;

    private NotificationType type;

    private Integer societyId;
    private Integer receiverAdminId;

    private boolean isRead;

    private LocalDateTime createdAt;

    private Integer targetSocietyId; // null = global
    private String targetRole;
    private Integer referenceId;


// GETTERS & SETTERS


    public Integer getTargetSocietyId() {
        return targetSocietyId;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setTargetSocietyId(Integer targetSocietyId) {
        this.targetSocietyId = targetSocietyId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Integer getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Integer societyId) {
        this.societyId = societyId;
    }

    public Integer getReceiverAdminId() {
        return receiverAdminId;
    }

    public void setReceiverAdminId(Integer receiverAdminId) {
        this.receiverAdminId = receiverAdminId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
