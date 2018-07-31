package com.example.paramveerjamhal.food4kids;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.paramveerjamhal.food4kids.entities.AccessToken;
import com.example.paramveerjamhal.food4kids.entities.ApiError;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;
import com.facebook.login.Login;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.annotation.RegEx;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @BindView(R.id.til_name)
    TextInputLayout tilName;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.til_password_confirm)
     TextInputLayout tilPasswordConfirm;
    @BindView(R.id.til_address)
    TextInputLayout tilAddress;
    @BindView(R.id.til_city)
    TextInputLayout tilCity;
    @BindView(R.id.til_postal_code)
    TextInputLayout til_postal_code;
    @BindView(R.id.til_mobile)
    TextInputLayout tilMobile;


    ApiService service;
    Call<AccessToken> call;
    AwesomeValidation validator;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        service = RetrofitBuilder.createService(ApiService.class);
        validator=new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        tokenManager=TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
        setupRules();

        if(tokenManager.getToken().getAccessToken()!=null){
            startActivity(new Intent(RegisterActivity.this,Advance3DDrawer1Activity.class));
            finish();
        }
    }

    @OnClick(R.id.btn_register)
    void register(){

        String name = tilName.getEditText().getText().toString();
        String email = tilEmail.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();
        String password_confirmation=tilPasswordConfirm.getEditText().getText().toString();
        String address=tilAddress.getEditText().getText().toString();
        String city=tilCity.getEditText().getText().toString();
        String postal_code=til_postal_code.getEditText().getText().toString();
        String mobile=tilMobile.getEditText().getText().toString();
        int userType=0;

        tilName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilPasswordConfirm.setError(null);
        tilAddress.setError(null);
        tilCity.setError(null);
        til_postal_code.setError(null);
        tilMobile.setError(null);

        validator.clear();

        if(validator.validate()) {
            call = service.register(name, email, password,password_confirmation,address,city,postal_code,mobile,userType);
            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.d(TAG, "+++++++++++++++=onResponse: " + response);

                    if (response.isSuccessful()) {
                        Log.w(TAG, "onResponse: " + response.body());
                        tokenManager.saveToken(response.body());
                        startActivity(new Intent(RegisterActivity.this,Advance3DDrawer1Activity.class));
                        finish();

                    } else {

                        handleErrors(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() + " personalised message : not working");
                }
            });
        }
        }

    @OnClick(R.id.go_to_login)
    void goToLogin()
    {
        startActivity(new Intent(this,LoginActivity.class));
    }


        private void handleErrors(ResponseBody response)
        {
            ApiError apiError=Utils.convertErrors(response);
            for(Map.Entry<String,List<String>> error:apiError.getErrors().entrySet()) {
                if (error.getKey().equals("name")) {
                    tilName.setError(error.getValue().get(0));
                }
                if (error.getKey().equals("email")) {

                    tilEmail.setError(error.getValue().get(0));
                }
                if(error.getKey().equals("password")){
                    tilPassword.setError(error.getValue().get(0));
                }
                if(error.getKey().equals("password_confirmation")){
                    tilPasswordConfirm.setError(error.getValue().get(0));
                }
                if(error.getKey().equals("address")){
                    tilAddress.setError(error.getValue().get(0));
                }
                if(error.getKey().equals("city")){
                    tilCity.setError(error.getValue().get(0));
                }
                if(error.getKey().equals("postal_code")){
                    til_postal_code.setError(error.getValue().get(0));
                }
                if(error.getKey().equals("mobile")){
                    tilMobile.setError(error.getValue().get(0));
                }
            }
        }

        public void setupRules()
        {

            validator.addValidation(this,R.id.til_name, RegexTemplate.NOT_EMPTY,R.string.err_name);
            validator.addValidation(this,R.id.til_email, Patterns.EMAIL_ADDRESS,R.string.err_email);
            validator.addValidation(this,R.id.til_password,"[a-zA-Z0-9]{6,}",R.string.err_password);
            validator.addValidation(this,R.id.til_password_confirm,R.id.til_password_confirm,R.string.err_passwordconfirm);
            validator.addValidation(this,R.id.til_address, RegexTemplate.NOT_EMPTY,R.string.err_address);
            validator.addValidation(this,R.id.til_city, RegexTemplate.NOT_EMPTY,R.string.err_city);
            validator.addValidation(this,R.id.til_postal_code,"[ABCEGHJKLMNPRSTVXY][0-9][ABCEGHJKLMNPRSTVWXYZ] ?[0-9][ABCEGHJKLMNPRSTVWXYZ][0-9]",R.string.err_postalcode);
            validator.addValidation(this,R.id.til_mobile, RegexTemplate.TELEPHONE,R.string.err_mobile);
        }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null) {
            call.cancel();
            call = null;
        }
    }
}
