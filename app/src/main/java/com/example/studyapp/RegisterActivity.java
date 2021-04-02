package com.example.studyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText idET, passwordET, passwordCheckET;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        idET = (EditText) findViewById(R.id.idET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        passwordCheckET = (EditText) findViewById(R.id.passwordCheckET);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = idET.getText().toString();
                String userPassword = passwordET.getText().toString();
                String userPasswordCheck = passwordCheckET.getText().toString();

                if(userID.isEmpty()) {
                    negativeBuilder("Please insert Email", "close");
                    return;
                }
                if(userPassword.isEmpty()) {
                    negativeBuilder("Please insert Password", "close");
                    return;
                }
                if(userPasswordCheck.isEmpty()) {
                    negativeBuilder("Please insert Password Check", "close");
                    return;
                }
                if(!userPassword.equals(userPasswordCheck)) {
                    negativeBuilder("It doesn't match Password And Password Check", "close");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                // 아이디 중복 체크
                Response.Listener<String> responseLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(!success) {
                                // 중복된 아이디가 없을 경우 회원가입 리퀘스트
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if(success) {
                                                // 회원가입 완료 후 로그인 페이지로 이동, 페이지 닫기
                                                Toast.makeText(RegisterActivity.this, "Success Sign Up !! Please Login", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                Intent intent = new Intent(RegisterActivity.this, FirstActivity.class);
                                                RegisterActivity.this.startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                negativeBuilder("Failed Sign Up", "Retry");
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                };
                                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                                queue.add(registerRequest);
                            }
                            else {
                                negativeBuilder("Duplicate Email", "Retry");
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                };
                RegisterCheck registerCheck = new RegisterCheck(userID, responseLister);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerCheck);
            }
        });
    }

    private void negativeBuilder(String msg, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(msg)
                .setNegativeButton(text, null)
                .create()
                .show();
    }
}