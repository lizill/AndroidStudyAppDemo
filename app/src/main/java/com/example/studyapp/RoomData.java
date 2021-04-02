package com.example.studyapp;

public class RoomData {
    private String userID;
    private String roomName;

    public RoomData(String userID, String roomName) {
        this.userID = userID;
        this.roomName = roomName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
