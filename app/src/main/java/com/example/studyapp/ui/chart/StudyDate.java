package com.example.studyapp.ui.chart;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.example.studyapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;
import java.util.Calendar;

class StudyDate implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
    private CalendarDay date;
    private String studyTime;
    private String studyDate;
    private ArrayList<Drawable> drawables = new ArrayList<>();
    public StudyDate (Activity context, String studyTime, String studyDate) {
        drawables.add(context.getResources().getDrawable(R.drawable.onstudy));
        drawables.add(context.getResources().getDrawable(R.drawable.onstudy2));
        drawables.add(context.getResources().getDrawable(R.drawable.onstudy3));
        drawables.add(context.getResources().getDrawable(R.drawable.onstudy4));

        this.studyTime = studyTime;
        this.studyDate = studyDate;

        date = setDays(divDate(studyDate));

        System.out.println(date.getYear() + "-" + date.getMonth() + "," + date.getDay() + "," + date.getDay());
    }
    // set year month day
    public CalendarDay setDays(String [] s){
        int year = Integer.valueOf(s[0]);
        int month = Integer.valueOf(s[1]) - 1;
        int day = Integer.valueOf(s[2]);
        CalendarDay tmp = CalendarDay.from(year,month,day);
        return tmp;
    }
    // String "2xxx-xx-xx" -> array
    public String []  divDate(String studyDate){
        String [] s = studyDate.split("-");
        for(int i = 0; i < s.length; i++){
        }
        return s;
    }
    // time -> second 로 구성 되어있음 / time ->  hour 로 바꿈
    public double getHour(String t){
        double time = Integer.valueOf(t);
        if(time == 0) return 0;
        return time / 3600;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }
    @Override
    public void decorate(DayViewFacade view) {
        double tmp = getHour(studyTime);
        //공부시간에 따라서 색깔 변화 준다.
        if(tmp > 0 && tmp < 2){
            view.setSelectionDrawable(drawables.get(0));
        }else if(tmp >= 2 && tmp < 4){
            view.setSelectionDrawable(drawables.get(1));
        }else if(tmp >= 4 && tmp < 7){
            view.setSelectionDrawable(drawables.get(2));
        }else if(tmp >= 7){
            view.setSelectionDrawable(drawables.get(3));
        }
    }
}
