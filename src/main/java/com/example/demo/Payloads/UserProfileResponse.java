package com.example.demo.Payloads;

public class UserProfileResponse {

    private UserDto user;
    private StaffDto staff; // null if not staff

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }
}
