package com.example.demo.Entities;

import com.example.demo.Enums.NoticeCreatedByRole;
import com.example.demo.Enums.NoticePriority;
import com.example.demo.Enums.TargetAudience;
import com.example.demo.Enums.NoticeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /* ================= BASIC ================= */

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    /* ================= SENDER ================= */

    @Column(nullable = false)
    private Integer createdById;

    private Integer recievedById;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeCreatedByRole createdByRole;         // Notice likhne wala kaun hai? (SuperAdmin, SocietyAdmin)

    @Enumerated(EnumType.STRING)
    private NoticeCreatedByRole recievedByRole;


    @Column(nullable = false)
    private String createdByName;

    private String receivedByName;

    /* ================= TARGET ================= */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetAudience targetRole;              // Notice kaun-kaun dekh sakta hai

    // NULL => Global (Super Admin → Society Admin)
    @Column
    private Integer targetSocietyId;

    /* ================= META ================= */

    @Enumerated(EnumType.STRING)
    private NoticePriority priority;

    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    private String attachmentUrl;

    private Boolean isActive = true;

    private LocalDate expiryDate;

    /* ================= AUDIT ================= */

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    public Notice() {

    }


    /* ================= JPA LIFECYCLE ================= */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


// GETTERS & SETTERS & CONSTRUCTOR

    public Notice(int id, String title, String message, Integer createdById, Integer recievedById,
                  NoticeCreatedByRole createdByRole, NoticeCreatedByRole recievedByRole, String createdByName, String receivedByName, TargetAudience targetRole,
                  Integer targetSocietyId, NoticePriority priority, NoticeType noticeType,
                  String attachmentUrl, Boolean isActive, LocalDate expiryDate, LocalDateTime createdAt,
                  LocalDateTime updatedAt) {

        this.id = id;
        this.title = title;
        this.message = message;
        this.createdById = createdById;
        this.recievedById = recievedById;
        this.createdByRole = createdByRole;
        this.recievedByRole = recievedByRole;
        this.createdByName = createdByName;
        this.receivedByName = receivedByName;
        this.targetRole = targetRole;
        this.targetSocietyId = targetSocietyId;
        this.priority = priority;
        this.noticeType = noticeType;
        this.attachmentUrl = attachmentUrl;
        this.isActive = isActive;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


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

    public String getReceivedByName() {
        return receivedByName;
    }

    public void setReceivedByName(String receivedByName) {
        this.receivedByName = receivedByName;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
