package com.example.studyapp.ui.group;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavInflater;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.JSONTask;
import com.example.studyapp.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
    private Button groupOptionButton;

    private ArrayList<MemberData> membersData;
    private RecyclerView memberRecyclerView;
    private MemberRecyclerAdapter adapter;
    private TimeZone tz;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        Intent intent = getIntent();
        userID = userInfo.getString(USER_ID, null);
        group = intent.getStringExtra("group");
        Log.d("llll", group);

        groupTextView = findViewById(R.id.groupName);
        groupTextView.setText(group);

        contentsTextView = findViewById(R.id.contents);

        peopleCountTextView = findViewById(R.id.NumberOfPeople);

        new BackgroundTask().execute();


        groupOptionButton = findViewById(R.id.groupOption);
        groupOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupPage.this, GroupOption.class);
                intent.putExtra("group", group);
                startActivity(intent);
                finish();
            }
        });

        membersData = new ArrayList<MemberData>();

        tz = TimeZone.getTimeZone("Asia/Seoul");
        dateFormat.setTimeZone(tz);
        String today = dateFormat.format(new Date());
        System.out.println(today);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("room_name", group);
            jsonObject.accumulate("today", today);

            MemberTask memberTask = new MemberTask(jsonObject, "member", "POST");
            memberTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        memberRecyclerView = (RecyclerView) findViewById(R.id.member_recycler_view);
        memberRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new MemberRecyclerAdapter(membersData);
        memberRecyclerView.setAdapter(adapter);
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

    class MemberTask extends JSONTask {

        public MemberTask(JSONObject jsonObject, String Path, String method) {
            super(jsonObject, Path, method);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject memberObject = new JSONObject(result);
                JSONArray jsonArray = new JSONArray(memberObject.getString("data"));

                for(int i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    String userID = jsonObject.getString("userID");
                    String totalTime = jsonObject.getString("total_study_time");
                    String online = jsonObject.getString("online");
                    membersData.add(new MemberData(group, userID, totalTime, online));
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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