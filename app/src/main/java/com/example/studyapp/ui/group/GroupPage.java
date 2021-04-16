package com.example.studyapp.ui.group;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.GroupActivity;
import com.example.studyapp.HomeActivity;
import com.example.studyapp.R;

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
    private String peopleCount;
    private TextView groupTextView;
    private TextView contentsTextView;
    private TextView peopleCountTextView;
    private Button LeaveButton;
    private Button enterChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        Intent intent = getIntent();
        userID = userInfo.getString(USER_ID,null);
        group = intent.getStringExtra("group");
        Log.d("llll", group);

        groupTextView = findViewById(R.id.groupName);
        groupTextView.setText(group);

        contentsTextView = findViewById(R.id.contents);

        peopleCountTextView = findViewById(R.id.NumberOfPeople);

        new BackgroundTask().execute();



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
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupPage.this);
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

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String link = "https://www.dong0110.com/chatphp/GroupPage.php?group=" + group;
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
                    contents = object.getString("contents");
                    Log.d(contents, "asdf");
                    contentsTextView.setText(contents);
                    peopleCount = object.getString("count");
                    Log.d(peopleCount, "sadfa");
                    peopleCountTextView.setText("멤버 " + peopleCount);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}