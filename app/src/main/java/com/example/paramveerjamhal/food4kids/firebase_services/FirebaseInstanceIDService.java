package com.example.paramveerjamhal.food4kids.firebase_services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
       String token= FirebaseInstanceId.getInstance().getToken();
       registerToken(token);
    }

    private void registerToken(String token) {

        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("Token",token)
                .build();


        Request.Builder request=new Request.Builder()
                .url("http://192.168.0.13/food4kids/web/public/api/login");
    }
}
