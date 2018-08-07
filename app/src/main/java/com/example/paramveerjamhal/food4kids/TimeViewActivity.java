package com.example.paramveerjamhal.food4kids;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.paramveerjamhal.food4kids.adapter.EventAdapter;
import com.example.paramveerjamhal.food4kids.entities.AccessToken;
import com.example.paramveerjamhal.food4kids.entities.ApiError;
import com.example.paramveerjamhal.food4kids.entities.Participation_Response;
import com.example.paramveerjamhal.food4kids.entities.WeeklyEvent;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;
import com.example.paramveerjamhal.food4kids.views.RangeTimePickerDialog;
import com.framgia.library.calendardayview.CalendarDayView;
import com.framgia.library.calendardayview.EventView;
import com.framgia.library.calendardayview.PopupView;
import com.framgia.library.calendardayview.data.IEvent;
import com.framgia.library.calendardayview.data.IPopup;
import com.framgia.library.calendardayview.decoration.CdvDecorationDefault;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TimeViewActivity";
    private static int PopUp=0;
    ProgressDialog m_Dialog;

    private final static int DIALOG_FROM_TIME_PICKER = 0;
    private final static int DIALOG_TO_TIME_PICKER = 1;
    CalendarDayView dayView;
    ArrayList<IEvent> events;
    ArrayList<WeeklyEvent> aListModel=new ArrayList<WeeklyEvent>();
    int starthour, endhour;
    ApiService service;
    TokenManager tokenManager;
    Call<AccessToken> call;
    Call<Participation_Response> callP;
    String Selected_StartDate,Selected_EndDate;
    int participate_id;
    RangeTimePickerDialog time;
    private int position;
    AwesomeValidation validator;
    ArrayList<IPopup> popups;
    int event_id;
    int admin_approval=0;
    Popup popup = new Popup();
    @BindView(R.id.action_menu)
    FloatingActionMenu menu;
    @BindView(R.id.fab1)
    FloatingActionButton fab1;
    @BindView(R.id.fab2)
    FloatingActionButton fab2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeview);
        ButterKnife.bind(this);
        menu.setVisibility(View.VISIBLE);

        tokenManager = TokenManager.getInstance(this.getSharedPreferences("pref", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        validator=new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        m_Dialog= new ProgressDialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Set Time");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        aListModel = (ArrayList<WeeklyEvent>) getIntent().getExtras().getSerializable("weeklyevent");
        position=getIntent().getIntExtra("position",-1);
        if(aListModel.size()!=0) {
            event_id = aListModel.get(position).getEvent_id();
        }
        dayView = (CalendarDayView) findViewById(R.id.calendar);
        dayView.setLimitTime(8, 20);
        callParticipateApi(aListModel);
        ((CdvDecorationDefault) (dayView.getDecoration())).setOnEventClickListener(
                new EventView.OnEventClickListener() {
                    @Override
                    public void onEventClick(EventView view, IEvent data) {
                        Log.e("TAG", "onEventClick:" + data.getName());
                    }

                    @Override
                    public void onEventViewClick(View view, EventView eventView, IEvent data) {
                        Log.e("TAG", "onEventViewClick:" + data.getName());

                        if (data instanceof Event) {
                            // change event (ex: set event color)
                            dayView.setEvents(events);
                            //  showDialog();
                            showAskSessionAlertDialog(TimeViewActivity.this,"dayview");
                        }
                    }
                });


        ((CdvDecorationDefault) (dayView.getDecoration())).setOnPopupClickListener(
                new PopupView.OnEventPopupClickListener() {
                    @Override
                    public void onPopupClick(PopupView view, IPopup data) {
                        Log.e("TAG", "onPopupClick:" + data.getTitle());
                    }

                    @Override
                    public void onQuoteClick(PopupView view, IPopup data) {
                        Log.e("TAG", "onQuoteClick:" + data.getTitle());
                        Toast.makeText(TimeViewActivity.this, "pop up clicked", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoadData(PopupView view, ImageView start, ImageView end,
                                           IPopup data) {
                        start.setImageResource(R.drawable.avatar);
                    }
                });

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        events = new ArrayList<>();

        {
                String currentString = aListModel.get(position).getStart_time();
                String endTime = aListModel.get(position).getEnd_time();
                String[] separated = currentString.split(":");
                String[] separated1 = endTime.split(":");
                starthour = Integer.parseInt(separated[0]);
                endhour = Integer.parseInt(separated1[0]);
                int eventColor = ContextCompat.getColor(this, R.color.eventColor);
                Calendar timeStart = Calendar.getInstance();
                timeStart.set(Calendar.HOUR_OF_DAY, starthour);
                timeStart.set(Calendar.MINUTE, 0);
                Calendar timeEnd = (Calendar) timeStart.clone();
                timeEnd.set(Calendar.HOUR_OF_DAY, endhour);
                timeEnd.set(Calendar.MINUTE, 0);
                Event event = new Event(1, timeStart, timeEnd, aListModel.get(position).getWeekly_eventTask(), "Hockaido", eventColor);
                events.add(event);
        }
        popups = new ArrayList<>();
        dayView.setEvents(events);

//////////////////////////////////oncreate finish///////////////////////////////////////
    }

    private void callParticipateApi(final ArrayList<WeeklyEvent> list) {


        m_Dialog.setMessage("Please wait while fetching data...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        callP = service.showparticipation();
        callP.enqueue(new Callback<Participation_Response>() {
            @Override
            public void onResponse(Call<Participation_Response> call, Response<Participation_Response> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    if (response.body().getData().size() != 0) {
                        for(int i=0;i<response.body().getData().size();i++) {
                            if(Advance3DDrawer1Activity.userId_TAG==response.body().getData().get(i).getUser_id()) {
                                if (event_id == (response.body().getData().get(i).getEvent_id())) {

                                    System.out.println("response id "
                                            + response.body().getData().get(i).getEvent_id());
                                    participate_id = response.body().getData().get(i).getParticipate_id();
                                    Selected_StartDate = response.body().getData().get(i).getUser_startTime();
                                    Selected_EndDate = response.body().getData().get(i).getUser_endTime();
                                    admin_approval = response.body().getData().get(i).getAdmin_approveStatus();
                                    addPopup();
                                    System.out.println("Selected " + Selected_StartDate + " " + Selected_EndDate);
                                    menu.setVisibility(View.VISIBLE);
                                    m_Dialog.dismiss();
                                }
                            }else {
                                m_Dialog.dismiss();
                              //  menu.setVisibility(View.GONE);
                            }
                        }
                    }
                    else {
                        m_Dialog.dismiss();
                       menu.setVisibility(View.GONE);
                        Toast.makeText(TimeViewActivity.this, "No participation.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Participation_Response> call, Throwable t) {

                Toast.makeText(TimeViewActivity.this, "Request can not be processed", Toast.LENGTH_SHORT).show();
                m_Dialog.dismiss();
            }

        });
    }


    @SuppressLint("SetTextI18n")
    public void showAskSessionAlertDialog(final Activity activity, final String message) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_session);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (activity.getResources().getDisplayMetrics().widthPixels - activity.getResources().getDimension(R.dimen.d_dialog_padding));
        dialog.getWindow().setAttributes(lp);
        final TextInputLayout startTime = (TextInputLayout) dialog.findViewById(R.id.til_start_time);
        final EditText edit_starTime=(EditText)dialog.findViewById(R.id.et_startTime);
        edit_starTime.setShowSoftInputOnFocus(false);
        edit_starTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime.setError(null);
                   time= new RangeTimePickerDialog(TimeViewActivity.this,new RangeTimePickerDialog.OnTimeSetListener()
                    {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            edit_starTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                            Selected_StartDate=String.format("%02d:%02d", hourOfDay, minute);
                        }
                    },starthour,0,true);
                time.setMin(starthour,0);
                time.setMax(endhour,0);
                time.show();
                }


        });
        final TextInputLayout endTime = (TextInputLayout) dialog.findViewById(R.id.til_end_time);
        final EditText edit_endTime=(EditText)dialog.findViewById(R.id.et_endtime);
        edit_endTime.setShowSoftInputOnFocus(false);
        edit_endTime .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTime.setError(null);
                time= new RangeTimePickerDialog(TimeViewActivity.this,new RangeTimePickerDialog.OnTimeSetListener()
                {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edit_endTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                        Selected_EndDate=String.format("%02d:%02d", hourOfDay, minute);
                    }
                },starthour,0,true);
                time.setMin(starthour,0);
                time.setMax(endhour,0);
                time.show();
            }
        });

        dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    endTime.setError(null);
                    startTime.setError(null);
                    validator.clear();
                    String start = edit_starTime.getEditableText().toString();
                    String end = edit_endTime.getEditableText().toString();
                    if ((!start.equals("")) && (!end.equals(""))) {
                        if(message.equals("dayview")) {
                            callParticipationService(start, end);
                        }
                        else if(message.equals("update"))
                        {
                            callUpdateService(start,end);
                        }
                            dialog.dismiss();
                    }
                   else{
                        if (android.text.TextUtils.isEmpty(edit_starTime.getText())) {
                            startTime.setErrorEnabled(true);
                            startTime.setError("required start date");
                        }
                        if (android.text.TextUtils.isEmpty(edit_endTime.getText())) {
                            endTime.setErrorEnabled(true);
                            endTime.setError("required end date");
                        }
                }
            }
        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void showMessageDialog(final Activity activity, final String response, final String callingPlace) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (activity.getResources().getDisplayMetrics().widthPixels - activity.getResources().getDimension(R.dimen.d_dialog_padding));
        dialog.getWindow().setAttributes(lp);
        TextView heading = (TextView) dialog.findViewById(R.id.tv_heading);
        final TextView Message=(TextView)dialog.findViewById(R.id.msg);
        Message.setText(response);
        TextView btnOk=(TextView)dialog.findViewById(R.id.tv_ok);
        TextView btnCancel=(TextView)dialog.findViewById(R.id.tv_cancel);

       /* if(callingPlace.equals("insert")) {
            heading.setText("Information");
        }*/
       // else if(callingPlace.equals("update"))

        dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialog.dismiss();
                          }
        });
        dialog.show();
   }

    private void callUpdateService(String startDate,String endDate) {
        final ProgressDialog m_Dialog = new ProgressDialog(this);
        m_Dialog.setMessage("Please wait while updating the data...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        call = service.update_Participate(participate_id,Advance3DDrawer1Activity.userId_TAG,event_id,startDate,endDate,0);

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    m_Dialog.dismiss();
                    if (response.body() != null) {
                        System.out.println("response is not null");
                        showMessageDialog(TimeViewActivity.this,getString(R.string.participation_update),"update");
                        m_Dialog.dismiss();
                        addPopup();
                        menu.setVisibility(View.VISIBLE);
                        dayView.refresh();
                    }
                    else {
                        //  PopUp=2;
                        m_Dialog.dismiss();
                        menu.setVisibility(View.VISIBLE);
                        Toast.makeText(TimeViewActivity.this, "No data fetched.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {

                Toast.makeText(TimeViewActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                m_Dialog.dismiss();
            }


        });

    }




    private void callParticipationService(String startDate,String endDate) {
        final ProgressDialog m_Dialog = new ProgressDialog(this);
        m_Dialog.setMessage("Please wait while Processing...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        call = service.vol_Participate(Advance3DDrawer1Activity.userId_TAG,event_id,startDate,endDate,0);

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    m_Dialog.dismiss();
                    if (response.body() != null) {
                        System.out.println("response is not null");

                        showMessageDialog(TimeViewActivity.this,getString(R.string.participation_success),"insert");
                        m_Dialog.dismiss();
                        addPopup();
                        callParticipateApi(aListModel);
                        menu.setVisibility(View.VISIBLE);
                        dayView.refresh();
                        }
                    else {
                      //  PopUp=2;
                        m_Dialog.dismiss();
                        Toast.makeText(TimeViewActivity.this, "No data fetched.", Toast.LENGTH_SHORT).show();
                        menu.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {


                Toast.makeText(TimeViewActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                m_Dialog.dismiss();
            }


        });

    }

    public void addPopup()
    {
           Calendar timeStart = Calendar.getInstance();
            String[] separated = Selected_StartDate.split(":");
            String[] separated1 = Selected_StartDate.split(":");
            timeStart.set(Calendar.HOUR_OF_DAY, Integer.parseInt(separated[0]));
            timeStart.set(Calendar.MINUTE, Integer.parseInt(separated1[1]));
            String[] end = Selected_EndDate.split(":");
            String[] end1 = Selected_EndDate.split(":");
            Calendar timeEnd = (Calendar) timeStart.clone();
            timeEnd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(end[0]));
            timeEnd.set(Calendar.MINUTE, Integer.parseInt(end[1]));
            popup.setStartTime(timeStart);
            popup.setEndTime(timeEnd);
            if(admin_approval==1) {
             popup.setMsg("Request Approved"); }
            else
            {
                popup.setMsg("Waiting for Admin Approval");
            }
            popups.add(popup);
            dayView.setPopups(popups);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.fab1:
            showAskSessionAlertDialog(TimeViewActivity.this,"update");
                break;
            case R.id.fab2:
              //  showMessageDialog(TimeViewActivity.this,"delete","delete");
                delete_confirm();
                break;
        }
    }

    private void delete_confirm() {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeViewActivity.this);
            // set title
            alertDialogBuilder.setTitle("Are you sure you want to delete this Event?");
            // set dialog message
            alertDialogBuilder
                    .setMessage("Click yes to delete!")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            calldelete_EventService();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            alertDialogBuilder.show();

        }

    private void calldelete_EventService() {

        m_Dialog.setMessage("Please wait...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();

        call = service.delete_participation(participate_id);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    callParticipateApi(aListModel);
                    showMessageDialog(TimeViewActivity.this,getString(R.string.participation_delete),"delete");
                 /*   Intent intent = new Intent(TimeViewActivity.this, Advance3DDrawer1Activity.class);
                    //   intent.putExtra("userType",userType.toString());
                    startActivity(intent);*/
                    // finish();
                    popups.clear();
                    dayView.refresh();
                    m_Dialog.dismiss();
                   // finish();
                } else {

                    if (response.code() == 422) {}
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(TimeViewActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(TimeViewActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}

   /* @Override
    public void onBackPressed() {
        callP.cancel();
        m_Dialog.dismiss();

    }*/




