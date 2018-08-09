package com.example.paramveerjamhal.food4kids.firebase_services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;


import com.example.paramveerjamhal.food4kids.MainActivity;
import com.example.paramveerjamhal.food4kids.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FKFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel("mychannelid","mychannelname",NotificationManager.IMPORTANCE_HIGH);

            mChannel.setDescription("my description");
            mChannel.enableLights(true);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mChannel.setShowBadge(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100,200,300,400,500,40,300,200,100});
            mNotificationManager.createNotificationChannel(mChannel);


        }
        PendingIntent pi = PendingIntent.getActivity(this,0,new Intent(this, MainActivity.class),0);
        Notification notification = new NotificationCompat.Builder(this,"my_channel_id_01")
             //   .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(remoteMessage.getNotification().getBody())
                .setWhen(System.currentTimeMillis())
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentIntent(pi)
                .setContentText(remoteMessage.getNotification().getBody())
                .setLights(Color.RED, 3000, 3000)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .build();
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(0,notification);




    }


}
