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
                    negativeBuilder("아이디를 입력해 주세요.", "확인");
                    return;
                }
                else if(userPassword.isEmpty()) {
                    negativeBuilder("비밀번호를 입력해 주세요.", "확인");
                    return;
                }
                else if(userPasswordCheck.isEmpty() || !userPassword.equals(userPasswordCheck)) {
                    negativeBuilder("두개의 비밀번호가 일치하지 않습니다.", "확인");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("user_id", userID);
                    jsonObject.accumulate("user_password", userPassword);

                    RegisterTask loginTask = new RegisterTask(jsonObject, "register", "POST");
                    loginTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    class RegisterTask extends JSONTask {

        public RegisterTask(JSONObject jsonObject, String urlPath, String method) {
            super(jsonObject, urlPath, method);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String resultNum = jsonObject.get("result").toString();

                if(resultNum.equals("0")) { // duplicated id
                    negativeBuilder("중복된 아이디 입니다.","확인");
                }
                else {
                    // 회원가입 완료 후 로그인 페이지로 이동, 페이지 닫기
                    Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, FirstActivity.class);
                    RegisterActivity.this.startActivity(intent);
                    finish();
                }
                progressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}