package com.example.studyapp.ui.group;

public class Group {

    String group;
    String contents;
    String peopleCount;
    String category;
    String goalTime;
    String master;
    String startDate;
    String memberLimit;

    public Group(String group, String contents, String peopleCount, String category, String goalTime, String master, String startDate, String memberLimit) {
        this.group = group;
        this.contents = contents;
        this.peopleCount = peopleCount;
        this.category = category;
        this.goalTime = goalTime;
        this.master = master;
        this.startDate = startDate;
        this.memberLimit = memberLimit;
    }

    public String getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(String memberLimit) {
        this.memberLimit = memberLimit;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGoalTime() {
        return goalTime + "시간";
    }

    public void setGoalTime(String goalTime) {
        this.goalTime = goalTime;
    }

    public void setPeopleCount(String peopleCount) {
        this.peopleCount = peopleCount;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getContents() {
        return contents;
    }

    public String getPeopleCount() {
        return peopleCount;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }
}
