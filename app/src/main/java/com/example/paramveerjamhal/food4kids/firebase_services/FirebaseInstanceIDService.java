package com.example.paramveerjamhal.food4kids.firebase_services;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.codewarriors4.tiffin.utils.DatabaseHelper;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

public class FirebaseInstanceIDService extends com.google.firebase.iid.FirebaseInstanceIdService {

    private String refreshedToken;
    DatabaseHelper mDatabaseHelper;


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("token", "Refreshed token: " + refreshedToken);
/*        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        sessionUtli.setValue("fcmtoken",refreshedToken);
        String token = sessionUtli.getValue("fcmtoken");*/
        mDatabaseHelper = new DatabaseHelper(this);
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        if(!mDatabaseHelper.checkFCMExists(token)){
            boolean insertData = mDatabaseHelper.addData(token);
        }


        //new MyAsynTask().execute("");
    }

}
