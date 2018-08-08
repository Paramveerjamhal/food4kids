package com.example.paramveerjamhal.food4kids;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paramveerjamhal.food4kids.adapter.EventAdapter;
import com.example.paramveerjamhal.food4kids.adapter.UserAdapter;
import com.example.paramveerjamhal.food4kids.entities.AccessToken;
import com.example.paramveerjamhal.food4kids.entities.ApiError;
import com.example.paramveerjamhal.food4kids.entities.Participation_Response;
import com.example.paramveerjamhal.food4kids.entities.User;
import com.example.paramveerjamhal.food4kids.entities.UserWithEventTAsk;
import com.example.paramveerjamhal.food4kids.entities.UserWithEventTaskResponse;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.paramveerjamhal.food4kids.TimeViewActivity.showMessageDialog;

public class UserParticipationInformation extends AppCompatActivity {


    private static final String TAG = "UserProfile";
    private static boolean HIDE = false;

    @BindView(R.id.userName)
    EditText userName;
    @BindView(R.id.userEmail)
    EditText userEmail;
    @BindView(R.id.userMobile)
    EditText userMobile;
    @BindView(R.id.userAddress)
    EditText userAddress;
    @BindView(R.id.userCity)
    EditText userCity;
    @BindView(R.id.userPostalcode)
    EditText userPostalcode;
    @BindView(R.id.et_userStartTime)
    EditText userStartTime;
    @BindView(R.id.et_userEndTime)
    EditText userEndTime;
    //Button ids
    @BindView(R.id.action_menu)
    FloatingActionMenu menu;
    @BindView(R.id.fab_edit)
    FloatingActionButton block_user;
    @BindView(R.id.fab_delete)
    FloatingActionButton remove_user;


    TokenManager tokenManager;
    ApiService service;
    ProgressDialog m_Dialog;
    Call<AccessToken> call;
    Call<Participation_Response> callPart;

    ArrayList<UserWithEventTAsk> userWithEventTAsks;

    String user_Name, user_Email, user_Mobile, user_Address, user_City, user_Postal,user_StartTime,user_EndTime,date;
    int user_id,admin_Approval,part_id,event_id;

    TextView rel_contact;
    Intent callIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userparticipationinfo);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        this.setTitle("Volunteer Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Advance3DDrawer1Activity.Profile = 0;
            }
        });
        UserParticipationInformation.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tokenManager = TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        m_Dialog = new ProgressDialog(this);
        userWithEventTAsks= (ArrayList<UserWithEventTAsk>) this.getIntent().getExtras().getSerializable("user_list");
        user_id=this.getIntent().getIntExtra("user_id",-1);


        for (int i=0;i<userWithEventTAsks.size();i++) {

                user_Name=userWithEventTAsks.get(i).getName();
                user_Email=userWithEventTAsks.get(i).getEmail();
                user_Address=userWithEventTAsks.get(i).getAddress();
                user_City=userWithEventTAsks.get(i).getCity();
                user_Mobile=userWithEventTAsks.get(i).getMobile();
                user_Postal=userWithEventTAsks.get(i).getPostal_code();
                user_StartTime=userWithEventTAsks.get(i).getUser_startTime();
                user_EndTime=userWithEventTAsks.get(i).getUser_endTime();
                part_id=userWithEventTAsks.get(i).getParticipate_id();
                event_id=userWithEventTAsks.get(i).getEvent_id();
                date=userWithEventTAsks.get(i).getDate();


        }
        setDate();


    }


    private void setDate() {
        userName.setText(user_Name);
        userEmail.setText(user_Email);
        userAddress.setText(user_Address);
        userCity.setText(user_City);
        userMobile.setText(user_Mobile);
        userStartTime.setText(user_StartTime);
        userEndTime.setText(user_EndTime);
        PhoneNumberUtils.formatNumber(
                userMobile.getEditableText(), PhoneNumberUtils.getFormatTypeForLocale(Locale.US));
        userPostalcode.setText(user_Postal);

        userName.setEnabled(false);
        userAddress.setEnabled(false);
        userEmail.setEnabled(false);
        userCity.setEnabled(false);
        userMobile.setEnabled(false);
        userPostalcode.setEnabled(false);
        userStartTime.setEnabled(false);
        userEndTime.setEnabled(false);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(admin_Approval==0) {
            getMenuInflater().inflate(R.menu.menu_approve, menu);
        }


        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.approve_menu:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserParticipationInformation.this);
                // set title
                alertDialogBuilder.setTitle("Are you sure you want to approve this Volunteer to attend the Event?");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Click yes to Approve!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                callApproveService(1);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alertDialogBuilder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callApproveService(int approve) {
        final ProgressDialog m_Dialog = new ProgressDialog(this);
        m_Dialog.setMessage("Please wait while updating the data...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();

        call = service.update_Participate(part_id,user_id,event_id,user_StartTime,user_EndTime,approve);

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    m_Dialog.dismiss();
                    if (response.body() != null) {
                        System.out.println("response is not null");
                        showMessageDialogUser(UserParticipationInformation.this,getString(R.string.participation_approved),"update");

                        m_Dialog.dismiss();

                    }
                    else {
                        //  PopUp=2;
                        m_Dialog.dismiss();
                        menu.setVisibility(View.VISIBLE);
                        Toast.makeText(UserParticipationInformation.this, "No data fetched.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {

                Toast.makeText(UserParticipationInformation.this, t.toString(), Toast.LENGTH_SHORT).show();
                m_Dialog.dismiss();
            }


        });

    }

    public boolean onPrepareOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(HIDE)
        {
            invalidateOptionsMenu();
        }

        return true;
    }

    public void showMessageDialogUser(final Activity activity, final String response, final String callingPlace) {
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
                CallUserInfoService(date);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void CallUserInfoService(final String date) {

        {
            callPart = service.showparticipation();
            callPart.enqueue(new Callback<Participation_Response>() {
                @Override
                public void onResponse(Call<Participation_Response> call, Response<Participation_Response> response) {
                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        Log.w(TAG, "Repsonse of userParticipation Information class api ++++:api " + response.body().getData());
                        if (response.body().getData().size() != 0) {
                            for(int i=0;i<response.body().getData().size();i++) {
                                if (response.body().getData().get(i).getAdmin_approveStatus()==1) {

                                    HIDE = true;
                                }
                            }

                        } else {
                            Toast.makeText(UserParticipationInformation.this, "No data fetched.", Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        finish();
                        tokenManager.deleteToken();

                    }

                }

                @Override
                public void onFailure(Call<Participation_Response> call, Throwable t) {

                }

            });


        }
    }


    @OnClick(R.id.fab_delete)
    public void removeUser()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserParticipationInformation.this);
        // set title
        alertDialogBuilder.setTitle("Are you sure you want to  remove this Volunteer from event?");
        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to Remove!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        calldelete_Volunteer_Service();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.show();
    }


    private void calldelete_Volunteer_Service() {

        m_Dialog.setMessage("Please wait...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();

        call = service.delete_participation(part_id);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {

                    showMessageDialog(UserParticipationInformation.this,getString(R.string.participation_volunteerremove),"delete");

                    m_Dialog.dismiss();
                     finish();
                } else {

                    if (response.code() == 422) {}
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(UserParticipationInformation.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(UserParticipationInformation.this, apiError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


      /*
        aListModel = (ArrayList<User>) getIntent().getSerializableExtra("user");





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem action_right_drawer = menu.findItem(R.id.action_right_drawer);
        if (Advance3DDrawer1Activity.Profile == 1) {
            action_right_drawer.setVisible(false);
        }
        return true;
    }

    @OnClick(R.id.btn_submit)
    public void submit() {
        m_Dialog.setMessage("Please wait...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        call = service.update_user(user_Name,user_Email,user_Address,user_City,user_Postal,user_Mobile);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    buttons.setVisibility(View.GONE);
                    userName.setEnabled(false);
                    userAddress.setEnabled(false);
                    userEmail.setEnabled(false);
                    userCity.setEnabled(false);
                    userMobile.setEnabled(false);
                    userPostalcode.setEnabled(false);
                 Toast.makeText(UserParticipationInformation.this, "User information updated successfully", Toast.LENGTH_LONG).show();
                 m_Dialog.dismiss();
                } else {

                    if (response.code() == 422) {
                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(UserParticipationInformation.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(UserParticipationInformation.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
*/

}
