package com.example.paramveerjamhal.food4kids.Fragments;

import android.app.Notification;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.paramveerjamhal.food4kids.Advance3DDrawer1Activity;
import com.example.paramveerjamhal.food4kids.LoginActivity;
import com.example.paramveerjamhal.food4kids.R;
import com.example.paramveerjamhal.food4kids.RegisterActivity;
import com.example.paramveerjamhal.food4kids.TokenManager;
import com.example.paramveerjamhal.food4kids.Utils;
import com.example.paramveerjamhal.food4kids.entities.AccessToken;
import com.example.paramveerjamhal.food4kids.entities.ApiError;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;
import com.github.clans.fab.FloatingActionMenu;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class CreateEventFragment extends Fragment {

    private static final String TAG = "CreateEventFragment";
    String time;
    @BindView(R.id.datePicker)
    RelativeLayout datepicker;
    @BindView(R.id.til_event_name)
     public TextInputLayout event_title;
     @BindView(R.id.til_event_desc)
     TextInputLayout event_desc;
     @BindView(R.id.til_event_address)
     TextInputLayout event_address;
     @BindView(R.id.til_event_postal)
     TextInputLayout event_postal;
     @BindView(R.id.input_date)
     TextView event_date;
     @BindView(R.id.til_event_org)
     TextInputLayout event_organizer;
     @BindView(R.id.til_volNo)
     TextInputLayout event_noOfVol;
     @BindView(R.id.btn_create)
     Button event_create;
     @BindView(R.id.til_startTime)
     RelativeLayout startTime;
     @BindView(R.id.til_endTime)
     RelativeLayout endTime;
     @BindView(R.id.et_startTime)
     TextView et_startTime;
     @BindView(R.id.et_endTime)
     TextView et_endTime;

     @BindView(R.id.spinner_eventType)
     Spinner event_spinner;
     @BindView(R.id.action_menu)
     FloatingActionMenu menu;
     @BindView(R.id.spinner_eventTask)
     Spinner task_spinner;
     String spinnerText,spinnerTask;
     int eventType=0;
     TokenManager tokenManager;
     ApiService service;
     Call<AccessToken> call;
     AwesomeValidation validator;
     ProgressDialog m_Dialog;
     String e_startTime,e_endTime;
     Date inputDate,inputDate1;

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create Event");
        menu.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_event, container, false);
        ButterKnife.bind(this, rootView);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service= RetrofitBuilder.createServiceWithAuth(ApiService.class,tokenManager);
        validator=new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        m_Dialog = new ProgressDialog(getActivity());

        setupRules();
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("weekly");
        spinnerArray.add("special");
        List<String> spinnerTaskArray = new ArrayList<String>();
        spinnerTaskArray.add("Packing");
        spinnerTaskArray.add("Sorting");
        spinnerTaskArray.add("Delivery");

        //adapter of event type
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        //adapter of event task
        ArrayAdapter<String> adapterTask = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerTaskArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTask.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        event_spinner.setAdapter(adapter);
        task_spinner.setAdapter(adapterTask);
        spinnerText=event_spinner.getSelectedItem().toString();
        spinnerTask=task_spinner.getSelectedItem().toString();
        event_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerText=event_spinner.getSelectedItem().toString();
                switch (spinnerText) {
                    case "weekly":
                        eventType=0;
                        break;
                    case "special":
                        eventType=1;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        task_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerTask=task_spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }

    //date picker click
    @OnClick(R.id.input_date)
    public void showDatePickerDialog()
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    //create event button
    @OnClick(R.id.btn_create)
    public void createEvent() {

        String eventTitle = event_title.getEditText().getText().toString();
        String eventDescription = event_desc.getEditText().getText().toString();
        String eventAddress = event_address.getEditText().getText().toString();
        String postal_code = event_postal.getEditText().getText().toString();
        String event_Date = event_date.getText().toString();
        String event_Organizer = event_organizer.getEditText().getText().toString();
        int event_numberofVol= Integer.parseInt(event_noOfVol.getEditText().getText().toString());
        String event_st=et_startTime.getText().toString();
        String event_en=et_endTime.getText().toString();

        event_title.setError(null);
        event_desc.setError(null);
        event_address.setError(null);
        event_postal.setError(null);
        event_organizer.setError(null);
        event_noOfVol.setError(null);

        validator.clear();


        if (validator.validate()) {

            m_Dialog.setMessage("Please wait while uploading...");
            m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_Dialog.setCancelable(false);
            m_Dialog.show();
            int user_id = Advance3DDrawer1Activity.userId_TAG;
            call = service.events(user_id,eventType, eventTitle, eventDescription, eventAddress, postal_code, event_Date,
                                  event_Organizer,event_numberofVol,spinnerTask,event_Date,event_st,event_en);
            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.d(TAG, "+++++++++++++++=onResponse: " + response);


                    if (response.isSuccessful()) {
                        m_Dialog.dismiss();
                        Log.w(TAG, "onResponse: " + response.body());
                        // tokenManager.saveToken(response.body());
                        getActivity().startActivity(new Intent(getActivity(), Advance3DDrawer1Activity.class));

                    } else {

                        handleErrors(response.errorBody());
                        m_Dialog.dismiss();
                     //   Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() + " personalised message : not working");
                }
            });
        } else {
            m_Dialog.dismiss();
        }
    }

    //select start time button
    @OnClick(R.id.et_startTime)
    public void startTimer()
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            /*    String AM_PM ;
                if(selectedHour < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }
*/
                et_startTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                e_startTime= String.format("%02d:%02d", selectedHour, selectedMinute);

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();


}

//on click on end timer
   @OnClick(R.id.et_endTime)
    public void endTimer()
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                et_endTime.setText( String.format("%02d:%02d", selectedHour, selectedMinute));
                e_endTime=String.format("%02d:%02d", selectedHour, selectedMinute);

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }






    public void setupRules()
    {

        validator.addValidation(getActivity(),R.id.et_title, RegexTemplate.NOT_EMPTY,R.string.err_name);
        validator.addValidation(getActivity(),R.id.input_date, RegexTemplate.NOT_EMPTY,R.string.err_date);
        validator.addValidation(getActivity(),R.id.et_startTime,RegexTemplate.NOT_EMPTY,R.string.err_start_time);
        validator.addValidation(getActivity(),R.id.et_endTime,RegexTemplate.NOT_EMPTY,R.string.err_end_time);
        validator.addValidation(getActivity(),R.id.et_desc, RegexTemplate.NOT_EMPTY,R.string.err_email);
        validator.addValidation(getActivity(),R.id.et_address,RegexTemplate.NOT_EMPTY,R.string.err_passwordconfirm);
        validator.addValidation(getActivity(),R.id.et_organizer, RegexTemplate.NOT_EMPTY,R.string.err_email);
        validator.addValidation(getActivity(),R.id.et_postal, "[ABCEGHJKLMNPRSTVXY][0-9][ABCEGHJKLMNPRSTVWXYZ] ?[0-9][ABCEGHJKLMNPRSTVWXYZ][0-9]",R.string.err_postalcode);
        validator.addValidation(getActivity(),R.id.et_volNo, RegexTemplate.NOT_EMPTY,R.string.err_noOfVolunteer);

    }

    private void handleErrors(ResponseBody response)
                {
        ApiError apiError= Utils.convertErrors(response);
        for(Map.Entry<String,List<String>> error:apiError.getErrors().entrySet()) {
            if (error.getKey().equals("eventTitle")) {
            event_title.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("eventDescription")) {

                event_desc.setError(error.getValue().get(0));
            }
            if(error.getKey().equals("eventAddress")){
                event_address.setError(error.getValue().get(0));
            }
            if(error.getKey().equals("postal_code")){
                event_postal.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("event_Date")) {
                event_date.setError(error.getValue().get(0));
            }
            if(error.getKey().equals("event_Organizer")){
                event_organizer.setError(error.getValue().get(0));
            }
            if(error.getKey().equals("noOfVol")){
                event_noOfVol.setError(error.getValue().get(0));
            }


        }
    }



}
