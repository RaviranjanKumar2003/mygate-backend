package com.example.demo.Payloads;

import com.example.demo.Enums.VisitorLogStatus;

import java.time.LocalDateTime;

public class VisitorLogDto {

    private Long id;

    private Integer visitorId;

    private Integer flatId;

    private LocalDateTime inTime;

    private LocalDateTime outTime;

    private VisitorLogStatus visitorLogStatus;

    // GETTERS & SETTERS


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Integer visitorId) {
        this.visitorId = visitorId;
    }

    public Integer getFlatId() {
        return flatId;
    }

    public void setFlatId(Integer flatId) {
        this.flatId = flatId;
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
