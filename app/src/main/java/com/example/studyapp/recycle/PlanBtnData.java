package com.example.studyapp.recycle;

public class PlanBtnData {
    private String btnon;
    private String btnoff;


    public PlanBtnData(String tv_name, String tv_content) {
        this.btnon = tv_name;
        this.btnoff = tv_content;
    }


    public String getBtnon() {
        return btnon;
    }

    public void setBtnon(String btnon) {
        this.btnon = btnon;
    }

    public String getBtnoff() {
        return btnoff;
    }

    public void setBtnoff(String tv_content) {
        this.btnoff = btnoff;
    }
}