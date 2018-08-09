package com.example.paramveerjamhal.food4kids.firebase_services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.paramveerjamhal.food4kids.Advance3DDrawer1Activity;
import com.example.paramveerjamhal.food4kids.R;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService  extends com.google.firebase.messaging.FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("message"));
    }

    private void showNotification(String message) {
        Intent intent=new Intent(this, Advance3DDrawer1Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this)
                                                .setAutoCancel(true)
                .setContentTitle("Notification")
                .setContentText(message)
                .setSmallIcon(R.drawable.notification)
                .setContentIntent(pendingIntent);

        NotificationManager manager =(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }
}