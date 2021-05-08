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
    //그냥 숫자로 오기때문에 그걸 hh:mm:ss 단위로 만듦
    private String makeVisibleTime(String study){
        String [] ar = {"0","0","0","0","0","0"};
        int idx = ar.length -1;
        for(int i = study.length()-1; i >= 0; i--){
            char c = study.charAt(i);
            ar[idx--] = String.valueOf(c);
        }
        String time = "";
        for(int i = 0; i < ar.length; i++){
            time += ar[i];

            if(i == 1 || i == 3){
                time += ":";
            }
        }
        return time;
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

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }
    @Override
    public void decorate(DayViewFacade view) {
        String [] t = makeVisibleTime(studyTime).split(":");
        System.out.println("****************************");
        System.out.println(t[0]);
        System.out.println("****************************");
        int tmp = Integer.parseInt(t[0]);
        System.out.println(tmp);
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
