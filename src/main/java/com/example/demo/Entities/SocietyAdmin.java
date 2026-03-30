package com.example.demo.Entities;

import com.example.demo.Enums.SocietyStatus;
import com.example.demo.Enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "society_admins")
public class SocietyAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ================= BASIC DETAILS =================
    private String adminName;

    @Column(unique = true, nullable = false)
    private String adminEmail;

    private String adminPassword;

    private String mobileNumber;

    private String imageURL;

    // ================= ROLE =================
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.SOCIETY_ADMIN;

    // ================= STATUS =================
    @Enumerated(EnumType.STRING)
    private SocietyStatus societyAdminStatus = SocietyStatus.ACTIVE;

    // ================= RELATION =================
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;

    // ================= DEFAULT STATUS =================
    @PrePersist
    public void prePersist() {
        if (this.societyAdminStatus == null) {
            this.societyAdminStatus = SocietyStatus.ACTIVE;
        }
        if (this.role == null) {
            this.role = UserRole.SOCIETY_ADMIN;
        }
    }

// ================= GETTERS & SETTERS =


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public SocietyStatus getSocietyAdminStatus() {
        return societyAdminStatus;
    }

    public void setSocietyAdminStatus(SocietyStatus societyAdminStatus) {
        this.societyAdminStatus = societyAdminStatus;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}
