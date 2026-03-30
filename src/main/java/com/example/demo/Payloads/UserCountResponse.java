package com.example.demo.Payloads;

import java.util.Map;

public class UserCountResponse {

    private long totalUsers;
    private Map<String, Long> roleWiseCount;
    private Map<String, Long> statusWiseCount;

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Map<String, Long> getRoleWiseCount() {
        return roleWiseCount;
    }

    public void setRoleWiseCount(Map<String, Long> roleWiseCount) {
        this.roleWiseCount = roleWiseCount;
    }

    public Map<String, Long> getStatusWiseCount() {
        return statusWiseCount;
    }

    public void setStatusWiseCount(Map<String, Long> statusWiseCount) {
        this.statusWiseCount = statusWiseCount;
    }

}
