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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#39C0FA")));
//        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#39C0FA")));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Button[] btns = new Button[5];
        btns[0] = (Button)findViewById(R.id.btn1);
        btns[1] = (Button)findViewById(R.id.btn2);
        btns[2] = (Button)findViewById(R.id.btn3);
        btns[3] = (Button)findViewById(R.id.btn4);
        btns[4] = (Button)findViewById(R.id.btn5);


        btns[2].setSelected(true);
        for(int i =0;i<btns.length;i++){

            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int tag = (int) v.getTag();

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
//            System.out.println(page.getCurrentItem());
//            switch(page.getCurrentItem()) {
//                case 0:
//                    setTitle("STATISTIC");
//                    break;
//                case 1:
//                    setTitle("PLAN");
//                    break;
//                case 2:
//                    setTitle("HOME");
//                    break;
//                case 3:
//                    setTitle("RANK");
//                    break;
//                case 4:
//                    setTitle("GROUP");
//                    break;
//            }

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

//        case 0:
//        setTitle("STATISTIC");
//                    return new page1();
//                case 1:
//        setTitle("PLAN");
//                    return new page2();
//                case 2:
//        setTitle("HOME");
//                    return new page3();
//                case 3:
//        setTitle("RANK");
//                    return new page4();
//                case 4:
//        setTitle("GROUP");
//                    return new page5();

        @Override
        public int getCount() {
            return 5;
        }
    }
}