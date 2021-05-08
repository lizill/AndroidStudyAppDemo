package com.example.studyapp.ui.chart;

import android.content.Context;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class DayFragment extends Fragment {

    private PieChart piechart;
    private HorizontalBarChart barChart;
    private RequestQueue requestQueue;

    //time-line variable
    private String [] subjectLog,startLog,quitLog,focusLog;
    private RecyclerView recyclerView;
    private List<TimeLineModel> timeLineModelList;
    private TimeLineModel[] timeLineModel;
    private LinearLayoutManager linearLayoutManager;

    //time format setting
    private TimeZone tz;
    private DateFormat timeFormat = new SimpleDateFormat("a HH mm", Locale.KOREA);
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    private DateFormat dayFormat = new SimpleDateFormat("MM월 dd일 E요일", Locale.KOREA);

    //Information variable
    private String MaxFocus,MinStartTime,MaxEndTime, effectiveTime,today;

    //barchart variable
    private ArrayList<String> allSubject;
    private ArrayList<String> timeBySubject;

    //color setting
    private int [] c = new int [] {Color.parseColor("#b0adff"), Color.parseColor("#00ccff")};

    private String userID;

    //TextView variable
    TextView tv_totalTime,tv_longTime,tv_startTime,tv_endTime,tv_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //data exist ? DayFragment : NoneFragment
        View v = null;
        if(HomeFragment.TOTAL_STUDY_TIME.equals("00:00:00")){
            v = inflater.inflate(R.layout.fragment_nonpage, container, false);
        }else{
            v = inflater.inflate(R.layout.fragment_day, container, false);

            //Volley Queue  & request json
            requestQueue = Volley.newRequestQueue(getContext());

            // Time -> Korea setting
            tz = TimeZone.getTimeZone("Asia/Seoul");
            timeFormat.setTimeZone(tz);
            dateFormat.setTimeZone(tz);
            dayFormat.setTimeZone(tz);

            tv_totalTime = (TextView) v.findViewById(R.id.tv_totalTime);
            tv_totalTime.setText(HomeFragment.TOTAL_STUDY_TIME);

            tv_longTime = (TextView) v.findViewById(R.id.tv_longTime);
            tv_startTime = (TextView) v.findViewById(R.id.tv_startTime);
            tv_endTime = (TextView) v.findViewById(R.id.tv_endTime);

            //userID 받아오기
            userID = FirstActivity.userInfo.getString("userId", null);

            //현재 날짜 요일 불러오기
            today = dateFormat.format(new Date());
            String day = dayFormat.format(new Date());

            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_date.setText(day);

            searchInfo();


//            barChart = (HorizontalBarChart) v.findViewById(R.id.barChart);
//            searchBarChartData();

            piechart = (com.github.mikephil.charting.charts.PieChart) v.findViewById(R.id.piechart);

            recyclerView = (RecyclerView) v.findViewById(R.id.rv);
            searchTimelineData();

        }

        return v;
    }
    private void setTimelineData(){
        //Timeline setting
//            searchTimelineData();
        timeLineModelList = new ArrayList<>();
//        setTimeData();
        int size = subjectLog.length;
        timeLineModel = new TimeLineModel[size];

//            SubjectLog StartLog QuitLog FocusLog

        for (int i = 0; i < size; i++) {
            timeLineModel[i] = new TimeLineModel();
            timeLineModel[i].setTitle(subjectLog[i]);
            timeLineModel[i].setTime(String.format("%s - %s",startLog[i],quitLog[i]));
            timeLineModel[i].setTerm(focusLog[i]);
            timeLineModelList.add(timeLineModel[i]);
        }
        Context context = getContext();

        linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new TimeLineAdapter(timeLineModelList, context));
    }
    private float toFloatTime(String timeData){
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

    //PieChart data setting
    private void setPieChartData(){

        float minute = toFloatTime(HomeFragment.TOTAL_STUDY_TIME);

        //오늘 공부 끝낸 시간 - 시작 시간 = Term
        float total = toFloatTime(effectiveTime);

        System.out.println("minute : " + minute);
        System.out.println("total : "  + total);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(minute, "오늘 공부량"));
        entries.add(new PieEntry(total, "현재 시간 대비 공부량"));

        PieDataSet set = new PieDataSet(entries, "Study Information");
        set.setColors(c);
        PieData data = new PieData(set);
        piechart.setData(data);
        piechart.invalidate();
    }
    // BarChart data setting
    private void setBarData(){
//        allSubject, timeBySubject

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> xAxisName = new ArrayList<>();
//        barEntries.add(new BarEntry(1f, 10f));
//        xAxisName.add("Name 1");
        for(int i = 0; i < allSubject.size(); i++){
            xAxisName.add(allSubject.get(i));
//            barEntries.add(new BarEntry();
        }

        setBar(barEntries,xAxisName);
    }
    private void setBar(ArrayList<BarEntry> arrayList, final ArrayList<String> xAxisValues){
        barChart.setDrawBarShadow(false);
        barChart.setFitBars(true);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(25);
        barChart.setPinchZoom(true);

        barChart.setDrawGridBackground(true);
        BarDataSet barDataSet = new BarDataSet(arrayList, "Values");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        barData.setValueTextSize(0f);

        barChart.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
        barChart.setDrawGridBackground(false);

        Legend l = barChart.getLegend(); // Customize the ledgends
        l.setTextSize(10f);
        l.setFormSize(10f);
//To set components of x axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextSize(13f);
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        xAxis.setDrawGridLines(false);

        barChart.setData(barData);
    }
    private void searchBarChartData(){
        String url = String.format(Env.BarchartURL, userID,today);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            //json object >> {response:[{key : value}, {.....
                            JSONObject jsonObject = new JSONObject(response);

                            //object start name : response  >>>>> array
                            JSONArray jsonArray = jsonObject.getJSONArray("response");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject studyObject = jsonArray.getJSONObject(i);
                                String subject = studyObject.getString("study_subject");
                                String subjectStudyTime = studyObject.getString("study_time");

                                allSubject.add(subject);
                                timeBySubject.add(subjectStudyTime);
                            }

                            setBarData();

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
    private void createTimelineDataArray(int len){
        subjectLog = new String [len];
        startLog = new String [len];
        quitLog = new String [len];
        focusLog = new String [len];
    }
    private void searchTimelineData(){
        String url = String.format(Env.TimelineURL, userID,today);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            //json object >> {response:[{key : value}, {.....
                            JSONObject jsonObject = new JSONObject(response);

                            //object start name : response  >>>>> array
                            JSONArray jsonArray = jsonObject.getJSONArray("response");

                            int len = jsonArray.length();
                            createTimelineDataArray(len);

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject studyObject = jsonArray.getJSONObject(i);
                                String subject = studyObject.getString("study_subject");
                                String start = studyObject.getString("study_start");
                                String quit = studyObject.getString("study_end");
                                String focus = studyObject.getString("focusOn");

                                subjectLog[i] = subject;
                                startLog[i] = start;
                                quitLog[i] = quit;
                                focusLog[i] = focus;
                            }

                            setTimelineData();

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
    private void searchInfo(){
        String url = String.format(Env.InfoURL, userID,today);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            //json object >> {response:[{key : value}, {.....
                            JSONObject jsonObject = new JSONObject(response);

                            //object start name : response  >>>>> array
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject studyObject = jsonArray.getJSONObject(0);
                            MinStartTime = studyObject.getString("START");
                            MaxEndTime = studyObject.getString("END");
                            MaxFocus = studyObject.getString("focusOn");
                            effectiveTime = studyObject.getString("TERM");

                            System.out.println("effectiveTime : " + effectiveTime);


                            tv_longTime.setText(MaxFocus);
                            tv_startTime.setText(MinStartTime);
                            tv_endTime.setText(MaxEndTime);

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