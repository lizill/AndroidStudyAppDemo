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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        userInfo = getSharedPreferences(USER_INFO, Activity.MODE_PRIVATE);

        userId = userInfo.getString(USER_ID,null);
        userPassword = userInfo.getString(USER_PASSWORD,null);
        userName = userInfo.getString(USER_NAME, null);

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
        } else {
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

    private void negativeBuilder(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
        builder.setMessage(msg)
                .setNegativeButton("close", null)
                .create()
                .show();
    }

    class FirstTask extends JSONTask {

        public FirstTask(JSONObject jsonObject, String urlPath, String method) {
            super(jsonObject, urlPath, method);
        }

        @Override
        protected void onPostExecute(String result) {
            if(userName != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String resultNum = jsonObject.get("result").toString();

                    if (resultNum.equals("1")) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(FirstActivity.this, HomeActivity.class);
                        FirstActivity.this.startActivity(intent);
                        finish();
                    } else {
                        negativeBuilder("Failed Sign In");
                        progressBar.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String resultNum = jsonObject.get("result").toString();

                    if (resultNum.equals("1")) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(FirstActivity.this, UserNameActivity.class);
                        FirstActivity.this.startActivity(intent);
                        finish();
                    } else {
                        negativeBuilder("Failed Sign In");
                        progressBar.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}