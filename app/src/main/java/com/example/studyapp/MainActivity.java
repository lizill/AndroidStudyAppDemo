package com.example.studyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 유저정보 데이터 초기화 (로그아웃)
                SharedPreferences.Editor editor = FirstActivity.userInfo.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                MainActivity.this.startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Logout!", Toast.LENGTH_LONG).show();
            }
        });
    }
}