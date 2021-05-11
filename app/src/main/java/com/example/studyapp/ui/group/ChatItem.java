package com.example.studyapp.ui.group;

public class ChatItem {
    private String name;
    private String content;
    private String sendTime;
    private int viewType;

    @Override
    public String toString() {
        return "ChatItem{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", viewType=" + viewType +
                '}';
    }

    public ChatItem(String name, String content, String sendTime, int viewType) {
        this.name = name;
        this.content = content;
        this.sendTime = sendTime;
        this.viewType = viewType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
