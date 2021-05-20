package com.example.studyapp.ui.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

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


public class WeekFragment extends Fragment implements OnChartValueSelectedListener {

    private BarChart barChart;
    private PieChart piechart;
    private RequestQueue requestQueue;

    //time format setting
    private TimeZone tz;
    private DateFormat timeFormat = new SimpleDateFormat("a HH mm", Locale.KOREA);
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    private DateFormat dayFormat = new SimpleDateFormat("MM월 dd일 E요일", Locale.KOREA);


    //barchart variable
    private float [] dateArray;
    private float [] timeArray;
    private float max = 0;

    //Information variable
    private String aveStudyTimeOnWeek,today;
    private float allStudyTimeSecOnWeek, sumDayStartEndTerm;

    //color setting
    private int [] colorList = new int [] {Color.parseColor("#008cff"), Color.parseColor("#5056bf"), Color.parseColor("#2e38ff"),
            Color.parseColor("#2caee6"), Color.parseColor("#30cf9c"), Color.parseColor("#4faaff"),};

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

            tv_week_date = (TextView) v.findViewById(R.id.tv_week_date);

            barChart = (com.github.mikephil.charting.charts.HorizontalBarChart) v.findViewById(R.id.week_barChart);

            piechart = (com.github.mikephil.charting.charts.PieChart) v.findViewById(R.id.week_piechart);
            searchInfo();

        }
        return v;
    }
    //PieChart data setting
    private void setPieChartData(){

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(allStudyTimeSecOnWeek, "공부"));
        entries.add(new PieEntry(sumDayStartEndTerm - allStudyTimeSecOnWeek, "휴식"));

        PieDataSet set = new PieDataSet(entries, "Study Information");
        set.setColors(colorList);
        PieData data = new PieData(set);
        piechart.setData(data);
        piechart.invalidate();
    }
    private void createBarChartDataArray(int len){
        dateArray = new float [len];
        timeArray = new float [len];
    }
    private void setBarData(){
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
        yLeft.setAxisMaximum(max);
        yLeft.setAxisMinimum(0f);
        yLeft.setEnabled(false);

        xAxis.setLabelCount(7);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(){
            private String [] dayslist = {"월", "화", "수", "목", "금", "토", "일"};
            @Override
            public String getFormattedValue(float value) {
                return dayslist[(int)value];
            }
        });
        xAxis.setTextColor(Color.parseColor("#000000"));

        YAxis yRight = barChart.getAxisRight();
        yRight.setDrawAxisLine(true);
        yRight.setDrawGridLines(false);
        yRight.setEnabled(false);


        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int j = 0;
        for(int i = 0; i < 7; i++){
            if(i == dateArray[j]){
                barEntries.add(new BarEntry(dateArray[j], timeArray[j]));
                j++;
            }
            if(j >= dateArray.length) break;
        }

        BarDataSet dataSet = new BarDataSet(barEntries, "요일별 공부");
        dataSet.setDrawValues(false);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        barChart.animateY(1000);

        barChart.setData(data);
        barChart.invalidate();
    }

    private void searchInfo(){
        String url = String.format(Env.weekInfo2URL, userID,today);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONArray jsonArray2 = jsonObject.getJSONArray("response2");
                            JSONArray jsonArray3 = jsonObject.getJSONArray("response3");
                            JSONArray jsonArray4 = jsonObject.getJSONArray("response4");

                            JSONObject studyObject = jsonArray.getJSONObject(0);
                            JSONObject studyObject2 = jsonArray2.getJSONObject(0);
                            JSONObject weekObject = jsonArray3.getJSONObject(0);

                            String allStudyTimeOnWeek = studyObject.getString("AllStudyTimeOnWeek");
                            tv_week_totalTime.setText(allStudyTimeOnWeek);

                            allStudyTimeSecOnWeek = Float.parseFloat(studyObject.getString("AllStudyTimeSecOnWeek"));

                            aveStudyTimeOnWeek = studyObject.getString("AverageStudyTimeOnWeek");
                            tv_week_average.setText(aveStudyTimeOnWeek);

                            sumDayStartEndTerm = Float.parseFloat(studyObject2.getString("SumDayStartEndTerm"));

                            String firstDay = weekObject.getString("week_first_day");
                            String lastDay = weekObject.getString("week_last_day");

                            tv_week_date.setText(String.format("%s일 ~ %s일",firstDay,lastDay));

                            int len = jsonArray4.length();
                            createBarChartDataArray(len);

                            for(int i = 0; i < len; i++){
                                JSONObject barChartObject = jsonArray4.getJSONObject(i);

                                dateArray[i] = Float.parseFloat(barChartObject.getString("study_date"));
                                timeArray[i] = Float.parseFloat(barChartObject.getString("study_time"));

                                if(max < timeArray[i])
                                    max = timeArray[i];
                            }

                            setBarData();
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}