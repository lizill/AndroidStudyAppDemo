package com.example.studyapp.recycle;

import android.text.Editable;

public class HomeData {
    private int iv_profile;
    private Editable tv_name;
    private String tv_content;


    public HomeData(int iv_profile, Editable tv_name) {
        this.iv_profile = iv_profile;
        this.tv_name = tv_name;

    }
// 이 클래스가, 리사이클러뷰에 활용될 xml파일의
    /*
        id를 가져와서 저장할 클래스쯤?
     */

    public int getIv_profile() {
        return iv_profile;
    }

    public Editable getTv_name() {
        return tv_name;
    }


    public void setIv_profile(int iv_profile) {
        this.iv_profile = iv_profile;
    }

    public void setTv_name(Editable tv_name) {
        this.tv_name = tv_name;
    }


}