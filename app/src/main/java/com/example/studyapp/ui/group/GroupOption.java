package com.example.studyapp.ui.group;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.acl.Group;

import static com.example.studyapp.FirstActivity.USER_ID;
import static com.example.studyapp.FirstActivity.USER_NAME;
import static com.example.studyapp.FirstActivity.userInfo;

public class GroupOption extends AppCompatActivity {

    private Button groupLeaveButton;
    private Button dropGroupButton;
    private String userID;
    private String userName;
    private String groupName;
    private LinearLayout masterMenu;

    private String contents;
    private String memberCount;
    private String category;
    private String goalTime;
    private String master;
    private String memberLimit;

    private TextView groupName_TV;
    private TextView groupMaster_TV;
    private TextView groupCategory_TV;
    private TextView groupGoalTime_TV;
    private TextView groupMember_TV;

    private TextView changeGroupName_TV;
    private TextView changeContents_TV;
    private TextView changeMaster_TV;
    private TextView changeCategory_TV;
    private TextView changeGoalTime_TV;
    private TextView changeMemberLimit_TV;

    private LinearLayout changeGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_option);

        Intent intent = getIntent();
        userID = userInfo.getString(USER_ID, null);
        userName = userInfo.getString(USER_NAME, null);
        groupName = intent.getStringExtra("group");
        Log.d(groupName, "그룹 이름");

        groupName_TV = findViewById(R.id.groupName);
        groupGoalTime_TV = findViewById(R.id.groupGoalTime);
        groupMaster_TV = findViewById(R.id.groupMaster);
        groupCategory_TV = findViewById(R.id.groupCategory);
        groupMember_TV = findViewById(R.id.groupMember);

        changeContents_TV = findViewById(R.id.change_contents_TV);
        changeGroupName_TV = findViewById(R.id.change_groupName_TV);
        changeMaster_TV = findViewById(R.id.change_master_TV);
        changeCategory_TV = findViewById(R.id.change_category_TV);
        changeGoalTime_TV = findViewById(R.id.change_goalTime_TV);
        changeMemberLimit_TV = findViewById(R.id.change_memberLimit_TV);

        changeGroupName = findViewById(R.id.change_groupName);

        dropGroupButton = findViewById(R.id.dropGroup);
        groupLeaveButton = findViewById(R.id.leaveGroup);
        masterMenu = findViewById(R.id.master_menu);


        new BackgroundTask().execute();

        groupLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.equals(master)) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(GroupOption.this);
                    builder.setTitle("그룹탈퇴").setMessage("그룹장은 탈퇴할 수 없습니다. 그룹을 삭제하거나 그룹장을 다른 사람에게 위임 후 탈퇴해주세요.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupOption.this);
                    builder.setTitle("그룹 탈퇴").setMessage("\'" + groupName + "\'" + "그룹을 정말 탈퇴하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
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
                            LeaveGroupRequest leaveGroupRequest = new LeaveGroupRequest(userID, groupName, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                            queue.add(leaveGroupRequest);
                        }
                    });

                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        dropGroupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupOption.this);
                builder.setTitle("그룹 해체").setMessage("\'" + groupName + "\'" + "그룹을 정말 해체하시겠습니까? 그룹이 완전히 사라집니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
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
                                        GroupOption.super.onBackPressed();
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        DropGroupRequest dropGroupRequest = new DropGroupRequest(groupName, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                        queue.add(dropGroupRequest);
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        changeGroupName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupOption.this);
                builder.setTitle("그룹명 변경").setMessage("그룹명");
                final EditText groupNameET = new EditText(GroupOption.this);
                groupNameET.setText(groupName);
                groupNameET.setHint("변경하실 그룹명을 입력하세요.");
                builder.setView(groupNameET);
                builder.setPositiveButton("변경하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String changeGroupName = groupNameET.getText().toString();
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
                                                try{
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    boolean success = jsonResponse.getBoolean("success");
                                                    if (success) {
                                                        Log.d("성공",":::");
                                                        groupName = changeGroupName;
                                                        groupName_TV.setText(changeGroupName);
                                                        changeGroupName_TV.setHint(changeGroupName);
                                                    }
                                                } catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        ChangeGroupNameRequest changeGroupNameRequest = new ChangeGroupNameRequest(groupName, changeGroupName, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                                        queue.add(changeGroupNameRequest);
                                    }
                                    else {
                                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(GroupOption.this);
                                        builder.setTitle("그룹정보 변경").setMessage("이미 사용중인 그룹이름 입니다.")
                                                .setNegativeButton("확인", null)
                                                .create()
                                                .show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        GroupNameCheck groupNameCheck = new GroupNameCheck(changeGroupName, responseLister);
                        RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                        queue.add(groupNameCheck);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GroupOption.this, GroupPage.class);
        intent.putExtra("group", groupName);
        startActivity(intent);
        finish();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String link = "https://www.dong0110.com/chatphp/GroupPage.php?group=" + groupName;
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                if(jsonArray.length() == 0) return;
                int count = 0;
                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    groupName = object.getString("groupName");
                    groupName_TV.setText(groupName);
                    changeGroupName_TV.setHint(groupName);

                    contents = object.getString("contents");
                    changeContents_TV.setHint(contents);

                    memberCount = object.getString("count");

                    category = object.getString("category");
                    groupCategory_TV.setText(category);
                    changeCategory_TV.setHint(category);

                    goalTime = object.getString("goalTime");
                    groupGoalTime_TV.setText(goalTime);
                    changeGoalTime_TV.setHint(goalTime + "시간");

                    master = object.getString("master");
                    groupMaster_TV.setText(master);
                    changeMaster_TV.setHint(master);
                    if(userName.equals(master)){
                        masterMenu.setVisibility(View.VISIBLE);
                    }

                    memberLimit = object.getString("peopleLimit");
                    groupMember_TV.setText(memberCount + "/" + memberLimit);
                    changeMemberLimit_TV.setHint(memberLimit + "명");
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}