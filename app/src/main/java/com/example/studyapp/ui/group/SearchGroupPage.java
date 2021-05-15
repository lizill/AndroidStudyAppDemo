package com.example.studyapp.ui.group;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.studyapp.FirstActivity.USER_ID;
import static com.example.studyapp.FirstActivity.userInfo;

public class SearchGroupPage extends AppCompatActivity {
    private RecyclerView groupRecyclerView;
    private SearchGroupRecyclerAdapter adapter;
    private ArrayList<Group> groupList;
    private String userID;
    private Button makeGroupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group_page);

        userID = userInfo.getString(USER_ID,null);
        groupList = new ArrayList<Group>();
        groupRecyclerView = findViewById(R.id.searchGroupRecyclerView);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchGroupRecyclerAdapter(this, getApplicationContext(), groupList);
        groupRecyclerView.setAdapter(adapter);

        makeGroupButton = findViewById(R.id.makeGroupButton);
        makeGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchGroupPage.this, MakeGroup.class);
                startActivity(intent);
            }
        });

//        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String group = (String)((TextView)view.findViewById(R.id.groupText)).getText();
//                Log.d("?????: ", group);
//                AlertDialog.Builder builder = new AlertDialog.Builder(SearchGroupPage.this);
//                builder.setTitle(group).setMessage("그룹에 가입하시겠습니까?");
//                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Response.Listener<String> responseListener = new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                try{
//                                    JSONObject jsonResponse = new JSONObject(response);
//                                    boolean success = jsonResponse.getBoolean("success");
//                                    if (success) {
//                                        Log.d("성공",":::");
//                                        peopleCountIncrease(group);
//                                        SearchGroupPage.super.onBackPressed();
//                                    }
//                                } catch (Exception e){
//                                    e.printStackTrace();
//                                }
//                            }
//                        };
//                        JoinGroupRequest joinGroupRequest = new JoinGroupRequest(userID, group, responseListener);
//                        RequestQueue queue = Volley.newRequestQueue(SearchGroupPage.this);
//                        queue.add(joinGroupRequest);
//                    }
//
//                });

//                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }
//        });

        new BackgroundTask().execute();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String link = "https://www.dong0110.com/chatphp/SearchGroup.php?userID=" + userID;
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
                Log.d("응답", ""+jsonArray.length());
                if(jsonArray.length() == 0) return;
                int count = 0;
                String groupName, contents, peopleCount, category, goalTime, master, startDate;
                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    groupName = object.getString("groupName");
                    contents = object.getString("contents");
                    peopleCount = object.getString("count");
                    category = object.getString("category");
                    goalTime = object.getString("goalTime");
                    master = object.getString("master");
                    startDate = object.getString("startDate");
                    Group group = new Group(groupName, contents, peopleCount, category, goalTime, master, startDate);
                    Log.d("불러오나", groupName +contents + peopleCount + category + goalTime + master + startDate);
                    groupList.add(group);
                    adapter.notifyDataSetChanged();
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}