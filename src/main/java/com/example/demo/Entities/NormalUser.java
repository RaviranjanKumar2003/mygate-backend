package com.example.demo.Entities;

import com.example.demo.Enums.NormalUserType;
import jakarta.persistence.*;

@Entity
@Table(
        name = "normal_users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"flat_id"})
        }
)
public class NormalUser {

    @Id
    private Integer id;

    private String email;

    private String password;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @ManyToOne
    @JoinColumn(name = "flat_id", nullable = false, unique = true)
    private Flat flat;

    @Enumerated(EnumType.STRING)
    private NormalUserType normalUserType; // TENANT / OWNER


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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public NormalUserType getNormalUserType() {
        return normalUserType;
    }

    public void setNormalUserType(NormalUserType normalUserType) {
        this.normalUserType = normalUserType;
    }
}
