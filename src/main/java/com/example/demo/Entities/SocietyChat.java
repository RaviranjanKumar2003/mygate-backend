package com.example.demo.Entities;

import com.example.demo.Enums.NormalUserType;
import com.example.demo.Enums.UserRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class SocietyChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer societyId;

    private Integer senderId;

    private String senderName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private NormalUserType userType;   // OWNER / TENANT / SOCIETY_ADMIN

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime createdAt;

    private boolean deletedForEveryone = false;
    private boolean deletedForMe = false;

    // ✅ Users who deleted the message for themselves
    @ElementCollection
    private List<Integer> deletedForUsers = new ArrayList<>();


    // reply feature
    private Integer replyMessageId;

    // message type
    private String messageType;
    // TEXT / IMAGE / FILE

    private String fileUrl;


// GETTERS & SETTERS


    public List<Integer> getDeletedForUsers() {
        return deletedForUsers;
    }

    public void setDeletedForUsers(List<Integer> deletedForUsers) {
        this.deletedForUsers = deletedForUsers;
    }

    public Integer getReplyMessageId() {
        return replyMessageId;
    }

    public void setReplyMessageId(Integer replyMessageId) {
        this.replyMessageId = replyMessageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public boolean isDeletedForEveryone() {
        return deletedForEveryone;
    }

    public void setDeletedForEveryone(boolean deletedForEveryone) {
        this.deletedForEveryone = deletedForEveryone;
    }

    public boolean isDeletedForMe() {
        return deletedForMe;
    }

    public void setDeletedForMe(boolean deletedForMe) {
        this.deletedForMe = deletedForMe;
    }

    public NormalUserType getUserType() {
        return userType;
    }

    public void setUserType(NormalUserType userType) {
        this.userType = userType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Integer societyId) {
        this.societyId = societyId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}