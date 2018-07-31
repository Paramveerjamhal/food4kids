package com.example.paramveerjamhal.food4kids;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paramveerjamhal.food4kids.entities.AccessToken;
import com.example.paramveerjamhal.food4kids.entities.ApiError;
import com.example.paramveerjamhal.food4kids.entities.User;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity {


    private static final String TAG = "UserProfile";
    @BindView(R.id.username)
    TextView name;
    @BindView(R.id.useremail)
    TextView email;

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
    //Button ids
    @BindView(R.id.btn_submit)
    AppCompatButton submit_btn;
    @BindView(R.id.btn_cancel)
    AppCompatButton camcel_btn;
    @BindView(R.id.Buttons)
    LinearLayout buttons;

    ArrayList<User> aListModel;

    TokenManager tokenManager;
    ApiService service;
    ProgressDialog m_Dialog;
    Call<AccessToken> call;

    String user_Name, user_Email, user_Mobile, user_Address, user_City, user_Postal;

    TextView rel_contact;
    Intent callIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tokenManager = TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        m_Dialog = new ProgressDialog(this);
        aListModel = (ArrayList<User>) getIntent().getSerializableExtra("user");


        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);


        this.setTitle("Profile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Advance3DDrawer1Activity.Profile = 0;
            }
        });
        setDate();
    }

    private void setDate() {
        name.setText(aListModel.get(0).getName());
        email.setText(aListModel.get(0).getEmail());
        userName.setText(aListModel.get(0).getName());
        userEmail.setText(aListModel.get(0).getEmail());
        userAddress.setText(aListModel.get(0).getAddress());
        userCity.setText(aListModel.get(0).getCity());
        userMobile.setText(aListModel.get(0).getMobile());
        PhoneNumberUtils.formatNumber(
                userMobile.getEditableText(), PhoneNumberUtils.getFormatTypeForLocale(Locale.US));
        userPostalcode.setText(aListModel.get(0).getPostal_code());

        userName.setEnabled(false);
        userAddress.setEnabled(false);
        userEmail.setEnabled(false);
        userCity.setEnabled(false);
        userMobile.setEnabled(false);
        userPostalcode.setEnabled(false);
    }

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
                 Toast.makeText(UserProfile.this, "event updated successfully", Toast.LENGTH_LONG).show();
                 m_Dialog.dismiss();
                } else {

                    if (response.code() == 422) {
                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(UserProfile.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        m_Dialog.dismiss();
                        Toast.makeText(UserProfile.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.btn_cancel)
    public void cancel()
    {
        userName.setEnabled(false);
        userAddress.setEnabled(false);
        userEmail.setEnabled(false);
        userCity.setEnabled(false);
        userMobile.setEnabled(false);
        userPostalcode.setEnabled(false);
        buttons.setVisibility(View.GONE);
    }

    @OnClick(R.id.edit_profile)
    public void edit()
    {
        buttons.setVisibility(View.VISIBLE);
        user_Name=userName.getEditableText().toString();
        user_Address=userAddress.getEditableText().toString();
        user_City=userCity.getEditableText().toString();
        user_Email=userEmail.getEditableText().toString();
        user_Mobile=userMobile.getEditableText().toString();
        user_Postal=userPostalcode.getEditableText().toString();

        userName.setEnabled(true);
        userAddress.setEnabled(true);
        userEmail.setEnabled(true);
        userCity.setEnabled(true);
        userMobile.setEnabled(true);
        userPostalcode.setEnabled(true);
    }
}

