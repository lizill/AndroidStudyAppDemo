package com.example.studyapp.ui.rank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.example.studyapp.recycle.RankAdapter;
import com.example.studyapp.recycle.RankData;
import com.example.studyapp.ui.chart.Env;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class RankFragment extends Fragment {
    private View root;
    private RankViewModel rankViewModel;
    private String userID = FirstActivity.userInfo.getString(FirstActivity.USER_ID,null);
    private String userPassword = FirstActivity.userInfo.getString(FirstActivity.USER_PASSWORD,null);

    private ProgressBar progressBar;

    private final static String MONTHRANK = "https://dong0110.com/chatphp/chart/RequireMonthRanking.php";
    private final static String WEEKRANK = "https://dong0110.com/chatphp/chart/RequireWeekRanking.php";
    private final static String DAILYRANK = "https://dong0110.com/chatphp/chart/RequireRanking.php";
    private static String nowRank = DAILYRANK;
    private RequestQueue requestQueue;

    private ArrayList<RankData> recycleArrayList;
    private RankAdapter rankAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rankViewModel =
                new ViewModelProvider(this).get(RankViewModel.class);
        root = inflater.inflate(R.layout.fragment_rank, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) root.findViewById(R.id.rv_rank);
        linearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recycleArrayList = new ArrayList<>();
        rankAdapter = new RankAdapter(recycleArrayList);
        recyclerView.setAdapter(rankAdapter);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date time = new Date();

        Button btn_daily = (Button) root.findViewById(R.id.btn_daily_rank);
        Button btn_week = (Button) root.findViewById(R.id.btn_week_rank);
        Button btn_month = (Button) root.findViewById(R.id.btn_month_rank);

        btn_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData(DAILYRANK,format.format(time));
            }
        });
        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData(WEEKRANK,format.format(time));
            }
        });
        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData(MONTHRANK,format.format(time));
            }
        });

        rankViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                progressBar.setVisibility(View.VISIBLE);



                searchData(DAILYRANK,format.format(time));
                System.out.println(format.format(time));


            }
        });
        return root;
    }


    private void searchData(String date, String time){
        while(recycleArrayList.size()>0){
            recycleArrayList.remove(0);
        };

        String url = String.format(date+"?study_date="+time);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            int first = 0;
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("response");

                            for(int i = 0;i<jsonArray.length();i++){
                                if(i>9) break;
                                JSONObject jo = (JSONObject)jsonArray.get(i);
                                TextView tv_name;
                                TextView tv_time;
                                String joTotalTime = jo.getString("Total");
                                String[] jotime = joTotalTime.split(":");
                                String name;
                                if(jo.getString("Name")==null||jo.getString("Name")=="null"
                                || jo.getString("Name")=="")
                                    name = "미등록 사용자";
                                else
                                    name = jo.getString("Name");
                                int timeSecond = Integer.parseInt(jotime[0])*60*60+
                                        Integer.parseInt(jotime[1])*60+Integer.parseInt(jotime[2]);
                                switch(i){
                                    case 0:
                                        tv_name = root.findViewById(R.id.ranking_1_name);
                                        tv_time = root.findViewById(R.id.ranking_1_time);
                                        first = timeSecond;
                                        break;
                                    case 1:
                                        tv_name = root.findViewById(R.id.ranking_2_name);
                                        tv_time = root.findViewById(R.id.ranking_2_time);
                                        break;
                                    case 2:
                                        tv_name = root.findViewById(R.id.ranking_3_name);
                                        tv_time = root.findViewById(R.id.ranking_3_time);
                                        break;
                                    default:

//                                        static ArrayList<RankData> recycleArrayList;
//                                        static RankAdapter rankAdapter;
//                                        private RecyclerView recyclerView;
//                                        private LinearLayoutManager linearLayoutManager;
                                        RankData rankData = new RankData(
                                                jo.getString("Ranking"),
                                                name,
                                                joTotalTime,
                                                ((timeSecond*100)/first)>0?(timeSecond*100)/first:1,
                                                R.drawable.member_image);
                                        recycleArrayList.add(rankData);
                                        continue;
                                }
                                tv_name.setText(name);
                                tv_time.setText(joTotalTime);
                            }
//                            R.id.memberImage;
                            rankAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try{
                                progressBar.setVisibility(View.GONE);
                            }catch(Exception e2){

                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        requestQueue.add(request);
    }
}
