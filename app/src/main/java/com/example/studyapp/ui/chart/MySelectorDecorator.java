package com.example.studyapp.ui.chart;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.example.studyapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MySelectorDecorator implements DayViewDecorator {
    private final Drawable drawable;

    public MySelectorDecorator(Activity context){
        drawable = context.getResources().getDrawable(R.drawable.shape_selection);
    }
    @Override
    public boolean shouldDecorate(CalendarDay day)
    {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        view.addSpan(new RelativeSizeSpan(1.2f));
        view.addSpan(new ForegroundColorSpan(0xff000000));
    }
}
