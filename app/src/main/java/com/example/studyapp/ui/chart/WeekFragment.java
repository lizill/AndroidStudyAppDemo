package com.example.studyapp.ui.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import com.example.studyapp.ui.home.HomeFragment;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class WeekFragment extends Fragment {


    private PieChart piechart;
    private RequestQueue requestQueue;

    //time format setting
    private TimeZone tz;
    private DateFormat timeFormat = new SimpleDateFormat("a HH mm", Locale.KOREA);
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    private DateFormat dayFormat = new SimpleDateFormat("MM월 dd일 E요일", Locale.KOREA);

    //Information variable
    private String allStudyTimeOnWeek,aveStudyTimeOnWeek,sumDayStartEndTerm,today;

    //color setting
    private int [] colorList = new int [] {Color.parseColor("#b0adff"), Color.parseColor("#00ccff")};

    private String userID;

    //TextView variable
    TextView tv_week_totalTime,tv_week_average,tv_week_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //data exist ? DayFragment : NoneFragment
        View v = null;

//        if(HomeFragment.TOTAL_STUDY_TIME.equals("00:00:00")){
        if(!HomeFragment.isWeekFragment){
            v = inflater.inflate(R.layout.fragment_nonpage, container, false);
        }else{
            v = inflater.inflate(R.layout.fragment_week, container, false);

            //Volley Queue  & request json
            requestQueue = Volley.newRequestQueue(getContext());

            // Time -> Korea setting
            tz = TimeZone.getTimeZone("Asia/Seoul");
            timeFormat.setTimeZone(tz);
            dateFormat.setTimeZone(tz);
            dayFormat.setTimeZone(tz);

            tv_week_totalTime = (TextView) v.findViewById(R.id.tv_week_totalTime);
            tv_week_average = (TextView) v.findViewById(R.id.tv_week_average);


            //userID 받아오기
            userID = FirstActivity.userInfo.getString("userId", null);

            //현재 날짜 요일 불러오기
            today = dateFormat.format(new Date());
            String day = dayFormat.format(new Date());

            tv_week_date = (TextView) v.findViewById(R.id.tv_week_date);

            piechart = (com.github.mikephil.charting.charts.PieChart) v.findViewById(R.id.week_piechart);
            searchInfo();

        }
        return v;
    }
    //PieChart data setting
    private void setPieChartData(){

        float studyTotal = timeCastingToFloat(allStudyTimeOnWeek);

        //오늘 공부 끝낸 시간 - 시작 시간 = Term
        float termTotal = timeCastingToFloat(sumDayStartEndTerm);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(studyTotal, "공부"));
        entries.add(new PieEntry(termTotal - studyTotal, "휴식"));

        PieDataSet set = new PieDataSet(entries, "Study Information");
        set.setColors(colorList);
        PieData data = new PieData(set);
        piechart.setData(data);
        piechart.invalidate();
    }
    private float timeCastingToFloat(String timeData){
        String [] t = timeData.split(":");
        float pieTime = 0.0f;
        for(int i = 0; i < t.length; i++){
            float tmp = Float.parseFloat(t[i]);
            if(tmp > 0) {
                switch (i) {
                    case 0:
                        pieTime += tmp * 3600;
                        break;
                    case 1:
                        pieTime += tmp * 60;
                        break;
                    case 2:
                        pieTime += tmp;
                        break;
                }
            }
        }
        return pieTime;
    }
    private void searchInfo(){
        String url = String.format(Env.weekInfoURL, userID,today);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONArray jsonArray2 = jsonObject.getJSONArray("response2");
                            JSONArray jsonArray3 = jsonObject.getJSONArray("response3");

                            JSONObject studyObject = jsonArray.getJSONObject(0);
                            JSONObject studyObject2 = jsonArray2.getJSONObject(0);
                            JSONObject weekObject = jsonArray3.getJSONObject(0);

                            allStudyTimeOnWeek = studyObject.getString("AllStudyTimeOnWeek");
                            tv_week_totalTime.setText(allStudyTimeOnWeek);

                            aveStudyTimeOnWeek = studyObject.getString("AverageStudyTimeOnWeek");
                            tv_week_average.setText(aveStudyTimeOnWeek);

                            sumDayStartEndTerm = studyObject2.getString("SumDayStartEndTerm");

                            String firstDay = weekObject.getString("week_first_day");
                            String lastDay = weekObject.getString("week_last_day");

                            tv_week_date.setText(String.format("%s일 ~ %s일",firstDay,lastDay));

                            setPieChartData();

                        } catch (JSONException e) {
                            e.printStackTrace();
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