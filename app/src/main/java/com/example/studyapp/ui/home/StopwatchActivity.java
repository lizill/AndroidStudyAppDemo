package com.example.studyapp.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
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
import com.google.gson.Gson;

import static com.example.studyapp.ui.home.HomeFragment.mSocket;

public class StopwatchActivity extends AppCompatActivity {

    private TextView tv_total_timer, tv_subject, tv_subject_timer;
    private ImageButton back_btn;
    private long MillisecondTime, StartTime, leaveTime, termTime = 0L ;
    private Handler handler;
    private int Seconds, Minutes, Hours, tmp, t, t2, hour, min, sec, totalHour, totalMin, totalSec, gapOfSecond,gapOfMinute,studyTimeSec,studyTimeTotalSec;
    private String today,userID,start,end,confirmToday;

    private boolean isFirst,isTomorrow;
    private boolean isActiveOn = true;

    public static String subject = "";
    static boolean isStart;

    //현재 날짜 불러오기
    private TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    DateFormat timeFormat = new SimpleDateFormat("HH mm ss", Locale.KOREA);

    private RequestQueue requestQueue;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isStart = true;
        setContentView(R.layout.activity_stopwatch);
        tv_total_timer = (TextView)findViewById(R.id.tv_total_timer);
        tv_subject_timer = (TextView)findViewById(R.id.tv_subject_timer);

        userID = FirstActivity.userInfo.getString("userId", null);

        dateFormat.setTimeZone(tz);
        timeFormat.setTimeZone(tz);
        today = dateFormat.format(new Date());

        confirmToday =today.split("-")[2];


        //과목정보 불러오기
        Intent intent = getIntent();

        subject = intent.getStringExtra("subject");
        tv_subject = findViewById(R.id.tv_subject);
        tv_subject.setText(subject);


        //Volley Queue  & request json
        requestQueue = Volley.newRequestQueue(getApplication());
        searchStudyTimeToday();

        back_btn = (ImageButton)findViewById(R.id.back_btn);

        handler = new Handler() ;
        StartTime = SystemClock.uptimeMillis();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = false;
                Intent intent = new Intent(StopwatchActivity.this, HomeActivity.class);
                startActivity(intent);
                handler.removeCallbacks(runnable);
            }
        });
    }
    private void InsertData(){
        String url = Env.save2URL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {
                        //json object >> {response:[{key : value}, {.....
                        JSONObject jsonObject = new JSONObject(response);
                        String res = jsonObject.getString("success");
                        System.out.println("start  " + start + "  end"  + end);
//                        BeginEndData();

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
                String time = tv_subject_timer.getText().toString().replace(":","");
                end = timeFormat.format(new Date());
                Map<String,String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("study_date", today);
                params.put("study_subject", subject);
                params.put("study_time", time);
                params.put("study_start", start.replace(" ", ":"));
                params.put("study_end", end.replace(" ", ":"));
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }
    private void UpdateData(){
        String url = Env.reSave2URL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //json object >> {response:[{key : value}, {.....
                    JSONObject jsonObject = new JSONObject(response);
                    String res = jsonObject.getString("success");

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
                String time = tv_subject_timer.getText().toString().replace(":","");
                end = timeFormat.format(new Date());
                Map<String,String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("study_date", today);
                params.put("study_subject", subject);
                params.put("study_time", time);
                params.put("study_start", start.replace(" ", ":"));
                params.put("study_end", end.replace(" ", ":"));
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
                            JSONObject studyObject2 = jsonArray.getJSONObject(1);

                            String studyTime = studyObject.getString("study_time");
                            String studyTimeSecTmp = studyObject.getString("study_time_sec");
                            String studyTotalTime = studyObject2.getString("study_total_time");

                            if(!studyTime.equals("null")){
                                convertToTime(studyTime);
                                studyTimeSec = Integer.parseInt(studyTimeSecTmp);
                            }else{
                                isFirst = true;
                                studyTimeSec = 0;
                            }
                            if(studyTotalTime.equals("null")){
                                studyTimeTotalSec = 0;
                            }else{
                                studyTimeTotalSec = Integer.parseInt(studyTotalTime);
                            }
                            handler.postDelayed(runnable, 0);
                            start = timeFormat.format(new Date());
                            gapOfSecond = 60 - Integer.parseInt(start.split(" ")[2]);
                            gapOfMinute = 60 - Integer.parseInt(start.split(" ")[1]) - 1;
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

        if(Seconds + gapOfSecond >= 60){
            gapOfMinute++;
            gapOfSecond = Seconds + gapOfSecond - 60;
        }
        if(Minutes + gapOfMinute > 60){
            gapOfMinute = Minutes + gapOfMinute - 60;
        }
    }
    public Runnable runnable = new Runnable() {
        public void run() {
            /*
            현재 부팅한 시간을 나타낸다 uptimeMiillis, StartTime = 처음 부팅 시간
            SystemClock.uptimeMillis() - StartTime을 하면 밀리 초가 나오고 그것을 1000으로 나누면 1초다.
            leaveTime은 중간에 다른 공간으로 가면 시간의 텀을 빼주는 것 이다.
             */
            MillisecondTime = SystemClock.uptimeMillis() - StartTime - leaveTime;

            //시간의 흐름
            tmp = (int)(MillisecondTime / 1000);
            t = studyTimeSec + tmp;
            hour = t / 3600;
            t %= 3600;
            min = t / 60;
            t %= 60;
            sec = t;

            t2 = studyTimeTotalSec + tmp;
            totalHour = t2 / 3600;
            t2 %= 3600;
            totalMin = t2 / 60;
            t2 %= 60;
            totalSec = t2;

            //String format을 통한 시간 대입
            tv_subject_timer.setText(String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec));
            tv_total_timer.setText(String.format("%02d", totalHour) + ":" + String.format("%02d", totalMin) + ":" + String.format("%02d", totalSec));
            System.out.println(totalHour +  "          " + hour);
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        // 공부 종료 정보를 서버로 보냄
        mSocket.emit("end", userID);
        System.out.println("sdafsa");

        isActiveOn = false;
        handler.removeCallbacks(runnable);
        if(isFirst){
            InsertData();
            isFirst = false;
        }else{
            UpdateData();
        }

        termTime = SystemClock.uptimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isActiveOn){
            showMessage();
        }
    }
    //dialog 정의
    private void showMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("공부합시다.");
        builder.setMessage("이어하기");
        builder.setPositiveButton("계속", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 공부 시작 정보를 서버로 보냄
                mSocket.emit("start", userID);

                //정지된 만큼 텀 계산
                termTime = SystemClock.uptimeMillis() - termTime;
                leaveTime += termTime;
                isActiveOn = true;
                start = timeFormat.format(new Date());
                handler.postDelayed(runnable, 0);
            }
        });
        builder.show();
    }

    //버튼 누른 효과를 그대로 뒤로가기 버튼누를 때 나타남
    @Override
    public void onBackPressed() {
        isStart = false;
        Intent intent = new Intent(StopwatchActivity.this, HomeActivity.class);
        startActivity(intent);
        handler.removeCallbacks(runnable);

        super.onBackPressed();
    }
}