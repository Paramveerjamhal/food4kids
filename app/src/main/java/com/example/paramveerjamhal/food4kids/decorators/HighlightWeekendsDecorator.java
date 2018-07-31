package com.example.paramveerjamhal.food4kids.decorators;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;
import java.util.Locale;

/**
 * Highlight Saturdays and Sundays with a background
 */
public class HighlightWeekendsDecorator implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance(Locale.getDefault());
    private Drawable highlightDrawable;
    private static int color = Color.parseColor("#009369");
    private String userRole;

    public HighlightWeekendsDecorator(String userRole) {
        highlightDrawable = new ColorDrawable(color);
        this.userRole=userRole;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        if(userRole.equals("packing")){
          return  weekDay == Calendar.TUESDAY;
        }
         if(userRole.equals("sorting")){
           return weekDay == Calendar.WEDNESDAY;
        }
        if(userRole.equals("delivery")){
            return weekDay == Calendar.THURSDAY;
        }
        return weekDay == Calendar.TUESDAY /*|| weekDay == Calendar.SUNDAY*/;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(highlightDrawable);
         }



}
