package com.example.studyapp.ui.chart;

import android.app.Activity;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.icu.text.Edits;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.FirstActivity;
import com.example.studyapp.R;
import com.example.studyapp.ui.home.HomeFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class ChartFragment extends Fragment {
    private Activity activity;
    private ChartViewModel chartViewModel;
    private RequestQueue requestQueue;
    private MaterialCalendarView materialCalendarView;
    private String userID;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private DateFormat df;
    private String [] titles = new String[]{"일간", "주간", "월간"};
    private Map<String, Boolean> studyRecordMap = new HashMap();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        chartViewModel = new ViewModelProvider(this).get(ChartViewModel.class);
        View root = inflater.inflate(R.layout.activity_chart, container, false);

        userID = FirstActivity.userInfo.getString("userId", null);

        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        df.setTimeZone(tz);
        String today = df.format(new Date());

        //Volley Queue  & request json
        requestQueue = Volley.newRequestQueue(getContext());
        parseCalendarInfo(today);

        OneDayDecorator oneDayDecorator = new OneDayDecorator();

        materialCalendarView = root.findViewById(R.id.calendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());


        // calendar view setting
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2000, 1, 1))
                .setMaximumDate(CalendarDay.from(2049, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                String tDate = df.format(date.getDate());
                Calendar c = date.getCalendar();
                c.setFirstDayOfWeek(Calendar.MONDAY);
                int week = c.get(c.WEEK_OF_YEAR);
                System.out.println(week + "클릭");

                DayFragment dayFragment= null;
                WeekFragment weekFragment = null;
                MonthFragment monthFragment = null;
                if(studyRecordMap.get(tDate) == null){
                    dayFragment = new DayFragment(tDate, 0);
                }else{
                    dayFragment = new DayFragment(tDate, 1);
                }
                if(studyRecordMap.get("w" + week) == null){
                    weekFragment = new WeekFragment(tDate, 0);
                }else{
                    weekFragment = new WeekFragment(tDate, 1);
                }
                if(studyRecordMap.get("m" + (date.getMonth() + 1)) == null){
                    monthFragment = new MonthFragment(tDate, 0);
                }else{
                    monthFragment = new MonthFragment(tDate, 1);
                }
                setCreateTabLayout(new Fragment[] {dayFragment, weekFragment, monthFragment});
            }
        });

        //Decorator event  customizing
        materialCalendarView.addDecorators(
                new MySelectorDecorator(getActivity()),
                new SundayDecorator(),
                new SaturdayDecoractor(),
                oneDayDecorator,
                new EventDecorator(Color.RED, Collections.singleton(CalendarDay.today()))
        );
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String tDate = String.format("%s-%s-%s", date.getYear(), date.getMonth() + 1, date.getDay());
                parseCalendarInfo(tDate);
            }
        });

        viewPager2 = root.findViewById(R.id.pager);
        tabLayout = root.findViewById(R.id.tab_layout);

        viewPager2.setAdapter(new PagerAdapter(getActivity()));

        setStartTabLayout(today);
        chartViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }
    private void setStartTabLayout(String today){
        DayFragment dayFragment= null;
        WeekFragment weekFragment = null;
        MonthFragment monthFragment = null;

        if(HomeFragment.isDayFragment){
            dayFragment = new DayFragment(today, 1);
        }else{
            dayFragment = new DayFragment(today, 0);
        }
        if(HomeFragment.isWeekFragment){
            weekFragment = new WeekFragment(today, 1);
        }else{
            weekFragment = new WeekFragment(today, 0);
        }
        if(HomeFragment.isMonthFragment){
            monthFragment = new MonthFragment(today, 1);
        }else{
            monthFragment = new MonthFragment(today, 0);
        }
        setCreateTabLayout(new Fragment[] {dayFragment,weekFragment,monthFragment});
    }

    private void setCreateTabLayout(Fragment [] fragments){
        PagerAdapter pagerAdapter2 = new PagerAdapter(getActivity());
        for(Fragment frag : fragments){
            pagerAdapter2.addFrag(frag);
        }
        viewPager2.setAdapter(pagerAdapter2);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(titles[position])).attach();
    }



    // _GET request json
    private void parseCalendarInfo(String d){
        String url = String.format(Env.getDaysStudyTimeInfoURL,userID, d);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            //json object >> {response:[{key : value}, {.....
                            JSONObject jsonObject = new JSONObject(response);
                            //object start name : response  >>>>> array
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONArray jsonArray2 = jsonObject.getJSONArray("response2");
                            JSONArray jsonArray3 = jsonObject.getJSONArray("response3");

                            if(jsonArray != null){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    // {time : ? , date : ? } ......
                                    JSONObject studyObject = jsonArray.getJSONObject(i);
                                    String date = studyObject.getString("study_date");
                                    String time = studyObject.getString("study_time");
                                    materialCalendarView.addDecorator(new StudyDate(activity, time, date));
                                    studyRecordMap.put(date, true);
                                }
                            }
                            if(jsonArray2 != null){
                                for(int i = 0; i < jsonArray2.length(); i++){
                                    // {time : ? , date : ? } ......
                                    JSONObject studyObject = jsonArray2.getJSONObject(i);
                                    String week = studyObject.getString("week");
                                    System.out.println(week);
                                    studyRecordMap.put("w"+week, true);
                                }
                            }
                            JSONObject jsonObject1 = jsonArray3.getJSONObject(0);
                            String month = jsonObject1.getString("month");
                            if(!month.equals("null")){
                                studyRecordMap.put("m"+Integer.parseInt(month), true);
                            }

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
        requestQueue.add(request);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof Activity)
            activity = (Activity) context;
    }
}