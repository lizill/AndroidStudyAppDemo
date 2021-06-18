package com.example.studyapp.ui.group;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.R;
import com.example.studyapp.ui.home.HomeFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.acl.Group;
import java.util.Arrays;

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
    private TextView changeMaster_TV;
    private TextView changeCategory_TV;
    private TextView changeGoalTime_TV;
    private TextView changeMemberLimit_TV;

    private LinearLayout changeGroupName;
    private LinearLayout changeMemberLimit;
    private LinearLayout changeGoalTime;
    private LinearLayout changeCategory;
    private LinearLayout changeContents;
    private LinearLayout changeMaster;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_option);

        getSupportActionBar().setTitle("Group");

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

        changeGroupName_TV = findViewById(R.id.change_groupName_TV);
        changeMaster_TV = findViewById(R.id.change_master_TV);
        changeCategory_TV = findViewById(R.id.change_category_TV);
        changeGoalTime_TV = findViewById(R.id.change_goalTime_TV);
        changeMemberLimit_TV = findViewById(R.id.change_memberLimit_TV);

        changeGroupName = findViewById(R.id.change_groupName);
        changeMemberLimit = findViewById(R.id.change_memberLimit);
        changeGoalTime = findViewById(R.id.change_goalTime);
        changeCategory = findViewById(R.id.change_category);
        changeContents = findViewById(R.id.change_contents);
        changeMaster = findViewById(R.id.change_master);

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

                                            HomeFragment.mSocket.emit("left", gson.toJson(new RoomData(userID, groupName, System.currentTimeMillis())));

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
                        if(changeGroupName.isEmpty()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(GroupOption.this);
                            builder.setTitle("그룹정보 변경").setMessage("그룹 이름을 입력해 주세요.")
                                    .setNegativeButton("확인", null)
                                    .create()
                                    .show();
                        } else {
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
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        changeMemberLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] memberLimitArr = new String[49];
                for(int i = 0; i < 49; i++){
                    memberLimitArr[i] =(i+2) + "명";
                }
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GroupOption.this);
                builder.setTitle("모집인원 선택");
                builder.setItems(memberLimitArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int changeMemberLimit = which + 2;
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                    jsonObject = jsonArray.getJSONObject(0);
                                    String memberCount = jsonObject.getString("count");
                                    Log.d("아아아아", memberCount);
                                    Log.d(""+(Integer.parseInt(memberCount) >= changeMemberLimit), memberCount + ":" + changeMemberLimit);
                                    if (Integer.parseInt(memberCount) > changeMemberLimit) {
                                        Log.d("크거나 같음","불가능");
                                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(GroupOption.this);
                                        builder.setTitle("그룹정보 변경").setMessage("현재 인원보다 적습니다.")
                                                .setNegativeButton("확인", null)
                                                .create()
                                                .show();
                                    } else {
                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try{
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    boolean success = jsonResponse.getBoolean("success");
                                                    if (success) {
                                                        Log.d("성공",":::");
                                                        memberLimit = changeMemberLimit + "";
                                                        groupMember_TV.setText(memberCount + "/" + memberLimit);
                                                        changeMemberLimit_TV.setHint(memberLimit + "명");
                                                    }
                                                } catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        ChangeMemberLimitRequest changeMemberLimitRequest = new ChangeMemberLimitRequest(groupName, changeMemberLimit, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                                        queue.add(changeMemberLimitRequest);
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        MemberCountCheck memberCountCheck = new MemberCountCheck(groupName, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                        queue.add(memberCountCheck);
                    }
                });
                builder.show();
            }
        });

        changeGoalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] goalTimeArr = new String[12];
                for(int i = 0; i < 12; i++){
                    goalTimeArr[i] = "하루 " + (i+1) + "시간";
                }
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GroupOption.this);
                builder.setTitle("목표시간 선택");
                builder.setItems(goalTimeArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goalTime = (which + 1)+"";
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Log.d("성공",":::");
                                        groupGoalTime_TV.setText(goalTime + "시간");
                                        changeGoalTime_TV.setHint(goalTime + "시간");
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        ChangeGoalTimeRequest changeGoalTimeRequest = new ChangeGoalTimeRequest(groupName, goalTime, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                        queue.add(changeGoalTimeRequest);
                    }
                });
                builder.show();

            }
        });

        changeCategory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GroupOption.this);
                builder.setTitle("카테고리 선택");
                builder.setItems(MakeGroup.categoryArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        category = MakeGroup.categoryArr[which];
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Log.d("성공",":::");
                                        groupCategory_TV.setText(category);
                                        changeCategory_TV.setHint(category);
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        ChangeCategoryRequest changeCategoryRequest = new ChangeCategoryRequest(groupName, category, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                        queue.add(changeCategoryRequest);
                    }
                });
                builder.show();
            }
        });

        changeContents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupOption.this);
                builder.setTitle("공지사항 변경").setMessage("공지사항");
                final EditText contentsET = new EditText(GroupOption.this);
                contentsET.setText(contents);
                contentsET.setHint("변경하실 공지사항을 입력하세요.");
                contentsET.setHeight(500);
                builder.setView(contentsET);
                builder.setPositiveButton("변경하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contents = contentsET.getText().toString();
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
                        ChangeContentsRequest changeContentsRequest = new ChangeContentsRequest(groupName, contents, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                        queue.add(changeContentsRequest);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        changeMaster.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            String[] members = new String[jsonArray.length()];
                            for(int i = 0; i < members.length; i++) {
                                members[i] = jsonArray.getJSONObject(i).getString("userName");
                            }
                            Log.d("완성", Arrays.toString(members));
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GroupOption.this);
                            builder.setTitle("그룹장 변경");
                            builder.setItems(members, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String newMaster = members[which];
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try{
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if (success) {
                                                    Log.d("성공",":::");
                                                    groupMaster_TV.setText(newMaster);
                                                    changeMaster_TV.setHint(newMaster);
                                                    onBackPressed();
                                                }
                                            } catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    ChangeMasterRequest changeMasterRequest = new ChangeMasterRequest(groupName, newMaster, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                                    queue.add(changeMasterRequest);
                                }
                            });
                            builder.show();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                GetMemberRequest getMemberRequest = new GetMemberRequest(groupName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(GroupOption.this);
                queue.add(getMemberRequest);
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

                    memberCount = object.getString("count");

                    category = object.getString("category");
                    groupCategory_TV.setText(category);
                    changeCategory_TV.setHint(category);

                    goalTime = object.getString("goalTime");
                    groupGoalTime_TV.setText(goalTime + "시간");
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