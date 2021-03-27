package com.example.studyapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class FirstActivity extends AppCompatActivity {

    public static SharedPreferences userInfo;

    private String userID, userPassword, userName;
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

        userInfo = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

        userID = userInfo.getString("inputID",null);
        userPassword = userInfo.getString("inputPassword",null);
        userName = userInfo.getString("inputName", null);

        if(userID != null && userPassword != null && userName != null) {
            progressBar.setVisibility(View.VISIBLE);
            Response.Listener<String> responseLister = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success) {
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                            FirstActivity.this.startActivity(intent);
                            finish();
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                            builder.setMessage("Failed Sign In")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            };
            LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseLister);
            RequestQueue queue = Volley.newRequestQueue(FirstActivity.this);
            queue.add(loginRequest);
        }
        else if (userID != null && userPassword != null && userName == null) {
            progressBar.setVisibility(View.VISIBLE);
            Response.Listener<String> responseLister = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success) {
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(FirstActivity.this, UserNameActivity.class);
                            FirstActivity.this.startActivity(intent);
                            finish();
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                            builder.setMessage("Failed Sign In")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            };
            LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseLister);
            RequestQueue queue = Volley.newRequestQueue(FirstActivity.this);
            queue.add(loginRequest);
        }
        else {
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
    }
}