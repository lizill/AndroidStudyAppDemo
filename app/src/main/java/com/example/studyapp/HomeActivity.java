package com.example.studyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    ViewPager page;
    String[] name ={"STATISTIC","PLAN","HOME","RANK","GROUP"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        page = (ViewPager)findViewById(R.id.page);
        Button[] btns = new Button[5];
        btns[0] = (Button)findViewById(R.id.btn1);
        btns[1] = (Button)findViewById(R.id.btn2);
        btns[2] = (Button)findViewById(R.id.btn3);
        btns[3] = (Button)findViewById(R.id.btn4);
        btns[4] = (Button)findViewById(R.id.btn5);

        page.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        page.setCurrentItem(2);
        btns[2].setSelected(true);
        for(int i =0;i<btns.length;i++){

            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int tag = (int) v.getTag();
                    page.setCurrentItem(tag);
                    for(int i = 0;i<btns.length;i++){
                        if(tag==i){
                            btns[i].setSelected(true);
                        }else{
                            btns[i].setSelected(false);
                        }
                    }
                }
            });
            btns[i].setTag(i);
        }
    }


    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {

            switch(position) {
                case 0:
                    return new page1();
                case 1:
                    return new page2();
                case 2:
                    return new page3();
                case 3:
                    return new page4();
                case 4:
                    return new page5();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}