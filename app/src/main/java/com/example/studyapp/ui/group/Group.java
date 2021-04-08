package com.example.studyapp.ui.group;

public class Group {

    String group;
    String contents;
    String peopleCount;

    public Group(String group, String contents, String peopleCount) {
        this.group = group;
        this.contents = contents;
        this.peopleCount = peopleCount;
    }

    public void setPeopleCount(String peopleCount) {
        this.peopleCount = peopleCount;
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
        return "멤버 수: " + peopleCount + "명";
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
