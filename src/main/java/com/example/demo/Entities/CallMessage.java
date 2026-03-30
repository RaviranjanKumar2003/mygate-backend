package com.example.demo.Entities;

public class CallMessage {

    private String roomName;
    private String callerName;
    private String type;

// getters setters

    // Default constructor (jaruri)
    public CallMessage() {}

    // Parameterized constructor (optional)
    public CallMessage(String roomName, String callerName, String type) {
        this.roomName = roomName;
        this.callerName = callerName;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }
}