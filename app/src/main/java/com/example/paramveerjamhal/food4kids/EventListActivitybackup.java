package com.example.paramveerjamhal.food4kids;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paramveerjamhal.food4kids.Fragments.DatePickerFragment;
import com.example.paramveerjamhal.food4kids.Tags.AppConstant;
import com.example.paramveerjamhal.food4kids.adapter.EventAdapter;
import com.example.paramveerjamhal.food4kids.entities.AccessToken;
import com.example.paramveerjamhal.food4kids.entities.ApiError;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventListActivitybackup extends AppCompatActivity {


    private static final String TAG = "EventListActivity";
    public static String DELETE_RESPONSE = " ";

    @BindView(R.id.fab_delete)
    FloatingActionButton fab_delete;
    @BindView(R.id.fab_edit)
    FloatingActionButton fab_edit;

    //Textinputlayout ids
    @BindView(R.id.til_event_name)
    public TextInputLayout event_title;
    @BindView(R.id.til_event_date)
    TextInputLayout event_date;
    @BindView(R.id.til_event_desc)
    TextInputLayout event_desc;
    @BindView(R.id.til_event_address)
    TextInputLayout event_address;
    @BindView(R.id.til_event_postal)
    TextInputLayout event_postal;
    @BindView(R.id.til_event_org)
    TextInputLayout event_organizer;

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
    @BindView(R.id.et_org)
    EditText etorg;



    ApiService service;
    TokenManager tokenManager;
    Call<AccessToken> call;
     ProgressDialog m_Dialog;

     @BindView(R.id.save_button)
     Button save;

    //event id
    int intent_eventId;
    String intent_event_Name, intent_event_Desc, intent_event_Date, intent_event_Org, intent_event_Address,intent_event_Postal,
    event_startTime,even_endTime,spinner_task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        //binding view
        ButterKnife.bind(this);



        tokenManager=TokenManager.getInstance(getSharedPreferences("pref",MODE_PRIVATE));
        service= RetrofitBuilder.createServiceWithAuth(ApiService.class,tokenManager);
        m_Dialog= new ProgressDialog(this);

        //getting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //getting extras from intent
        intent_eventId = getIntent().getIntExtra("event_id", -1);
        intent_event_Name = getIntent().getStringExtra("event_name");
        intent_event_Date = getIntent().getStringExtra("event_date");
        intent_event_Address = getIntent().getStringExtra("event_address");
        intent_event_Postal=getIntent().getStringExtra("event_postal");
        intent_event_Desc = getIntent().getStringExtra("event_desc");
        intent_event_Org = getIntent().getStringExtra("event_organizer");


        setData();

    if (AppConstant.user_Type.equals("1")) {
            fab_delete.setVisibility(View.VISIBLE);
            fab_edit.setVisibility(View.VISIBLE);
        } else {
            fab_delete.setVisibility(View.GONE);
            fab_edit.setVisibility(View.GONE);
        }
    }

    private void setData() {

        etTitle.setText(intent_event_Name);
        etDesc.setText(intent_event_Desc);
        etAddress.setText(intent_event_Address);
        etPostal.setText(intent_event_Postal);
        etDate.setText(intent_event_Date);
        etorg.setText(intent_event_Org);

        etTitle.setFocusable(false);
        etDesc.setFocusable(false);
        etAddress.setFocusable(false);
        etPostal.setFocusable(false);
        etorg.setFocusable(false);
        etDate.setFocusable(false);


        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.fab_delete)
    public void delete_confirm() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EventListActivitybackup.this);
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

    @OnClick(R.id.fab_edit)
    public void edit_event(){
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
        etDate.setFocusableInTouchMode(true);
        etDate.setFocusable(true);

         //setting visibility of views
         save.setVisibility(View.VISIBLE);
         fab_edit.setVisibility(View.GONE);
         fab_delete.setVisibility(View.GONE);
             }

             @OnClick(R.id.save_button)
             public void save_event()
             {
               String name=etTitle.getEditableText().toString();
               String date=etDate.getEditableText().toString();
               String desc=etDesc.getEditableText().toString();
               String org=etorg.getEditableText().toString();
               String postal=etPostal.getEditableText().toString();
               String add=etAddress.getEditableText().toString();
              /* call = service.update_event(intent_eventId,Advance3DDrawer1Activity.userId_TAG,
                       name,
                       desc,
                       add,
                       postal,date,org);*/
                 Log.e("service data +++", "save_event+++++++= "+intent_event_Name );
           call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                   // Intent intent = new Intent(EventListActivity.this, Advance3DDrawer1Activity.class);
                    fab_delete.setVisibility(View.VISIBLE);
                    fab_edit.setVisibility(View.VISIBLE);
                    save.setVisibility(View.GONE);
                    etTitle.setFocusable(false);
                    etDesc.setFocusable(false);
                    etAddress.setFocusable(false);
                    etPostal.setFocusable(false);
                    etorg.setFocusable(false);
                    etDate.setFocusable(false);
                    Toast.makeText(EventListActivitybackup.this,"event updated successfully",Toast.LENGTH_LONG).show();
                   // startActivity(intent);
                    m_Dialog.dismiss();
                  //  finish();
                } else {

                    if (response.code() == 422) {}
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(EventListActivitybackup.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(EventListActivitybackup.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });


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
                    DELETE_RESPONSE=response.message();
                    Intent intent = new Intent(EventListActivitybackup.this, Advance3DDrawer1Activity.class);
                    //   intent.putExtra("userType",userType.toString());
                    startActivity(intent);

                    m_Dialog.dismiss();
                    finish();
                } else {

                    if (response.code() == 422) {}
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(EventListActivitybackup.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(EventListActivitybackup.this, apiError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.input_date)
    public void showDatePickerDialog()
    {
      //  etDate.setText("");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
