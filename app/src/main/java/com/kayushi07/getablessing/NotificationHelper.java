package com.kayushi07.getablessing;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName,
                NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public NotificationCompat.Builder getChannelNotification() {


        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        int count = prefs.getInt("blessing_count", 0);

        String todays_blessings = BlessedData.getData()[count];

        String userName = prefs.getString("user_name", null);

//        String txt = "Good Morning, " + userName + " " + todays_blessings;
//        System.out.println(txt);
        SharedPreferences.Editor editor = prefs
                .edit();

        editor.putString("blessing_today",
                todays_blessings);

        if(count <= 198) {


            count= count+1;
            editor.putInt("blessing_count",
                    count);
        }
        else
        {

            editor.putInt("blessing_count",
                    0);
        }

        editor.commit();

        Resources res = this.getResources();






        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.dp)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.icon))
                .setTicker(res.getString(R.string.notification_title))
                .setAutoCancel(true)
                .setContentTitle("Blessings " + userName + ",")
                .setContentText(todays_blessings);
    }
}