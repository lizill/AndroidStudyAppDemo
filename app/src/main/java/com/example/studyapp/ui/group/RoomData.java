package com.example.studyapp.ui.group;

public class RoomData {

    private String userID;
    private String roomName;
    private long sendTime;

    public RoomData(String userID, String roomName, long sendTime) {
        this.userID = userID;
        this.roomName = roomName;
        this.sendTime = sendTime;
    }

    public String getUserId() {
        return userID;
    }

    public void setUserId(String userID) {
        this.userID = userID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
