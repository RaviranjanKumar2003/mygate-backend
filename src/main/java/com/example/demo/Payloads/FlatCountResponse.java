package com.example.demo.Payloads;

import com.example.demo.Enums.FlatStatus;

import java.util.Map;

public class FlatCountResponse {

    private Integer totalFlats;

    private Map<FlatStatus, Integer> statusWiseCount;



    // GETTERS & SETTERS

    public Integer getTotalFlats() {
        return totalFlats;
    }

    public void setTotalFlats(Integer totalFlats) {
        this.totalFlats = totalFlats;
    }

    public Map<FlatStatus, Integer> getStatusWiseCount() {
        return statusWiseCount;
    }

    public void setStatusWiseCount(Map<FlatStatus, Integer> statusWiseCount) {
        this.statusWiseCount = statusWiseCount;
    }
}
