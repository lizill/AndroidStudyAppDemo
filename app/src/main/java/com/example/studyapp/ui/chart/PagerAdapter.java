package com.example.studyapp.ui.chart;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new WeekFragment();
            case 2:
                return new MonthFragment();
            default:
                return new DayFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
