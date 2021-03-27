package com.example.studyapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserNameActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText userNameET;
    private Button completeButton;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        userNameET = (EditText) findViewById(R.id.userNameET);
        userID = FirstActivity.userInfo.getString("inputID", null);

        completeButton = (Button) findViewById(R.id.completeButton);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameET.getText().toString();

                if(userName.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserNameActivity.this);
                    builder.setMessage("Please insert Name")
                            .setPositiveButton("close", null)
                            .create()
                            .show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                // Save user Name
                                SharedPreferences.Editor autoLogin = FirstActivity.userInfo.edit();
                                autoLogin.putString("inputName", userName);
                                autoLogin.commit();

                                Toast.makeText(UserNameActivity.this, "Wellcome !! " + userName + " !!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(UserNameActivity.this, MainActivity.class);
                                UserNameActivity.this.startActivity(intent);
                                finish();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserNameActivity.this);
                                builder.setMessage("Failed Sign Up")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                };
                UserNameRequest userNameRequest = new UserNameRequest(userName, userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UserNameActivity.this);
                queue.add(userNameRequest);
            }
        });

    }
}