package com.example.studyapp.ui.group;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavInflater;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.GroupActivity;
import com.example.studyapp.HomeActivity;
import com.example.studyapp.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.studyapp.FirstActivity.USER_ID;
import static com.example.studyapp.FirstActivity.userInfo;

public class GroupPage extends AppCompatActivity {
    private String group;
    private String contents;
    private String userID;
    private TextView groupTextView;
    private TextView contentsTextView;
    private Button LeaveButton;
    private Button enterChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        Intent intent = getIntent();
        userID = userInfo.getString(USER_ID,null);
        group = intent.getStringExtra("group");
        contents = intent.getStringExtra("contents");
        Log.d("llll", group);
        Log.d("llll", contents);

        groupTextView = findViewById(R.id.groupName);
        groupTextView.setText(group);

        contentsTextView = findViewById(R.id.contents);
        contentsTextView.setText(contents);

        enterChatButton = findViewById(R.id.chatting);
        enterChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupPage.this, ChatActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("group", group);
                startActivity(intent);
            }
        });

        LeaveButton = findViewById(R.id.leaveGroup);
        LeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                leaveGroup();
            }
        });
    }

    private void peopleCountDecrease() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Log.d("성공",":::");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        PeopleCountDecreaseRequest peopleCountDecreaseRequest = new PeopleCountDecreaseRequest(group, responseListener);
        RequestQueue queue = Volley.newRequestQueue(GroupPage.this);
        queue.add(peopleCountDecreaseRequest);
    }

    private void leaveGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(group).setMessage("정말로 그룹을 탈퇴하시겠습니까?");
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
                                peopleCountDecrease();
//                                GroupPage.super.onBackPressed();
                                Intent intent = new Intent(GroupPage.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                LeaveGroupRequest LeaveGroupRequest = new LeaveGroupRequest(userID, group, responseListener);
                RequestQueue queue = Volley.newRequestQueue(GroupPage.this);
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
}