package com.example.studyapp.ui.home;

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

public class StopwatchActivity extends AppCompatActivity {

    private TextView textView ;
    private Button back_btn;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    private Handler handler;
    private int Seconds, Minutes, MilliSeconds, Hours, tmp, t, hour, min, sec;
    private String subject,today,userID,start,end;
    private boolean isFirst = false;

    //현재 날짜 불러오기
    private TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    DateFormat timeFormat = new SimpleDateFormat("HH mm ss", Locale.KOREA);

    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);textView = (TextView)findViewById(R.id.textView);

        userID = FirstActivity.userInfo.getString("userId", null);

        dateFormat.setTimeZone(tz);
        timeFormat.setTimeZone(tz);

        today = dateFormat.format(new Date());

        start = timeFormat.format(new Date());
        System.out.println(start);

        //과목정보 불러오기
        Intent intent = getIntent();
        subject = intent.getStringExtra("subject");


        //Volley Queue  & request json
        requestQueue = Volley.newRequestQueue(getApplication());
        searchStudyTimeToday();

        back_btn = (Button)findViewById(R.id.back_btn);

        handler = new Handler() ;
        StartTime = SystemClock.uptimeMillis();


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StopwatchActivity.this, HomeActivity.class);
                startActivity(intent);
                if(isFirst){
                    InsertData();
                    isFirst = false;
                }else{
                    UpdateData();
                }
                handler.removeCallbacks(runnable);

                end = timeFormat.format(new Date());
                System.out.println(end);

                BeginEndData();
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
                String time = textView.getText().toString().replace(":","");
                Map<String,String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("study_date", today);
                params.put("study_subject", subject);
                params.put("study_time", time);
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
                String time = textView.getText().toString().replace(":","");
                System.out.println(userID + " " + today + " " + subject + " " + time);
                Map<String,String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("study_date", today);
                params.put("study_subject", subject);
                params.put("study_time", time);
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
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            //시간의 흐름
            tmp = (int)(UpdateTime / 1000);
            t = Seconds + tmp;
            hour = Hours + t / 3600;
            t %= 3600;
            min = Minutes + t / 60;
            t %= 60;
            sec = t;

            MilliSeconds = (int) (UpdateTime % 1000);

            //String format을 통한 시간 대입
            textView.setText(String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec));
            handler.postDelayed(this, 0);
        }
    };
}