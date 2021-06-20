package com.example.studyapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
<<<<<<< HEAD

import android.widget.EditText;
import android.widget.LinearLayout;
=======
import android.widget.EditText;
>>>>>>> 46e6dc88e85b4aef9baf959afc20deeeee334b32
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.FirstActivity;
import com.example.studyapp.R;
import com.example.studyapp.recycle.HomeAdapter;
import com.example.studyapp.recycle.HomeData;
import com.example.studyapp.ui.chart.Env;
import com.example.studyapp.ui.group.Group;
import com.example.studyapp.ui.group.GroupFragment;
import com.example.studyapp.ui.group.GroupRecyclerAdapter;
import com.example.studyapp.ui.group.SearchGroupPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.socket.client.Socket;

import static com.example.studyapp.FirstActivity.USER_ID;
import static com.example.studyapp.FirstActivity.userInfo;

public class HomeFragment extends Fragment {

    private ArrayList<HomeData> arrayList;
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EditText editTextTextPersonName;
<<<<<<< HEAD
    private RecyclerView subjectRecyclerView;
    public static Socket mSocket;


    private HomeViewModel homeViewModel;

    private TextView tv_data;
    private RequestQueue requestQueue;
    private String today,userID, subject;
    public static String TOTAL_STUDY_TIME;
    public static View root;
=======
    private HomeViewModel homeViewModel;

//    private Button sub1;
    private TextView tv_data;
    private RequestQueue requestQueue;
    private String today,userID,subject;
    public static View root;
    public static boolean isDayFragment, isWeekFragment,isMonthFragment;
>>>>>>> 46e6dc88e85b4aef9baf959afc20deeeee334b32

    public static boolean isDayFragment, isWeekFragment,isMonthFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
<<<<<<< HEAD

        editTextTextPersonName = (EditText)root.findViewById(R.id.editTextTextPersonName);
        recyclerView = (RecyclerView) root.findViewById(R.id.rv_home);
        linearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        homeAdapter = new HomeAdapter(arrayList);
        recyclerView.setAdapter(homeAdapter);

=======

        editTextTextPersonName = (EditText)root.findViewById(R.id.editTextTextPersonName);
        recyclerView = (RecyclerView)root.findViewById(R.id.rv_home);
        linearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        homeAdapter = new HomeAdapter(arrayList);
        recyclerView.setAdapter(homeAdapter);

>>>>>>> 46e6dc88e85b4aef9baf959afc20deeeee334b32
        subject = StopwatchActivity.subject;
        userID = FirstActivity.userInfo.getString("userId", null);

        //현재 날짜 불러오기
        TimeZone tz;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        tz = TimeZone.getTimeZone("Asia/Seoul");
        dateFormat.setTimeZone(tz);
        Date date = new Date();
        today = dateFormat.format(date);



        //Volley Queue  & request json
        requestQueue = Volley.newRequestQueue(getContext());
        totalStudyTime();
        totalSubject();


<<<<<<< HEAD

=======
>>>>>>> 46e6dc88e85b4aef9baf959afc20deeeee334b32
        tv_data = (TextView) root.findViewById(R.id.tv_data);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                /*
                onChanged= 뷰를 눌러서 실행했을때 실행시킬 이벤트 삽입
                 */
                Button addButton = (Button)root.findViewById(R.id.addButton);
                addButton.setOnClickListener(new View.OnClickListener(){
                    @Override
<<<<<<< HEAD
                    public void onClick(View v){
=======
                    public void onClick(View v) {
>>>>>>> 46e6dc88e85b4aef9baf959afc20deeeee334b32
                        HomeData homeData = null;
                        homeData = new HomeData(editTextTextPersonName.getText().toString(),"00:00:00");
                        plusSubject();
                        arrayList.add(homeData);
                        homeAdapter.notifyDataSetChanged();
                    }

<<<<<<< HEAD
=======
//                    @Override
//                    public void onClock(View v){
//                        HomeData homeData = null;
//                    }
>>>>>>> 46e6dc88e85b4aef9baf959afc20deeeee334b32
                });
            }
        });
        return root;
    }


    private void totalStudyTime() {
        String url = String.format(Env.totalURL2, userID, today);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
<<<<<<< HEAD

                        //json object >> {response:[{key : value}, {.....
                        JSONObject jsonObject = new JSONObject(response);

                        //object start name : response  >>>>> array
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        JSONObject studyObject = jsonArray.getJSONObject(0);
                        String todayStudyTime = studyObject.getString("study_time");

                        JSONObject studyObject2 = jsonArray.getJSONObject(1);
                        String study_week_time = studyObject2.getString("study_week_time");

                        JSONObject studyObject3 = jsonArray.getJSONObject(2);
                        String study_month_time = studyObject3.getString("study_month_time");

                        if(!study_week_time.equals("null")) isWeekFragment = true;
                        if(!study_month_time.equals("null")) isMonthFragment = true;

                        if(!todayStudyTime.equals("null")){
                            isDayFragment = true;
                        }else{
                            todayStudyTime = "00:00:00";
                        }

                        tv_data.setText(todayStudyTime);






//                        //json object >> {response:[{key : value}, {.....
//                        JSONObject jsonObject = new JSONObject(response);
//                        //object start name : response  >>>>> array
//                        JSONArray jsonArray = jsonObject.getJSONArray("response");
//                        JSONObject studyObject = jsonArray.getJSONObject(0);
//
//                        String studyTime = studyObject.getString("study_time");
//
//
//
//                        if(studyTime.equals("null")){
//                            TOTAL_STUDY_TIME = "00:00:00";
//                            tv_data.setText(TOTAL_STUDY_TIME);
//                        }else{
//                            TOTAL_STUDY_TIME = studyTime;
//                            tv_data.setText(TOTAL_STUDY_TIME);
//                        }
//                        System.out.println(TOTAL_STUDY_TIME);
=======
                        //json object >> {response:[{key : value}, {.....
                        JSONObject jsonObject = new JSONObject(response);

                        //object start name : response  >>>>> array
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        JSONObject studyObject = jsonArray.getJSONObject(0);
                        String todayStudyTime = studyObject.getString("study_time");

                        JSONObject studyObject2 = jsonArray.getJSONObject(1);
                        String study_week_time = studyObject2.getString("study_week_time");

                        JSONObject studyObject3 = jsonArray.getJSONObject(2);
                        String study_month_time = studyObject3.getString("study_month_time");

                        if(!study_week_time.equals("null")) isWeekFragment = true;
                        System.out.println("isWeekFragment : " + study_week_time);
                        if(!study_month_time.equals("null")) isMonthFragment = true;

                        if(!todayStudyTime.equals("null")){
                            isDayFragment = true;
                        }else{
                            todayStudyTime = "00:00:00";
                        }

                        tv_data.setText(todayStudyTime);

>>>>>>> 46e6dc88e85b4aef9baf959afc20deeeee334b32
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        requestQueue.add(request);
    }

<<<<<<< HEAD

=======
>>>>>>> 46e6dc88e85b4aef9baf959afc20deeeee334b32
    private void totalSubject() {
        String url = String.format(Env.subjectNameURL, userID, today);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        int count  = 0;
                        while(count<jsonArray.length()) {
                            JSONObject studyObject = jsonArray.getJSONObject(count);
                            String subjectName = studyObject.getString("subject"+count);
                            String todaySubjectName = studyObject.getString("todaySubjectName"+count);
                            String todaySubjectTime = studyObject.getString("todaySubjectTime"+count);
                            if(todaySubjectTime.equals("")||todaySubjectTime.equals("null")){
                                todaySubjectTime = "00:00:00";
                            }
                            HomeData returnSubjects = new HomeData(subjectName,todaySubjectTime);
                            arrayList.add(returnSubjects);
                            homeAdapter.notifyDataSetChanged();
                            count++;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void plusSubject() {
        String url = Env.PlusSubjectURL;
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = simpleDateFormat.format(mDate);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("success");
                Toast.makeText( getActivity(), res, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })  {
            @Override
            protected Map<String, String> getParams() {
                String SubjectNames = editTextTextPersonName.getText().toString();
                Map<String,String> params = new HashMap<>();
                params.put("SubjectNames", SubjectNames);
                params.put("userID",userID);
                params.put("getTime",getTime);
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }

<<<<<<< HEAD
=======


>>>>>>> 46e6dc88e85b4aef9baf959afc20deeeee334b32
}