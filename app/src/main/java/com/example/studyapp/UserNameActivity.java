package com.example.studyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

        userID = FirstActivity.userInfo.getString(FirstActivity.USER_ID, null);

        completeButton = (Button) findViewById(R.id.completeButton);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameET.getText().toString();

                if(userName.isEmpty()) {
                    negativeBuilder("이름을 입력해주세요.", "확인");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("user_id", userID);
                    jsonObject.accumulate("user_name", userName);

                    NameTask nameTask = new NameTask(jsonObject, "name", "POST");
                    nameTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void negativeBuilder(String msg, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserNameActivity.this);
        builder.setMessage(msg)
                .setNegativeButton(text, null)
                .create()
                .show();
    }

    class NameTask extends JSONTask {

        public NameTask(JSONObject jsonObject, String Path, String method) {
            super(jsonObject, Path, method);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String resultNum = jsonObject.get("result").toString();

                String userName = userNameET.getText().toString();

                if(resultNum.equals("1")) {
                    SharedPreferences.Editor autoLogin = FirstActivity.userInfo.edit();
                    autoLogin.putString(FirstActivity.USER_NAME, userName);
                    autoLogin.commit();

                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(UserNameActivity.this, HomeActivity.class);
                    UserNameActivity.this.startActivity(intent);
                    finish();
                }
                else {
                    negativeBuilder("Failed Sign In", "Retry");
                    progressBar.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}