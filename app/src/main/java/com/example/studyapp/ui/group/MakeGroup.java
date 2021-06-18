package com.example.studyapp.ui.group;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.studyapp.FirstActivity.USER_ID;
import static com.example.studyapp.FirstActivity.USER_NAME;
import static com.example.studyapp.FirstActivity.userInfo;

public class MakeGroup extends AppCompatActivity {
    private String userID;
    private EditText groupNameEditText;
    private EditText categorySelect;
    private EditText contentsEditText;
    public final static String[] categoryArr = {"초등학교", "중학교", "고등학교", "대학교"};
    private EditText goalTimeSelect;
    public final String[] goalTimeArr = new String[12];
    private int goalTime = 0;
    private EditText memberLimitSelect;
    public final String[] memberLimitArr = new String[49];
    private int memberLimit = 0;
    private Button makeGroupButton;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);

        getSupportActionBar().setTitle("Group");

        userID = userInfo.getString(USER_ID,null);
        userName = userInfo.getString(USER_NAME, null);

        groupNameEditText = findViewById(R.id.groupName);
        contentsEditText = findViewById(R.id.contents);

        categorySelect = findViewById(R.id.category);
        categorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MakeGroup.this);
                builder.setTitle("카테고리 선택");
                builder.setItems(categoryArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        categorySelect.setText(categoryArr[which]);
                        Toast.makeText(getApplicationContext(), categoryArr[which], Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });

        goalTimeSelect = findViewById(R.id.goalTime);
        for(int i = 0; i < 12; i++){
            goalTimeArr[i] = "하루 " + (i+1) + "시간";
        }
        goalTimeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MakeGroup.this);
                builder.setTitle("목표시간 선택");
                builder.setItems(goalTimeArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goalTimeSelect.setText(goalTimeArr[which]);
                        goalTime = which + 1;
                        Toast.makeText(getApplicationContext(), goalTimeArr[which], Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });

        memberLimitSelect = findViewById(R.id.personCount);
        for(int i = 0; i < 49; i++){
            memberLimitArr[i] =(i+2) + "명";
        }
        memberLimitSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MakeGroup.this);
                builder.setTitle("모집인원 선택");
                builder.setItems(memberLimitArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        memberLimitSelect.setText(memberLimitArr[which]);
                        memberLimit = which + 2;
                        Toast.makeText(getApplicationContext(), memberLimitArr[which], Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });
        makeGroupButton = findViewById(R.id.makeGroupButton);
        makeGroupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String groupName = groupNameEditText.getText().toString();
                String category = categorySelect.getText().toString();
                String contents = contentsEditText.getText().toString();
                if(groupName.isEmpty()) {
                    negativeBuilder("그룹 이름을 입력해 주세요", "close");
                    return;
                }
                if(category.isEmpty()) {
                    negativeBuilder("카테고리를 선택해 주세요", "close");
                    return;
                }
                if(goalTime == 0) {
                    negativeBuilder("목표 시간을 선택해 주세요", "close");
                    return;
                }
                if(memberLimit == 0) {
                    negativeBuilder("모집 인원을 선택해 주세요", "close");
                    return;
                }
                if(contents.isEmpty()) {
                    negativeBuilder("그룹 설명을 적어 주세요", "close");
                    return;
                }

                Response.Listener<String> responseLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            Log.d("success", ""+success);
                            if(!success) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if(success) {
                                                Log.d("결과", "만들기 성공");
                                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try{
                                                            JSONObject jsonResponse = new JSONObject(response);
                                                            boolean success = jsonResponse.getBoolean("success");
                                                            if (success) {
                                                                Log.d("성공",":::");
                                                                peopleCountIncrease(groupName);
                                                                MakeGroup.super.onBackPressed();
                                                            }
                                                        } catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                };
                                                JoinGroupRequest joinGroupRequest = new JoinGroupRequest(userID, groupName, responseListener);
                                                RequestQueue queue = Volley.newRequestQueue(MakeGroup.this);
                                                queue.add(joinGroupRequest);
                                            }
                                            else {
                                                negativeBuilder("만들기 실패", "Retry");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                MakeGroupRequest makeGroupRequest = new MakeGroupRequest(groupName, contents, category, goalTime, userName, memberLimit, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(MakeGroup.this);
                                queue.add(makeGroupRequest);
                            }
                            else {
                                negativeBuilder("이미 존재하는 이름입니다.", "Retry");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                GroupNameCheck groupNameCheck = new GroupNameCheck(groupName, responseLister);
                RequestQueue queue = Volley.newRequestQueue(MakeGroup.this);
                queue.add(groupNameCheck);
            }
        });
    }

    private void peopleCountIncrease(String group) {
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
        PeopleCountIncreaseRequest peopleCountIncreaseRequest = new PeopleCountIncreaseRequest(group, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MakeGroup.this);
        queue.add(peopleCountIncreaseRequest);
    }

    private void negativeBuilder(String msg, String text) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MakeGroup.this);
        builder.setMessage(msg)
                .setNegativeButton(text, null)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MakeGroup.this, SearchGroupPage.class);
        startActivity(intent);
        finish();
    }
}