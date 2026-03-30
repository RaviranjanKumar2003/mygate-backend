package com.example.demo.Entities;

import com.example.demo.Enums.StaffLevel;
import com.example.demo.Enums.StaffStatus;
import com.example.demo.Enums.StaffType;
import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffLevel staffLevel;         // FLAT, FLOOR,BUILDING, SOCIETY

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffType staffType;           // MAID, DRIVER, COOK, SECURITY, ACCOUNTANT,CLEANER

    private String dutyTiming;

    private double salary;


// GETTERS & SETTERS


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StaffType getStaffType() {
        return staffType;
    }

    public void setStaffType(StaffType staffType) {
        this.staffType = staffType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StaffLevel getStaffLevel() {
        return staffLevel;
    }

    public void setStaffLevel(StaffLevel staffLevel) {
        this.staffLevel = staffLevel;
    }

    public String getDutyTiming() {
        return dutyTiming;
    }

    public void setDutyTiming(String dutyTiming) {
        this.dutyTiming = dutyTiming;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

}
