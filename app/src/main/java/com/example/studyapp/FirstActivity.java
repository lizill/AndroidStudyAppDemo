package com.example.studyapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstActivity extends AppCompatActivity {

    // ------------------------------------------------------------------------------------
    // 유저 정보 변수
    // ------------------------------------------------------------------------------------
    public static SharedPreferences userInfo;
    public static final String USER_INFO = "userInfo";
    public static final String USER_ID = "userId";
    public static final String USER_PASSWORD = "userPassword";
    public static final String USER_NAME = "userName";

    private String userId, userPassword, userName;
    private ProgressBar progressBar;
    private Button signInButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // ------------------------------------------------------------------------------------
        // 액션바, 상태바 없애기
        // ------------------------------------------------------------------------------------
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // ------------------------------------------------------------------------------------
        // 유저 정보가 있을경우 자동 로그인, 이름 설정 페이지 또는 홈으로 이동
        // ------------------------------------------------------------------------------------
        userInfo = getSharedPreferences(USER_INFO, Activity.MODE_PRIVATE);

        userId = userInfo.getString(USER_ID,null);
        userPassword = userInfo.getString(USER_PASSWORD,null);
        userName = userInfo.getString(USER_NAME, null);

        // 로딩 바
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // 유저 정보가 없을경우 회원가입, 로그인 버튼 표시
        if(userId == null && userPassword == null) {
            signInButton = (Button) findViewById(R.id.signInButton);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FirstActivity.this, RegisterActivity.class);
                    FirstActivity.this.startActivity(intent);
                }
            });

            loginButton = (Button) findViewById(R.id.loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                    FirstActivity.this.startActivity(intent);
                }
            });
        }
        // 저장된 유저 정보가 있을 경우 로그인 체크
        else {
            progressBar.setVisibility(View.VISIBLE);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", userId);
                jsonObject.accumulate("user_password", userPassword);

                FirstTask loginTask = new FirstTask(jsonObject, "login", "POST");
                loginTask.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 경고창
    private void negativeBuilder(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
        builder.setMessage(msg)
                .setNegativeButton("확인", null)
                .create()
                .show();
    }

    class FirstTask extends JSONTask {

        public FirstTask(JSONObject jsonObject, String urlPath, String method) {
            super(jsonObject, urlPath, method);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String resultNum = jsonObject.get("result").toString();

                if (resultNum.equals("1")) { // 1 : 로그인 성공
                    progressBar.setVisibility(View.GONE);
                    if(userName != null) {
                        Intent intent = new Intent(FirstActivity.this, HomeActivity.class);
                        FirstActivity.this.startActivity(intent);
                    } else {
                        Intent intent = new Intent(FirstActivity.this, UserNameActivity.class);
                        FirstActivity.this.startActivity(intent);
                    }
                    finish();
                } else {
                    negativeBuilder("로그인 정보를 확인해 주세요.");
                    progressBar.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}