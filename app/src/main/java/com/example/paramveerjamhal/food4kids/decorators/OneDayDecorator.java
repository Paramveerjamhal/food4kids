package com.example.paramveerjamhal.food4kids.decorators;

import android.graphics.Typeface;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * Decorate a day by making the text big and bold
 */
public class OneDayDecorator implements DayViewDecorator {

    private CalendarDay date;
    private HashSet<CalendarDay> dates;
    public OneDayDecorator() {
      //  date = CalendarDay.today();
    }

    public OneDayDecorator(Collection<CalendarDay> dates){
        this.dates = new HashSet<>(dates);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
    }

    /**
     * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
     */
    public void setDate(Date date) {
        this.date = CalendarDay.from(date.getTime());
    }
}
