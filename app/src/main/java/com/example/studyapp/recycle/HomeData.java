package com.example.studyapp.recycle;

public class HomeData {
    private String tv_home_name;
    private String subject_time;

    public HomeData(String tv_name,String subject_time) {
        this.subject_time = subject_time;
        this.tv_home_name = tv_name;

    }

    public String getSubject_time() {
        return subject_time;
    }


    public void setSubject_time(String subject_time) {this.subject_time = subject_time;}


    public String getTv_name() {
        return tv_home_name;
    }


    public void setTv_name(String tv_home_name) {
        this.tv_home_name = tv_home_name;
    }


}