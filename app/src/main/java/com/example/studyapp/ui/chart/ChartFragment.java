package com.example.studyapp.ui.chart;

import android.graphics.Color;
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
import com.example.studyapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ChartFragment extends Fragment {

    private ChartViewModel chartViewModel;
    private RequestQueue requestQueue;
    private String userID = "gjgjajaj";
    private ArrayList<String> studyTimes = new ArrayList<>();
    private ArrayList<String> studyDates = new ArrayList<>();
    private MaterialCalendarView materialCalendarView;

    private ViewPager2 viewPager2;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        chartViewModel = new ViewModelProvider(this).get(ChartViewModel.class);
        View root = inflater.inflate(R.layout.activity_chart, container, false);

        //Volley Queue  & request json
        requestQueue = Volley.newRequestQueue(getContext());
        parseJson();

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

        //Decorator event  customizing
        materialCalendarView.addDecorators(
                new MySelectorDecorator(getActivity()),
                new SundayDecorator(),
                new SaturdayDecoractor(),
                oneDayDecorator,
                new EventDecorator(Color.RED, Collections.singleton(CalendarDay.today()))
        );

        viewPager2 = root.findViewById(R.id.pager);
        viewPager2.setAdapter(new PagerAdapter(getActivity()));

        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position)
                        {
                            case 0:{
                                tab.setText("일간");
                                break;
                            }
                            case 1:{
                                tab.setText("주간");
                                break;
                            }
                            case 2:{
                                tab.setText("월간");
                                break;
                            }
                        }
                    }
                });
        tabLayoutMediator.attach();



        chartViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }
    // _GET request json
    private void parseJson(){
        String url = String.format("http://gjgjajaj.dothome.co.kr/study.php?userID=%s",userID);
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

                                // {time : ? , date : ? } ......
                                JSONObject studyObject = jsonArray.getJSONObject(i);
                                String time = studyObject.getString("studyTime");
                                String date = studyObject.getString("studyDate");
                                studyTimes.add(time);
                                studyDates.add(date);
                                materialCalendarView.addDecorator(new StudyDate(getActivity(), time, date));
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
}