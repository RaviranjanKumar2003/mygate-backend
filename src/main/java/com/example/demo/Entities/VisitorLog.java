package com.example.demo.Entities;


import com.example.demo.Enums.VisitorLogStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class VisitorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Visitor visitor;

    @ManyToOne
    private Flat flat;

    private LocalDateTime inTime;

    private LocalDateTime outTime;

    @Enumerated(EnumType.STRING)
    private VisitorLogStatus visitorLogStatus=VisitorLogStatus.PENDING;

    // GETTERS & SETTERS


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public LocalDateTime getInTime() {
        return inTime;
    }

    public void setInTime(LocalDateTime inTime) {
        this.inTime = inTime;
    }

    public LocalDateTime getOutTime() {
        return outTime;
    }

    public void setOutTime(LocalDateTime outTime) {
        this.outTime = outTime;
    }

    public VisitorLogStatus getVisitorLogStatus() {
        return visitorLogStatus;
    }

    public void setVisitorLogStatus(VisitorLogStatus visitorLogStatus) {
        this.visitorLogStatus = visitorLogStatus;
    }


}
