package com.example.studyapp.ui.group;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.R;

import org.json.JSONObject;

import static com.example.studyapp.FirstActivity.USER_ID;
import static com.example.studyapp.FirstActivity.userInfo;

public class GroupOption extends AppCompatActivity {

    private Button groupLeaveButton;
    private String userID;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_option);

        Intent intent = getIntent();
        userID = userInfo.getString(USER_ID, null);
        groupName = intent.getStringExtra("group");
        Log.d(groupName, "그룹 이름");


        groupLeaveButton = findViewById(R.id.leaveGroup);
        groupLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupOption.this);
                builder.setTitle(groupName).setMessage("정말로 그룹을 탈퇴하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Log.d("성공",":::");
                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try{
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    boolean success = jsonResponse.getBoolean("success");
                                                    if (success) {
                                                        Log.d("성공",":::");
                                                        GroupOption.super.onBackPressed();
                                                    }
                                                } catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        PeopleCountDecreaseRequest peopleCountDecreaseRequest = new PeopleCountDecreaseRequest(groupName, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                                        queue.add(peopleCountDecreaseRequest);
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        LeaveGroupRequest LeaveGroupRequest = new LeaveGroupRequest(userID, groupName, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                        queue.add(LeaveGroupRequest);
                    }
                });

                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}