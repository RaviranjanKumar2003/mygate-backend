package com.example.demo.Payloads;

public class FloorSummaryDto {

    private long totalFloor;
    private long activeFloor;
    private long inactiveFloor;

    public FloorSummaryDto(long totalFloor, long activeFloor, long inactiveFloor) {
        this.totalFloor = totalFloor;
        this.activeFloor = activeFloor;
        this.inactiveFloor = inactiveFloor;
    }

    public long getTotalFloor() {
        return totalFloor;
    }

    public void setTotalFloor(long totalFloor) {
        this.totalFloor = totalFloor;
    }

    public long getActiveFloor() {
        return activeFloor;
    }

    public void setActiveFloor(long activeFloor) {
        this.activeFloor = activeFloor;
    }

    public long getInactiveFloor() {
        return inactiveFloor;
    }

    public void setInactiveFloor(long inactiveFloor) {
        this.inactiveFloor = inactiveFloor;
    }
}
