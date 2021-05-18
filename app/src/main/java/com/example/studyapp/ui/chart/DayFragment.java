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
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
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
import java.util.Arrays;
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
    private String MaxFocus,MinStartTime,MaxEndTime, totalTime, today;
    private float totalTimeSec, sumDayStartEndTerm;
    //barchart variable
    private String [] allSubject;
    private float [] timeBySubject;

    //color setting
    private int [] colorList = new int [] {Color.parseColor("#b0adff"), Color.parseColor("#00ccff")};
    private int [] barColorList = new int [] {Color.parseColor("#ff6161"), Color.parseColor("#faa628"), Color.parseColor("#16de20"),
            Color.parseColor("#2e38ff"), Color.parseColor("#13187d"), Color.parseColor("#abafff"),};

    private String userID;

    //TextView variable
    TextView tv_totalTime,tv_longTime,tv_startTime,tv_endTime,tv_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //data exist ? DayFragment : NoneFragment
        View v = null;
        if(!HomeFragment.isDayFragment){
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


            barChart = (HorizontalBarChart) v.findViewById(R.id.barChart);

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

    //PieChart data setting
    private void setPieChartData(){

        //오늘 공부 시간
        float todayStudyTime = totalTimeSec;

        //오늘 공부 끝낸 시간 - 시작 시간 = Term
        float totalTerm = sumDayStartEndTerm;

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(todayStudyTime, "공부"));
        entries.add(new PieEntry(totalTerm - todayStudyTime, "휴식"));

        PieDataSet set = new PieDataSet(entries, "Study Information");
        set.setColors(colorList);
        PieData data = new PieData(set);
        piechart.setData(data);
        piechart.invalidate();
    }

    // BarChart data setting
    private void setBarData(){
//        allSubject, timeBySubject

        barChart.setDrawBarShadow(false);
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.getLegend().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setClickable(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDrawValueAboveBar(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(false);

        YAxis yLeft = barChart.getAxisLeft();
        yLeft.setAxisMaximum(totalTimeSec);
        yLeft.setAxisMinimum(0f);
        yLeft.setEnabled(false);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("ds");
        xAxis.setLabelCount(allSubject.length);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(allSubject));

        YAxis yRight = barChart.getAxisRight();
        yRight.setDrawAxisLine(true);
        yRight.setDrawGridLines(false);
        yRight.setEnabled(false);


        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < timeBySubject.length; i++){
            float val = timeBySubject[i];
            barEntries.add(new BarEntry(i, val));
        }

        BarDataSet dataSet = new BarDataSet(barEntries, "과목별 공부");
        dataSet.setDrawValues(false);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        data.setValueTextSize(10);

        barChart.animateY(1000);

        barChart.setDrawBarShadow(true);
        dataSet.setBarShadowColor(Color.argb(40,150,150,150));

        barChart.setData(data);
        data.setValueFormatter(new IndexAxisValueFormatter());
        barChart.invalidate();
    }

    private void searchBarChartData(){
        String url = String.format(Env.barchartURL, userID,today);
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
                            createBarChartDataArray(len);

                            for(int i = 0; i < len; i++){
                                JSONObject studyObject = jsonArray.getJSONObject(i);
                                String subject = studyObject.getString("study_subject");
                                String subjectStudyTime = studyObject.getString("study_time");

                                allSubject[i] = subject;
                                timeBySubject[i] = Float.parseFloat(subjectStudyTime);

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
    private void createBarChartDataArray(int len){
        allSubject = new String [len];
        timeBySubject = new float [len];
    }

    private void createTimelineDataArray(int len){
        subjectLog = new String [len];
        startLog = new String [len];
        quitLog = new String [len];
        focusLog = new String [len];
    }
    private void searchTimelineData(){
        String url = String.format(Env.timelineURL, userID,today);
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
        String url = String.format(Env.info2URL, userID,today);
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
                            JSONObject studyObject2 = jsonArray.getJSONObject(1);

                            MinStartTime = studyObject.getString("START");
                            MaxEndTime = studyObject.getString("END");
                            MaxFocus = studyObject.getString("focusOn");
                            sumDayStartEndTerm = Float.parseFloat(studyObject.getString("TERM"));
                            totalTime = studyObject2.getString("TOTAL");
                            totalTimeSec = Float.parseFloat(studyObject2.getString("TOTALSEC"));


                            tv_longTime.setText(MaxFocus);
                            tv_startTime.setText(MinStartTime);
                            tv_endTime.setText(MaxEndTime);
                            tv_totalTime.setText(totalTime);

                            setPieChartData();
                            searchBarChartData();

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