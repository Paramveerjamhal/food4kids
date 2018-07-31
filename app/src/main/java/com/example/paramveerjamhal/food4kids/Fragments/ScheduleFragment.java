package com.example.paramveerjamhal.food4kids.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.paramveerjamhal.food4kids.LoginActivity;
import com.example.paramveerjamhal.food4kids.R;
import com.example.paramveerjamhal.food4kids.TokenManager;
import com.example.paramveerjamhal.food4kids.decorators.EventDecorator;
import com.example.paramveerjamhal.food4kids.decorators.HighlightWeekendsDecorator;
import com.example.paramveerjamhal.food4kids.decorators.OneDayDecorator;
import com.example.paramveerjamhal.food4kids.entities.WeeklyEvent;
import com.example.paramveerjamhal.food4kids.entities.Weekly_EventResponse;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ScheduleFragment extends Fragment implements OnDateSelectedListener {

    private static final String TAG ="ScheduleFragment" ;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    ApiService service;
    TokenManager tokenManager;
    Call<Weekly_EventResponse> call;

    List<CalendarDay> days=new ArrayList<>();

    String spinnerText;
    String selectedRole;
    @BindView(R.id.calendarView)
    MaterialCalendarView widget;
    @BindView(R.id.spinner)
    Spinner role_spinner;

    List<DayOfWeek> dayofweekList;
    ArrayList<CalendarDay> dates=new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Set Task");
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("pref", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        callPackingService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_schedule_layout, container, false);
        //binding the layout file
        ButterKnife.bind(this, rootView);

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
                        widget.invalidateDecorators();
                        widget.removeDecorators();
                        callPackingService();

                        selectedRole = spinnerText;
                       // widget.addDecorators(new AllDaysDisabledDecorator(), new MultipleDateDecorator(eventsBeanList), oneDayDecorator);
                        selectedRole = null;
                        break;
                    case "sorting":
                        widget.invalidateDecorators();
                        widget.removeDecorators();
                        selectedRole = spinnerText;
                        widget.addDecorators(new AllDaysDisabledDecorator(), new HighlightWeekendsDecorator(selectedRole), oneDayDecorator);
                        selectedRole = null;
                        break;
                    case "delivery":
                        widget.invalidateDecorators();
                        widget.removeDecorators();
                        selectedRole = spinnerText;
                        widget.addDecorators(new AllDaysDisabledDecorator(), new HighlightWeekendsDecorator(selectedRole), oneDayDecorator);
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

                          for(int i=0;i<response.body().getData().size();i++) {
                              try {
                                          String inputDateStr=response.body().getData().get(i).getDate();
                                          @SuppressLint("SimpleDateFormat")
                                          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                          Date date1 = format.parse(inputDateStr);
                                          System.out.println(date1);
                                          Calendar c = Calendar.getInstance();
                                          c.setTime(date1);
                                          System.out.println("month "+(c.get(Calendar.MONTH)+1) + " date "+c.get(Calendar.DATE) +
                                                  "year "+c.get(Calendar.YEAR));

                                          widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
                                          widget.setDateSelected(c, true);
                                  CalendarDay day = CalendarDay.from(c);
                                  dates.add(day); widget.addDecorators(new AllDaysDisabledDecorator(),new EnableDatesDecorator(dates),oneDayDecorator);

                                         // widget.setSelectedDate(c);

                                      } catch (ParseException e) {
                                          e.printStackTrace();
                                      }
                                  }
                              Toast.makeText(getActivity(),"", Toast.LENGTH_SHORT).show();
                      }

                     //   widget.addDecorators(new AllDaysDisabledDecorator(), new EventDecorator(Color.RED, days), oneDayDecorator);
                    } else {
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

                    Log.w(TAG, "Repsonse of schedule fragment for SORTING api ++++:api " + response.body().getData().get(0).getDate());
                    if (response.body().getData().size() != 0) {

                        Log.e(TAG, "response of packing calender "+ response.body().getData() );
                        Toast.makeText(getContext(), response.body().getData().get(0).getDate().toString(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "No data fetched.", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                    tokenManager.deleteToken();
                }
            }

            @Override
            public void onFailure(Call<Weekly_EventResponse> call, Throwable t) {}
        });
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * Simulate an API call to show how to add decorators
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
                calendar.add(Calendar.DATE, 5);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (getActivity().isFinishing()) {
                return;
            }

            //   widget.addDecorator(new EventDecorator(Color.RED, calendarDays));
        }
    }

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


    private static class EnableDatesDecorator implements DayViewDecorator {
        private HashSet<CalendarDay> dates;

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
        }
    }


}


