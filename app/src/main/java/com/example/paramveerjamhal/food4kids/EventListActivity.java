package com.example.paramveerjamhal.food4kids;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.paramveerjamhal.food4kids.adapter.EventAdapter;
import com.example.paramveerjamhal.food4kids.entities.AccessToken;
import com.example.paramveerjamhal.food4kids.entities.ApiError;
import com.example.paramveerjamhal.food4kids.entities.Weekly_EventResponse;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventListActivity extends AppCompatActivity {

    private static final String TAG = "CreateEventFragment";
    String time;
    @BindView(R.id.datePicker)
    RelativeLayout datepicker;
    @BindView(R.id.til_event_name)
    TextInputLayout event_title;
    @BindView(R.id.til_event_desc)
    TextInputLayout event_desc;
    @BindView(R.id.til_event_address)
    TextInputLayout event_address;
    @BindView(R.id.til_event_postal)
    TextInputLayout event_postal;

    @BindView(R.id.til_event_org)
    TextInputLayout event_organizer;
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
    @BindView(R.id.action_menu)
    FloatingActionMenu menu;
    @BindView(R.id.fab_edit)
    FloatingActionButton fab_edit;
    @BindView(R.id.fab_delete)
    FloatingActionButton fab_delete;
    //EditText ids
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.input_date)
    EditText etDate;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.et_postal)
    EditText etPostal;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_organizer)
    EditText etorg;
    @BindView(R.id.et_volNo)
    EditText etnoOfVol;
    @BindView(R.id.til_volNo)
    TextInputLayout event_noOfVol;

    @BindView(R.id.save_button)
    Button save;


    //event id
    int intent_eventId,intent_event_Type,intent_weekly_id;
    String intent_event_Name, intent_event_Desc, intent_event_Date, intent_event_Org,
           intent_noOfVol,intent_event_Address,intent_event_Postal,intent_event_startTime,
            intent_event_endTime,intent_event_task;

    ArrayAdapter<String> adapter,adapterTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        ButterKnife.bind(this);
        //getting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.green_theme));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));
        service= RetrofitBuilder.createServiceWithAuth(ApiService.class,tokenManager);
        validator=new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        m_Dialog = new ProgressDialog(this);
        setupRules();
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("weekly");
        spinnerArray.add("special");
        List<String> spinnerTaskArray = new ArrayList<String>();
        spinnerTaskArray.add("Packing");
        spinnerTaskArray.add("Sorting");
        spinnerTaskArray.add("Delivery");

        //getting extras from intent
        intent_eventId = getIntent().getIntExtra("event_id", -1);
        intent_weekly_id=getIntent().getIntExtra("weekly_id",-1);
        intent_event_Name = getIntent().getStringExtra("event_name");
        intent_event_Type=getIntent().getIntExtra("event_Type",0);
        intent_event_Date = getIntent().getStringExtra("event_date");
        intent_event_Address = getIntent().getStringExtra("event_address");
        intent_event_Postal=getIntent().getStringExtra("event_postal");
        intent_event_Desc = getIntent().getStringExtra("event_desc");
        intent_event_Org = getIntent().getStringExtra("event_organizer");
        intent_noOfVol=getIntent().getStringExtra("event_noOfVol");
        intent_event_task=getIntent().getStringExtra("event_task");
        intent_event_startTime=getIntent().getStringExtra("event_task");
        intent_event_startTime=getIntent().getStringExtra("start_time");
        intent_event_endTime=getIntent().getStringExtra("end_time");
        menu.setVisibility(View.VISIBLE);
        event_create.setVisibility(View.GONE);

        //adapter of event type
         adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        //adapter of event task
         adapterTask = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerTaskArray);

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
        setData();
    }

    private void setData() {
        etTitle.setText(intent_event_Name);
        event_spinner.setSelection(intent_event_Type);
        etDesc.setText(intent_event_Desc);
        etAddress.setText(intent_event_Address);
        etPostal.setText(intent_event_Postal);
        etDate.setText(intent_event_Date);
        etorg.setText(intent_event_Org);
        etnoOfVol.setText(intent_noOfVol);
        int i = adapterTask.getPosition(intent_event_task);
        task_spinner.setSelection(i);
        et_startTime.setText(intent_event_startTime);
        et_endTime.setText(intent_event_endTime);

        etTitle.setFocusable(false);
        etDesc.setFocusable(false);
        etAddress.setFocusable(false);
        etPostal.setFocusable(false);
        etorg.setFocusable(false);
        etnoOfVol.setFocusable(false);
        etDate.setFocusable(false);
        event_spinner.setEnabled(false);
        task_spinner.setEnabled(false);
        et_startTime.setEnabled(false);
        et_endTime.setEnabled(false);
    }

    @OnClick(R.id.fab_edit)
    public void editEvent() {

        etTitle.setFocusableInTouchMode(true);
        etTitle.setFocusable(true);
        etDesc.setFocusableInTouchMode(true);
        etDesc.setFocusable(true);
        etAddress.setFocusableInTouchMode(true);
        etAddress.setFocusable(true);
        etPostal.setFocusableInTouchMode(true);
        etPostal.setFocusable(true);
        etorg.setFocusableInTouchMode(true);
        etorg.setFocusable(true);
        etnoOfVol.setFocusableInTouchMode(true);
        etnoOfVol.setFocusable(true);
        etDate.setFocusableInTouchMode(true);
        etDate.setFocusable(true);
        event_spinner.setEnabled(true);
        task_spinner.setEnabled(true);
        et_startTime.setEnabled(true);
        et_endTime.setEnabled(true);

        save.setVisibility(View.VISIBLE);
       menu.setVisibility(View.GONE);

    }

    @OnClick(R.id.fab_delete)
    public void delete_confirm(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EventListActivity.this);
        // set title
        alertDialogBuilder.setTitle("Are you sure you want to delete this Event?");
        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to delete!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete_Event();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.show();

    }



    public void delete_Event() {


        m_Dialog.setMessage("Please wait...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();

        call = service.delete_event(intent_eventId);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    EventAdapter.DELETE_TAG="deleted";
                    Intent intent = new Intent(EventListActivity.this, Advance3DDrawer1Activity.class);
                    //   intent.putExtra("userType",userType.toString());
                    startActivity(intent);

                    m_Dialog.dismiss();
                    finish();
                } else {

                    if (response.code() == 422) {}
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(EventListActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(EventListActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    //method to update and save the event
    @OnClick(R.id.save_button)
    public void save_event() {
        String eventTitle = event_title.getEditText().getText().toString();
        int eventType = event_spinner.getSelectedItemPosition();
        String eventDescription = event_desc.getEditText().getText().toString();
        String eventAddress = event_address.getEditText().getText().toString();
        String postal_code = event_postal.getEditText().getText().toString();
        String event_Date = etDate.getText().toString();
        String event_Organizer = event_organizer.getEditText().getText().toString();
        int event_Volunteerno= Integer.parseInt(event_noOfVol.getEditText().getText().toString());
        String event_st = et_startTime.getText().toString();
        String event_en = et_endTime.getText().toString();


        m_Dialog.setMessage("Please wait while uploading...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        int user_id = Advance3DDrawer1Activity.userId_TAG;
        call = service.update_event(intent_eventId, user_id, eventType, eventTitle, eventDescription, eventAddress,
                postal_code, event_Date, event_Organizer,event_Volunteerno,intent_weekly_id,spinnerTask, event_Date,
                event_st, event_en);

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    menu.setVisibility(View.VISIBLE);
                    save.setVisibility(View.GONE);

                    etTitle.setFocusable(false);
                    etDesc.setFocusable(false);
                    etAddress.setFocusable(false);
                    etPostal.setFocusable(false);
                    etorg.setFocusable(false);
                    etnoOfVol.setFocusable(false);
                    etDate.setFocusable(false);
                    event_spinner.setEnabled(false);
                    task_spinner.setEnabled(false);
                    et_startTime.setEnabled(false);
                    et_endTime.setEnabled(false);

                    Toast.makeText(EventListActivity.this, "event updated successfully", Toast.LENGTH_LONG).show();
                    // startActivity(intent);
                    m_Dialog.dismiss();
                    //  finish();
                } else {

                    if (response.code() == 422) {
                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(EventListActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(EventListActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {

                Toast.makeText(EventListActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                m_Dialog.dismiss();
            }
        });
    }
    //date picker click
    @OnClick(R.id.input_date)
    public void showDatePickerDialog()
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }



    //select start time button
    @OnClick(R.id.et_startTime)
    public void startTimer()
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(EventListActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                et_startTime.setText( String.format("%02d:%02d", selectedHour, selectedMinute));
                e_startTime=String.format("%02d:%02d", selectedHour, selectedMinute);

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
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                et_endTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                e_endTime=String.format("%02d:%02d", selectedHour, selectedMinute);

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public void setupRules()
    {

        validator.addValidation(this,R.id.et_title, RegexTemplate.NOT_EMPTY,R.string.err_name);
        validator.addValidation(this,R.id.input_date, RegexTemplate.NOT_EMPTY,R.string.err_date);
        validator.addValidation(this,R.id.et_startTime,RegexTemplate.NOT_EMPTY,R.string.err_start_time);
        validator.addValidation(this,R.id.et_endTime,RegexTemplate.NOT_EMPTY,R.string.err_end_time);
        validator.addValidation(this,R.id.et_desc, RegexTemplate.NOT_EMPTY,R.string.err_email);
        validator.addValidation(this,R.id.et_address,RegexTemplate.NOT_EMPTY,R.string.err_passwordconfirm);
        validator.addValidation(this,R.id.et_organizer, RegexTemplate.NOT_EMPTY,R.string.err_email);
        validator.addValidation(this,R.id.et_postal, "[ABCEGHJKLMNPRSTVXY][0-9][ABCEGHJKLMNPRSTVWXYZ] ?[0-9][ABCEGHJKLMNPRSTVWXYZ][0-9]",R.string.err_postalcode);
        validator.addValidation(this,R.id.et_volNo,RegexTemplate.NOT_EMPTY,R.string.err_noOfVolunteer);

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
                etDate.setError(error.getValue().get(0));
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
