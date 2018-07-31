package com.example.paramveerjamhal.food4kids;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class Schedule extends AppCompatActivity{
    TextView rel_contact;
    String spinnerText;
    Spinner sItems;
    ArrayList<DateData> dates;
    MCalendarView calendarView;
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Schedule");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("packing");
        spinnerArray.add("sorting");
        spinnerArray.add("delivery");
        calendarView = ((MCalendarView) findViewById(R.id.calendar));;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);
        spinnerText=sItems.getSelectedItem().toString();
        dates = new ArrayList<>();
        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                spinnerText=sItems.getSelectedItem().toString();
                for (int i = 0; i < dates.size(); i++) {
                    calendarView.unMarkDate(dates.get(i).getYear(), dates.get(i).getMonth(), dates.get(i).getDay());//mark multiple dates with this code.
                }
                switch (spinnerText) {
                    case "packing":

                        dates.clear();
                        dates.add(new DateData(2018, 07, 04));
                        dates.add(new DateData(2018, 07, 11));
                        dates.add(new DateData(2018, 07, 18));
                        dates.add(new DateData(2018, 07, 25));
                        break;
                    case "sorting":
                        dates.clear();
                        dates.add(new DateData(2018, 07, 05));
                        dates.add(new DateData(2018, 07, 12));
                        dates.add(new DateData(2018, 07, 19));
                        dates.add(new DateData(2018, 07, 26));
                        break;
                    case "delivery":
                        dates.clear();
                        dates.add(new DateData(2018, 07, 06));
                        dates.add(new DateData(2018, 07, 13));
                        dates.add(new DateData(2018, 07, 20));
                        dates.add(new DateData(2018, 07, 27));
                        break;
                }

                for (int i = 0; i < dates.size(); i++) {
                    calendarView.markDate(dates.get(i).getYear(), dates.get(i).getMonth(), dates.get(i).getDay());//mark multiple dates with this code.
                }
                Log.w("_____________", "onCreate: "+spinnerText );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



      /*  final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        Event ev1 = new Event(Color.GREEN, 1433701251000L, "Some extra data that I want to store.");
        compactCalendarView.addEvent(ev1);*/

        /*Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        c = Calendar.getInstance();
        String dayLongName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        Toast.makeText(this,dayLongName,Toast.LENGTH_SHORT).show();*/




/*
        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        Log.w("date++++++++++++++++", "onCreate: " + c.getTime());*/



        Log.w("marked dates:-", "" + calendarView.getMarkedDates().toString());

//        Log.w("Dates of tuesday-------", "onCreate: "+nextDayOfWeek(Calendar.TUESDAY));



        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
               // Toast.makeText(Schedule.this, String.format("%d-%d", date.getMonth(), date.getDay()), Toast.LENGTH_SHORT).show();
        //  startActivity(new Intent(Schedule.this,TimeSelection.class));
            }
        });

    }

    public static Calendar nextDayOfWeek( int dow){
        Calendar date = Calendar.getInstance();
        int diff = dow - date.get(Calendar.DAY_OF_WEEK);
        if (diff <= 0) {
            diff += 7;
        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        return date;
    }


/*mDatePicker.addDecorator(new DayViewDecorator() {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            // check if weekday is sunday
            return day.getCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            // add red foreground span
            view.addSpan(new ForegroundColorSpan(
                    ContextCompat.getColor(getContext(), Color.RED)));
        }
    });*/


}
