package com.kayushi07.getablessing;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "GET_BLESSED_ALARM";
    Intent intent;
    PendingIntent pendingIntent;
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "BroadcastReceiver has received alarm intent.");
//        Intent service1 = new Intent(context, AlarmService.class);
//        context.startService(service1);
//        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent mIntent = new Intent(this, DialogActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("test", "test");
//        mIntent.putExtras(bundle);



    NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();


        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, DialogActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        nb.setContentIntent(contentIntent);



        notificationHelper.getManager().notify(1, nb.build());
        Notification notification = nb.build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;






    }


    }
