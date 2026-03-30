package com.example.demo.Payloads;

import com.example.demo.Entities.Society;
import com.example.demo.Enums.NoticeCreatedByRole;
import com.example.demo.Enums.NoticePriority;
import com.example.demo.Enums.TargetAudience;
import com.example.demo.Enums.NoticeType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class NoticeDto {

    private int id;

    private String title;
    private String message;

    private Integer createdById;
    private Integer recievedById;

    private NoticeCreatedByRole createdByRole;
    private NoticeCreatedByRole recievedByRole;

    private String createdByName;
    private String recievedByName;

    private TargetAudience targetRole;
    private Integer targetSocietyId;

    private NoticePriority priority;
    private NoticeType noticeType;

    private String attachmentUrl;
    private Boolean isActive;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Society society;




// GETTERS & SETTERS


    public Integer getRecievedById() {
        return recievedById;
    }

    public void setRecievedById(Integer recievedById) {
        this.recievedById = recievedById;
    }

    public NoticeCreatedByRole getRecievedByRole() {
        return recievedByRole;
    }

    public void setRecievedByRole(NoticeCreatedByRole recievedByRole) {
        this.recievedByRole = recievedByRole;
    }

    public String getRecievedByName() {
        return recievedByName;
    }

    public void setRecievedByName(String recievedByName) {
        this.recievedByName = recievedByName;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public NoticeCreatedByRole getCreatedByRole() {
        return createdByRole;
    }

    public void setCreatedByRole(NoticeCreatedByRole createdByRole) {
        this.createdByRole = createdByRole;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public TargetAudience getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(TargetAudience targetRole) {
        this.targetRole = targetRole;
    }

    public Integer getTargetSocietyId() {
        return targetSocietyId;
    }

    public void setTargetSocietyId(Integer targetSocietyId) {
        this.targetSocietyId = targetSocietyId;
    }

    public NoticePriority getPriority() {
        return priority;
    }

    public void setPriority(NoticePriority priority) {
        this.priority = priority;
    }

    public NoticeType getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(NoticeType noticeType) {
        this.noticeType = noticeType;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
