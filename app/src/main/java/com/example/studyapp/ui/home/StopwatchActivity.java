package com.example.studyapp.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.HomeActivity;
import com.example.studyapp.R;
import com.example.studyapp.recycle.HomeAdapter;
import com.example.studyapp.recycle.HomeData;
import com.example.studyapp.ui.chart.Env;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import com.example.studyapp.FirstActivity;

public class StopwatchActivity extends AppCompatActivity {

    private TextView textView ;
    private ImageButton back_btn;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    private Handler handler;
    private int Seconds, Minutes, MilliSeconds, Hours, tmp, t, hour, min, sec, allhour,allmin,allsec,at,ahour,amin,asec;
    private String today,userID,start,end;
    public static String subject;
    private boolean isFirst = false;

    //현재 날짜 불러오기
    private TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    DateFormat timeFormat = new SimpleDateFormat("HH mm ss", Locale.KOREA);


    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        textView = (TextView)findViewById(R.id.tv_subject_timer);

        userID = FirstActivity.userInfo.getString("userId", null);


        dateFormat.setTimeZone(tz);
        timeFormat.setTimeZone(tz);

        today = dateFormat.format(new Date());
        System.out.println(today);
        start = timeFormat.format(new Date());
        System.out.println(start);


        //과목정보 불러오기

        Intent intent = getIntent();
        subject = intent.getStringExtra("subject");


        //Volley Queue  & request json
        requestQueue = Volley.newRequestQueue(getApplication());
        searchStudyTimeToday();

        back_btn = (ImageButton)findViewById(R.id.back_btn);

        handler = new Handler() ;
        StartTime = SystemClock.uptimeMillis();


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StopwatchActivity.this, HomeActivity.class);
                startActivity(intent);


                handler.removeCallbacks(runnable);

                end = timeFormat.format(new Date());
                //총 공부시간
                System.out.println(end);

                BeginEndData();
                InsertData();
            }
        });
    }
    private void BeginEndData(){
        String url = Env.BeginEndURL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //json object >> {response:[{key : value}, {.....
                    JSONObject jsonObject = new JSONObject(response);
                    String res = jsonObject.getString("success");
                    System.out.println("BeginEndData save");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })  {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("study_date", today);
                params.put("study_subject", subject);
                params.put("study_start", start.replace(" ", ":"));
                params.put("study_end", end.replace(" ", ":"));

                System.out.println(userID + " " + today + " " + subject + " " + start.replace(" ", ":") + end.replace(" ", ":"));
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }
    private void InsertData(){
        String url = Env.SaveURL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {
                        //json object >> {response:[{key : value}, {.....
                        JSONObject jsonObject = new JSONObject(response);
                        String res = jsonObject.getString("success");
                        Toast.makeText(StopwatchActivity.this, res, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })  {
            @Override
            protected Map<String, String> getParams() {
                int updateTime = (hour*3600+min*60+sec)-(Hours*3600+Minutes*60+Seconds);
                int newHour = updateTime /3600;
                updateTime %=3600;
                int newMin = updateTime/60;
                updateTime %=60;
                int newSec = updateTime;
                String newTime = String.format("%02d", newHour) + ":" + String.format("%02d", newMin) + ":" + String.format("%02d", newSec);
                System.out.println(newTime);
                String time = textView.getText().toString().replace(":","");
                Map<String,String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("study_date", today);
                params.put("study_subject", subject);
                params.put("study_time", newTime);
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }
    private void UpdateData(){
        String url = Env.ReSaveURL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //json object >> {response:[{key : value}, {.....
                    JSONObject jsonObject = new JSONObject(response);
                    String res = jsonObject.getString("success");
                    Toast.makeText(StopwatchActivity.this, res, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })  {
            @Override
            protected Map<String, String> getParams() {
                int updateTime = (hour*3600+min*60+sec)-(Hours*3600+Minutes*60+Seconds);
                int newHour = updateTime /3600;
                updateTime %=3600;
                int newMin = updateTime/60;
                updateTime %=60;
                int newSec = updateTime;
                String newTime = String.format("%02d", newHour) + ":" + String.format("%02d", newMin) + ":" + String.format("%02d", newSec);
//                private int Seconds, Minutes, MilliSeconds, Hours, tmp, t, hour, min, sec, allhour,allmin,allsec,at,ahour,amin,asec;
                String time = textView.getText().toString().replace(":","");
                System.out.println(userID + " " + today + " " + subject + " " + time);
                Map<String,String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("study_date", today);
                params.put("study_subject", subject);
                params.put("study_time", newTime);
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }


    private void searchStudyTimeToday(){
        String url = String.format(Env.fetchURL, userID,today,subject);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            //json object >> {response:[{key : value}, {.....
                            JSONObject jsonObject = new JSONObject(response);

                            //object start name : response  >>>>> array
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject studyObject = jsonArray.getJSONObject(0);
                            String studyTime = studyObject.getString("study_time");
                            int totalTime = 0;
                            try {
                                totalTime = Integer.parseInt(jsonArray.getJSONObject(1).getString("study_total_time"));
                            }catch(Exception error){

                            }

                            allhour = totalTime/3600;
                            allmin = (totalTime%3600)/60;
                            allsec = (totalTime%3600)%60;
                            String formatedTime = String.format("%02d", allhour) + ":" + String.format("%02d", allmin) + ":" + String.format("%02d", allsec);
                            TextView allTime = findViewById(R.id.tv_total_timer);
                            allTime.setText(formatedTime);
                            System.out.println(formatedTime);
//                            String.format("%02d", totalTime_hour) + ":" + String.format("%02d", totalTime_min) + ":" + String.format("%02d", totalTime_sec)
                            if(!studyTime.equals("null")){
                                convertToTime(studyTime);
                            }else{
                                isFirst = true;
                            }
                            handler.postDelayed(runnable, 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        requestQueue.add(request);
    }
    public void convertToTime(String studyTime){
        String [] time = studyTime.split(":");
        Hours = Integer.parseInt(time[0]);
        Minutes = Integer.parseInt(time[1]);
        Seconds = Integer.parseInt(time[2]);
    }
    public Runnable runnable = new Runnable() {
        //인덱스
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            //시간의 흐름
            tmp = (int)(UpdateTime / 1000);
            t = Seconds + tmp;
             at = allsec + tmp;
            hour = Hours + t / 3600;
            ahour = allhour + at/3600;
            t %= 3600;
            at%=3600;
            min = Minutes + t / 60;
            amin = allmin+at/60;
            t %= 60;
            at %= 60;
            sec = t;
            asec = at;
            MilliSeconds = (int) (UpdateTime % 1000);


            TextView allTime = findViewById(R.id.tv_total_timer);
//            String oldAllTime = allTime.getText().toString();
//            String[] time = oldAllTime.split(":");
//            int oldhour = Integer.parseInt(time[0]);
//            int oldmin = Integer.parseInt(time[1]);
//            int oldsec = Integer.parseInt(time[2]);
//            oldsec+=(tmp%3600)%60;
//            if(oldsec>=60){
//                oldsec = 0;
//                oldmin++;
//                if(oldmin>=60){
//                    oldmin=0;
//                    oldhour++;
//                }
//            }
            String formatedTime = String.format("%02d", ahour) + ":" + String.format("%02d", amin) + ":" + String.format("%02d", asec);
            allTime.setText(formatedTime);



            //String format을 통한 시간 대입
            textView.setText(String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec));
            handler.postDelayed(this, 0);
        }
    };
}