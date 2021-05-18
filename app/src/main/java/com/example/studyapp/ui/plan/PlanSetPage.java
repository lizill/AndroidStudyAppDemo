package com.example.studyapp.ui.plan;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.*;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.FirstActivity;
import com.example.studyapp.JSONTask;
import com.example.studyapp.LoginActivity;
import com.example.studyapp.R;
import com.example.studyapp.UserNameActivity;
import com.example.studyapp.recycle.PlanData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.sql.DriverManager.println;

public class PlanSetPage extends AppCompatActivity {
    private final int PERMISSIONS_REQUEST_RESULT = 1;

    /*
     * 사용자의 id와 password
     */
    private String userID;
    private String userPassword;
    /*
     * 과목을 설정하는 edittext
     */
    private EditText editText;

    /*
     * 시간을 고르는 PlanTimePicker에 사용 될 시간
     * 시작, 끝, 어디를 선택한지 확인하는 boolean값
     */
    public static Button st_btn;
    public static Button en_btn;
    public static TextView en_txt;

    static int st_hour;
    static int st_min;
    static int en_hour;
    static int en_min;
    static boolean whoTouch=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        setContentView(R.layout.plan_set_page);
        Intent intent = getIntent();
        userID=intent.getStringExtra("userID");
        userPassword=intent.getStringExtra("userPassword");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //권한을 거절하면 재 요청을 하는 함수
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_RESULT);
            }
        }//권한 요청

        // 입력 값을 넣는 edittext선언
        editText = findViewById(R.id.plan_subject);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode== KeyEvent.KEYCODE_ENTER) return true;
                return false;
            }
        });

        /*
         * Calender를 이용하여 오늘 지금 시간을 가져오고 그 시간에서 시, 분을 변수에 저장
         * 그리고 그 값을 view에 settext로 지정한다.
         */
        Calendar mCal = Calendar.getInstance();
        st_hour = mCal.get(Calendar.HOUR_OF_DAY);
        st_min = mCal.get(Calendar.MINUTE);

        st_btn = findViewById(R.id.start_time_btn);
        en_btn = findViewById(R.id.end_time_btn);
        en_txt = findViewById(R.id.end_time);
        st_btn.setText(PlanTimePicker.hourCal(st_hour, st_min,true));
        en_hour = st_hour;
        en_min = st_min+29;
        en_btn.setText(PlanTimePicker.hourCal(en_hour, en_min,false));

        /*
         * 위 버튼을 눌렀을 때 어느 버튼을 눌렀는지 정하고
         * timepicker로 시간을 정함
         */
        PlanTimePicker planTimePicker = new PlanTimePicker();
        st_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                whoTouch=true;
                planTimePicker.show(getSupportFragmentManager(),
                        "help");

            }
        });
        en_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                whoTouch=false;
                planTimePicker.show(getSupportFragmentManager(),
                        "help");
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    /*
     * 상단 액션바에 추가 된 버튼에 이벤트를 삽입
     * 이벤트가 발동되었을 때 서버와 연결하여 가져온 값들과 내가 TimePicker를 이용하여 입력한 값을 비교해
     * 중복되는 값을 확인하고 없다면 PlanTask를 이용해 서버와 연결하고 데이터를 보내는 메소드를 실행함
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String str = editText.getText().toString().replaceAll(" ","");
        if(str == null||str.equals(null)||str.equals("")){
            PlanTask.showToast(this,"과목명을 입력 해주세요.");
            return true;
        }
        if(str.length()>10){
            PlanTask.showToast(this,"과목이름이 너무 깁니다.");
            return true;
        }
        if(findDuplication(st_hour, st_min, en_hour,en_min).equals("0")){
            System.out.println("중복 없음");
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("position",PlanFragment.recycleArrayList.size()+1);
                jsonObject.accumulate("user_id", userID);
                jsonObject.accumulate("user_password", userPassword);
                jsonObject.accumulate("subject",str);
                jsonObject.accumulate("start",st_hour+":"+st_min+":00");
                jsonObject.accumulate("end",en_hour+":"+en_min+":00");

                PlanTask planTask = new PlanTask(jsonObject, "planInsert", "POST");
                planTask.execute();
                planTask = new PlanTask(jsonObject, "plan", "POST");
                planTask.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PlanTask.showToast(this,"추가되었습니다.");

            PlanFragment.planAdapter.notifyDataSetChanged();
            onBackPressed();
        }else if(findDuplication(st_hour, st_min, en_hour,en_min).equals("2")){
            PlanTask.showToast(this,"공부시간은 1분 이상이어야 합니다.");
        }else{
            PlanTask.showToast(this,"기존 기록과 시간이 중복됩니다.");
        }

        return true;
    }

    /*
     * 중복 확인하는 메소드
     * 시간이 저장된 ArrayList에 있는 시간과 중복되는지 확인하고
     * 중복되지 않으면 0을 return
     * 중복되면 1을 return
     * 시작값과 끝값이 같으면 2를 return하고
     * 결과를 통해 위 method에서 toast를 띄움
     */
    public String findDuplication(int st_hour, int st_min, int en_hour, int en_min){
        int st_time = st_hour*60+st_min;
        int en_time = en_hour*60+en_min;
        if(st_time == en_time) return "2";
        for(int i = 0;i<PlanFragment.recycleArrayList.size();i++){
            String timeStr = PlanFragment.recycleArrayList.get(i).getTv_content();
            int st_bTime=PlanTask.timeCal(timeStr);
            int en_bTime=PlanTask.timeCal(timeStr.substring(11));
            if(     st_time==st_bTime||
                    st_time==en_bTime||
                    en_time==st_bTime||
                    en_time==en_bTime)
                return "1";
            if(st_time<en_time){
                if(st_bTime<en_bTime){
                    if(st_time<st_bTime&&en_time>st_bTime){
                        return "1";
                    }else if(st_time>st_bTime&&st_time<en_bTime){
//                        false;
                        return "1";
                    }else if(st_time>en_bTime){
//                        true;
                    }
                }else{
                    if(st_time<en_bTime){
//                        false;
                        return "1";
                    }else if(en_time>st_bTime){
//                        false;
                        return "1";
                    }
                }
            }else{
                //st_time>en_time
                if(st_bTime<en_bTime){
                    if(en_time>st_bTime||st_time<en_bTime)
                        return "1";
                }else{
//                    false;
                    return "1";
                }
            }
        }
        return "0";
    }
}
