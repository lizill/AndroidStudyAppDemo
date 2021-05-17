package com.example.studyapp.ui.group;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class GroupFragment extends Fragment {
    private RecyclerView groupRecyclerView;
    private GroupRecyclerAdapter adapter;
    private ArrayList<Group> groupList;
    private String userID;
    private Button searchGroupButton;

    private GroupViewModel groupViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        groupViewModel =
                new ViewModelProvider(this).get(GroupViewModel.class);
        View root = inflater.inflate(R.layout.activity_group_main, container, false);
        final TextView textView = root.findViewById(R.id.text_group);
        groupViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                groupList = new ArrayList<Group>();
                groupRecyclerView = root.findViewById(R.id.groupRecyclerView);
                groupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new GroupRecyclerAdapter(getContext(), groupList);
                groupRecyclerView.setAdapter(adapter);

                userID = userInfo.getString(USER_ID,null);

                searchGroupButton = (Button)root.findViewById(R.id.SearchGroup);
                searchGroupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SearchGroupPage.class);
                        startActivity(intent);
                    }
                });
                new BackgroundTask().execute();
            }
        });
        return root;
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String link = "https://www.dong0110.com/chatphp/GroupList.php?userID=" + userID;
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
                String groupName, contents, peopleCount, category, goalTime, master, startDate, memberLimit;
                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    groupName = object.getString("groupName");
                    contents = object.getString("contents");
                    peopleCount = object.getString("count");
                    category = object.getString("category");
                    goalTime = object.getString("goalTime");
                    master = object.getString("master");
                    startDate = object.getString("startDate");
                    memberLimit = object.getString("peopleLimit");
                    Group group = new Group(groupName, contents, peopleCount, category, goalTime, master, startDate, memberLimit);
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
