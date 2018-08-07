package com.example.paramveerjamhal.food4kids.Fragments;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paramveerjamhal.food4kids.Advance3DDrawer1Activity;
import com.example.paramveerjamhal.food4kids.R;
import com.example.paramveerjamhal.food4kids.TimeViewActivity;
import com.example.paramveerjamhal.food4kids.TokenManager;
import com.example.paramveerjamhal.food4kids.decorators.OneDayDecorator;
import com.example.paramveerjamhal.food4kids.entities.Part_WeeklyModel;
import com.example.paramveerjamhal.food4kids.entities.Part_WeeklyResponse;
import com.example.paramveerjamhal.food4kids.entities.WeeklyEvent;
import com.example.paramveerjamhal.food4kids.entities.Weekly_EventResponse;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Admin_ScheduleFragment extends Fragment implements OnDateSelectedListener {

    private static final String TAG ="ScheduleFragment" ;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    ApiService service;
    TokenManager tokenManager;
    Call<Weekly_EventResponse> call;
    Call<Part_WeeklyResponse> part_call;
    public static int eventId_TAG = 0;
    public static String event_Task=" ";
    String spinnerText;
    String selectedRole;
    @BindView(R.id.calendarView)
    MaterialCalendarView widget;
    @BindView(R.id.spinner)
    Spinner role_spinner;
    @BindView(R.id.tv_complete)
    TextView tv_event_complete;
    @BindView(R.id.tv_events)
    TextView tv_event_available;
    @BindView(R.id.tv_waiting)
    TextView tv_event_waiting;


    List<DayOfWeek> dayofweekList;


    ArrayList<CalendarDay> dates=new ArrayList<>();
    private ArrayList<WeeklyEvent> weekly_event = new ArrayList<WeeklyEvent>();
    ArrayList<String> part_eventList=new ArrayList<String>();

    String startHour,endHour;
    ProgressDialog m_Dialog;
    int participate_event_id,participate_id,admin_approval;
    String Selected_StartDate,Selected_EndDate;
    String part_dates;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Set Task");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_schedule_layout, container, false);
        //binding the layout file
        ButterKnife.bind(this, rootView);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("pref", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        m_Dialog= new ProgressDialog(getActivity());

        tv_event_available.setText("Events Available");
        tv_event_complete.setText("Seats Booked");
        tv_event_waiting.setText("User waiting Approval");

        dayofweekList = new ArrayList<>();
        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("packing");
        spinnerArray.add("sorting");
        spinnerArray.add("delivery");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role_spinner = (Spinner) rootView.findViewById(R.id.spinner);
        role_spinner.setAdapter(adapter);
        spinnerText = role_spinner.getSelectedItem().toString();
        selectedRole = spinnerText;
        int i = adapter.getPosition(spinnerText);
        role_spinner.setSelection(i);

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);
        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        widget.state().edit()
                .setMinimumDate(instance1)
                .setMaximumDate(instance2)
                .commit();


        role_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerText = role_spinner.getSelectedItem().toString();
                switch (spinnerText) {
                    case "packing":
                        weekly_event.clear();
                        dates.clear();
                        widget.invalidateDecorators();
                        widget.removeDecorators();
                        widget.clearSelection();
                        callPackingService();
                        selectedRole = spinnerText;
                        selectedRole = null;
                        break;
                    case "sorting":
                        weekly_event.clear();
                        dates.clear();
                        widget.invalidateDecorators();
                        widget.removeDecorators();
                        widget.clearSelection();
                        callSortingService();
                        selectedRole = spinnerText;
                        selectedRole = null;
                        break;
                    case "delivery":
                        weekly_event.clear();
                        dates.clear();
                        widget.invalidateDecorators();
                        widget.removeDecorators();
                        widget.clearSelection();
                        widget.setSelectedDate(null);
                        callDeliveryService();
                        selectedRole = spinnerText;
                        selectedRole = null;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });



      return rootView;
    }

    private void callPackingService() {


        call = service.packing_calender();
        call.enqueue(new Callback<Weekly_EventResponse>() {
            @Override
            public void onResponse(Call<Weekly_EventResponse> call, Response<Weekly_EventResponse> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        widget.addDecorator(new AllDaysDisabledDecorator());
                        weekly_event.clear();
                        weekly_event.addAll(response.body().getData());

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            try {
                                eventId_TAG=weekly_event.get(i).getEvent_id();
                                event_Task=weekly_event.get(i).getWeekly_eventTask();
                                String inputDateStr = response.body().getData().get(i).getDate();
                                System.out.println("Packing Dates "+inputDateStr);
                                startHour = response.body().getData().get(i).getStart_time();
                                endHour = response.body().getData().get(i).getEnd_time();
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = format.parse(inputDateStr);
                                System.out.println(date1);
                                Calendar c = Calendar.getInstance();
                                c.setTime(date1);
                                widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
                                widget.setDateSelected(c, true);
                                CalendarDay day = CalendarDay.from(c);
                                dates.add(day);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            widget.addDecorators(new AllDaysDisabledDecorator(), new EnableDatesDecorator(dates));

                            callParticipationService();
                        }

                    }
                }//   widget.addDecorators(new AllDaysDisabledDecorator(), new EventDecorator(Color.RED, days), oneDayDecorator);
                else {
                    Toast.makeText(getActivity(), "No data fetched.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Weekly_EventResponse> call, Throwable t) {

            }
            });
    }
    private void callSortingService() {
        call = service.sorting_calender();
        call.enqueue(new Callback<Weekly_EventResponse>() {
            @Override
            public void onResponse(Call<Weekly_EventResponse> call, Response<Weekly_EventResponse> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        widget.addDecorator(new AllDaysDisabledDecorator());
                        weekly_event.clear();
                        weekly_event.addAll(response.body().getData());
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            try {
                                eventId_TAG=weekly_event.get(i).getEvent_id();
                                String inputDateStr = response.body().getData().get(i).getDate();
                                System.out.println("Packing Dates "+inputDateStr);
                                startHour = response.body().getData().get(i).getStart_time();
                                endHour = response.body().getData().get(i).getEnd_time();
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = format.parse(inputDateStr);
                                System.out.println(date1);
                                Calendar c = Calendar.getInstance();
                                c.setTime(date1);
                                widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
                                widget.setDateSelected(c, true);
                                CalendarDay day = CalendarDay.from(c);
                                dates.add(day);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            widget.addDecorators(new AllDaysDisabledDecorator(), new EnableDatesDecorator(dates));

                        }
                        callParticipationService();
                    }
                }//   widget.addDecorators(new AllDaysDisabledDecorator(), new EventDecorator(Color.RED, days), oneDayDecorator);
                else {
                    Toast.makeText(getActivity(), "No data fetched.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Weekly_EventResponse> call, Throwable t) {
            }
        });
    }
    private void callDeliveryService() {
        widget.setSelected(false);
        call = service.delivery_calender();
        call.enqueue(new Callback<Weekly_EventResponse>() {
            @Override
            public void onResponse(Call<Weekly_EventResponse> call, Response<Weekly_EventResponse> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        widget.addDecorator(new AllDaysDisabledDecorator());
                        weekly_event.clear();
                        weekly_event.addAll(response.body().getData());
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            try {
                                eventId_TAG=weekly_event.get(i).getEvent_id();
                                String inputDateStr = response.body().getData().get(i).getDate();
                                System.out.println("Packing Dates "+inputDateStr);
                                startHour = response.body().getData().get(i).getStart_time();
                                endHour = response.body().getData().get(i).getEnd_time();
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = format.parse(inputDateStr);
                                System.out.println(date1);
                                Calendar c = Calendar.getInstance();
                                c.setTime(date1);
                                widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
                                widget.setDateSelected(c, true);
                                CalendarDay day = CalendarDay.from(c);
                                dates.add(day);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            widget.addDecorators(new AllDaysDisabledDecorator(), new EnableDatesDecorator(dates));

                        }
                        callParticipationService();
                    }
                }//   widget.addDecorators(new AllDaysDisabledDecorator(), new EventDecorator(Color.RED, days), oneDayDecorator);
                else {
                    Toast.makeText(getActivity(), "No data fetched.", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Weekly_EventResponse> call, Throwable t) {

            }
        });
    }

    private void callParticipationService(){
        m_Dialog.setMessage("Please wait while fetching data...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        part_call = service.partJoinWeeklyAdmin();
        part_call.enqueue(new Callback<Part_WeeklyResponse>() {
            @Override
            public void onResponse(Call<Part_WeeklyResponse> call, Response<Part_WeeklyResponse> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    if (response.body().getData().size() != 0) {
                        List<Part_WeeklyModel> participateList = response.body().getData();
                        //part_eventList.addAll(participateList);
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            System.out.println("response id "
                                    + response.body().getData().get(i).getEvent_id());
                            if ((participateList.get(i).getWeekly_eventTask().toLowerCase()).equals(spinnerText)) {
                                String date = response.body().getData().get(i).getDate();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = null;
                                try {
                                    date1 = format.parse(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(date1);
                                Calendar c = Calendar.getInstance();
                                c.setTime(date1);
                                CalendarDay day = CalendarDay.from(c);
                                widget.invalidateDecorators();
                                if (participateList.get(i).getAdmin_approveStatus() == 1) {
                                    widget.addDecorator(new ChangingBackgroundDecorator(day, getResources().getColor(R.color.green_theme)));
                                }
                                else
                                {
                                    widget.addDecorator(new ChangingBackgroundDecorator(day, getResources().getColor(R.color.waiting)));
                                }
                            }
                            System.out.println("Selected " + Selected_StartDate + " " + Selected_EndDate);

                        }

                    }
                    m_Dialog.dismiss();
                }
                else {
                    m_Dialog.dismiss();
                    //  Toast.makeText(getActivity(), "No participation.", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Part_WeeklyResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Request can not be processed", Toast.LENGTH_SHORT).show();
                m_Dialog.dismiss();
            }

        });
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
     //   widget.invalidateDecorators();
      //  widget.addDecorators(new AllDaysDisabledDecorator(),new EnableDatesDecorator(dates),oneDayDecorator);
        oneDayDecorator.setDate(date.getDate());
        System.out.println("selected from on date selected method"+selected);

     //   widget.addDecorators(new AllDaysDisabledDecorator(), new EventDecorator(Color.RED, days),new ChangingBackgroundDecorator(date), oneDayDecorator);
      //  widget.addDecorators(new ChangingBackgroundDecorator(date));


        for(int i=0;i<weekly_event.size();i++) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null;
            try {
                date1 = format.parse(weekly_event.get(i).getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(date1);
            Calendar c = Calendar.getInstance();
            c.setTime(date1);
            CalendarDay day = CalendarDay.from(c);

           /*     Intent intent = new Intent(getActivity(), TimeViewActivity.class);
                if (date.equals(day)) {
                    intent.putExtra("weeklyevent", weekly_event);
                    intent.putExtra("position", i);
                }
                startActivity(intent);*/



        }


    }

    /************************************************ ALL THE DECORATORS ******************************************/

    public class AllDaysDisabledDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true; //decorate all days in calendar
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true); //disable all days
        }
    }

    private static class EnableDatesDecorator implements DayViewDecorator {
        private HashSet<CalendarDay> dates;
        private Drawable highlightDrawable;
        private static int color = Color.parseColor("#f48fb1");

        public EnableDatesDecorator(Collection<CalendarDay> dates) {

            this.dates = new HashSet<>(dates);
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
          //  return day.getDay() <= 10;
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(false);

            highlightDrawable = new ColorDrawable(color);
            view.setBackgroundDrawable(highlightDrawable);
        }
    }

    private static class ChangingBackgroundDecorator implements DayViewDecorator {
        private CalendarDay date;
        private int color;
        private Drawable highlightDrawable;
      //  private static int color = Color.parseColor("#BDB76B");

        public ChangingBackgroundDecorator(CalendarDay date,int color) {
          this.date=date;
          this.color=color;

        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            //  return day.getDay() <= 10;
            return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {

          highlightDrawable = new ColorDrawable(color);
            view.setBackgroundDrawable(highlightDrawable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
            call = null;
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

}


