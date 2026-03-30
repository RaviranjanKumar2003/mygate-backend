package com.example.demo.SuperAdminFolder;

import com.example.demo.Enums.UserRole;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "super_admins")
public class SuperAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String mobileNumber;

    @Column(nullable = false)
    private String password;

    private String imageURL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole=UserRole.SUPER_ADMIN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuperAdminStatus superAdminStatus = SuperAdminStatus.ACTIVE;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Automatically set createdAt
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ================= GETTERS & SETTERS =================

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public SuperAdminStatus getSuperAdminStatus() {
        return superAdminStatus;
    }

    public void setSuperAdminStatus(SuperAdminStatus superAdminStatus) {
        this.superAdminStatus = superAdminStatus;
    }
}
