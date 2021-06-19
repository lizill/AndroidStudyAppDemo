package com.example.studyapp.recycle;

// recycler view에 사용될 class
// recycler view에 들어가는 flagment xml에서 수정하고 싶은 부분을 담아두는 class
// PlanFragment에서 ArrayList를 사용해 PlanData를 모아둔 후
// 사용자가 Plan화면으로 넘어 올 떄  PlanAdapter의 notifyDataSetChanged() 메소드를 사용해
// 각 사용자의 리스트를 동적으로 변화한다.

public class RankData {
    private String tv_rank_pf;
    private String tv_rank_name;
    private String tv_rank_time;
    private int pb_rank_progress;
    private int iv_rank_studying;


    public RankData(String tv_rank_pf, String tv_rank_name, String tv_rank_time,
                            int pb_rank_progress, int iv_rank_studying) {
        this.tv_rank_pf = tv_rank_pf;
        this.tv_rank_name = tv_rank_name;
        this.tv_rank_time = tv_rank_time;
        this.pb_rank_progress = pb_rank_progress;
        this.iv_rank_studying = iv_rank_studying;
    }

    public String getTv_rank_pf() {
        return tv_rank_pf;
    }

    public void setTv_rank_pf(String tv_rank_pf) {
        this.tv_rank_pf = tv_rank_pf;
    }

    public String getTv_rank_name() {
        return tv_rank_name;
    }

    public void setTv_rank_name(String tv_rank_name) {
        this.tv_rank_name = tv_rank_name;
    }

    public String getTv_rank_time() {
        return tv_rank_time;
    }

    public void setTv_rank_time(String tv_rank_time) {
        this.tv_rank_time = tv_rank_time;
    }

    public int getPb_rank_progress() {
        return pb_rank_progress;
    }

    public void setPb_rank_progress(int pb_rank_progress) {
        this.pb_rank_progress = pb_rank_progress;
    }

    public int getIv_rank_studying() {
        return iv_rank_studying;
    }

    public void setIv_rank_studying(int iv_rank_studying) {
        this.iv_rank_studying = iv_rank_studying;
    }
}