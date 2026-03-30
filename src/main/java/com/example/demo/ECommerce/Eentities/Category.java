package com.example.demo.ECommerce.Eentities;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Long societyId; // category belongs to which society


// CONSTRUCTOR

    public Category() {}

    public Category(Long id, String name, String description, Long societyId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.societyId = societyId;
    }

// GETTERS & SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getSocietyId() { return societyId; }
    public void setSocietyId(Long societyId) { this.societyId = societyId; }
}