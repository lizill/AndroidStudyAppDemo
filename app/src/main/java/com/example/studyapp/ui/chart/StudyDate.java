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
    }
    // set year month day
    public CalendarDay setDays(String [] s){
        int year = Integer.valueOf(s[0]);
        int month = Integer.valueOf(s[1]) - 1;
        int day = Integer.valueOf(s[2]);
        CalendarDay setDay = CalendarDay.from(year,month,day);
        return setDay;
    }
    // String "2xxx-xx-xx" -> array
    public String []  divDate(String studyDate){
        String [] s = studyDate.split("-");
        for(int i = 0; i < s.length; i++){
        }
        return s;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }
    @Override
    public void decorate(DayViewFacade view) {
        String [] tmp = studyTime.split(":");
        int hour = Integer.parseInt(tmp[0]);
        int minute = Integer.parseInt(tmp[1]);
        int second = Integer.parseInt(tmp[2]);
        //공부시간에 따라서 색깔 변화 준다.
        if(hour < 2 && (second > 0 || minute > 0)){
            view.setBackgroundDrawable(drawables.get(0));
        }else if(hour >= 2 && hour < 4){
            view.setBackgroundDrawable(drawables.get(1));
        }else if(hour >= 4 && hour < 7){
            view.setBackgroundDrawable(drawables.get(2));
        }else if(hour >= 7){
            view.setBackgroundDrawable(drawables.get(3));
        }
    }
}
