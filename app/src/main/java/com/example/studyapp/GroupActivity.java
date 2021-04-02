package com.example.studyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GroupActivity extends AppCompatActivity {

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_group);

        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout
                SharedPreferences.Editor editor = FirstActivity.userInfo.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(GroupActivity.this, FirstActivity.class);
                GroupActivity.this.startActivity(intent);
                finish();
                Toast.makeText(GroupActivity.this, "Logout!", Toast.LENGTH_LONG).show();
            }
        });
    }
}