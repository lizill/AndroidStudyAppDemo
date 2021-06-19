package com.example.studyapp.ui.group;

public class MemberData {
    private String roomName;
    private String userName;
    private String totalTime;
    private String online;

    public MemberData(String roomName, String userName, String totalTime, String online) {
        this.roomName = roomName;
        this.userName = userName;
        this.totalTime = totalTime;
        this.online = online;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserId() {
        return userName;
    }

    public void setUserId(String userId) {
        this.userName = userId;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}