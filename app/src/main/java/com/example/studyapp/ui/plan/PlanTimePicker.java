package com.example.studyapp.ui.plan;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.studyapp.R;

import java.util.Calendar;
import java.util.zip.DataFormatException;

public class PlanTimePicker extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener{

    /*
     * PlanSetPage에서 버튼을 눌렀을 때 시작되는 이벤트 이 이벤트에서 timePicker를 설정함
     * timepicker에는 현재 시간을 가져와서 몇 시 몇 분인지 선택 할 수 있게 해줌
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar mCal = Calendar.getInstance();
        int hour = mCal.get(Calendar.HOUR_OF_DAY);
        int min = mCal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, this,
                hour, min, DateFormat.is24HourFormat(getContext()));
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return timePickerDialog;
    }

    /*
     * PlanSetPage에서 시작인지 끝인지 어느 버튼을 눌렀는지에 따라
     * 이벤트가 다르게 시작함
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // System.out.println(hourCal(hourOfDay, minute, PlanSetPage.touch));
        if(PlanSetPage.whoTouch){
            PlanSetPage.st_btn.setText(hourCal(hourOfDay, minute, true));
        }else{
            PlanSetPage.en_btn.setText(hourCal(hourOfDay, minute, false));
        }
        PlanSetPage.en_txt.setText("종료시간: "+timeCal()+" 분 동안");
    }

    /*
     * 시간을 설정하는 method 가져온 시간이 24시간으로 가져오는데 그 값을 우리가 알아먹기 쉽게
     * 오전 00:00:00, 오후 00:00:00이런식으로 바꿔줌
     */

    public static String hourCal(int hour, int min, boolean who){
        String noon ="";
        String result = "";
        if (min>=60){
            hour ++;
            min -=60;
        }
        if(who){
            PlanSetPage.st_hour=hour;
            PlanSetPage.st_min=min;
        }else{
            PlanSetPage.en_hour=hour;
            PlanSetPage.en_min=min;
        }
        if(hour>=12)
            noon = "오후";
        else
            noon = "오전";
        if(hour==24){
            hour =12;
            noon = "오전";
        }else if (hour>12){
            hour -=12;
        }
        result = who?" 부터":" 까지";
        result = noon+" "+hour+" : "+min+result;

        return result;
    }
    public static String timeCal(){
        int cal_hour=PlanSetPage.en_hour-PlanSetPage.st_hour;
        int cal_min=PlanSetPage.en_min-PlanSetPage.st_min;
        if(cal_min<0){
            cal_hour--;
            cal_min+=60;
        }
        if(cal_hour<0){
            cal_hour+=24;
        }
        String result = cal_hour*60+cal_min+"";
        return result;
    }
}
