package com.example.studyapp.recycle;

// recycler view에 사용될 class
// recycler view에 들어가는 flagment xml에서 수정하고 싶은 부분을 담아두는 class
// PlanFragment에서 ArrayList를 사용해 PlanData를 모아둔 후
// 사용자가 Plan화면으로 넘어 올 떄  PlanAdapter의 notifyDataSetChanged() 메소드를 사용해
// 각 사용자의 리스트를 동적으로 변화한다.

public class PlanData {
    // 넣을 그림의 ID
    private int iv_profile;

    // subject name
    private String tv_name;

    // 시간을 저장하는 string
    private String tv_content;

    // n번째에 오는 색상
    // 나중에 만들수록 색상이 짙어짐
    private String iv_color;


    public PlanData(int iv_profile, String tv_name, String tv_content, String iv_color) {
        this.iv_profile = iv_profile;
        this.tv_name = tv_name;
        this.tv_content = tv_content;
        this.iv_color = iv_color;
    }


    public int getIv_profile() {
        return iv_profile;
    }

    public void setIv_profile(int iv_profile) {
        this.iv_profile = iv_profile;
    }

    public String getTv_name() {
        return tv_name;
    }

    public void setTv_name(String tv_name) {
        this.tv_name = tv_name;
    }

    public String getTv_content() {
        return tv_content;
    }

    public void setTv_content(String tv_content) {
        this.tv_content = tv_content;
    }

    public String getIv_color() {
        return iv_color;
    }

    public void setTv_color(String iv_color) {
        this.iv_color = iv_color;
    }
}