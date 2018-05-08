package com.kayushi07.getablessing;
 
 
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.HashMap;
import java.util.List;


public class AlarmService extends IntentService 
{
      
   private static final int NOTIFICATION_ID = 1;
   private static final String TAG = "GET_BLESSED_ALARM";
   private NotificationManager notificationManager;
   private PendingIntent pendingIntent;
   int count;

    public AlarmService() {
	      super("AlarmService");
	  }
   
   
   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
       return super.onStartCommand(intent,flags,startId);
   }
   
   @Override
   protected void onHandleIntent(Intent intent) {
           // don't notify if they've played in last 24 hr
	   Log.i(TAG,"Alarm Service has started.");
       Context context = this.getApplicationContext();
       notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, DialogActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("test", "test");
//        mIntent.putExtras(bundle);


       final SharedPreferences prefs = PreferenceManager
               .getDefaultSharedPreferences(this);
       count = prefs.getInt("blessing_count", 0);

       String todays_blessings = BlessedData.getData()[count];

       mIntent.putExtra("blessing" , todays_blessings);
       mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);



		Resources res = this.getResources();
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//       final SharedPreferences prefs = PreferenceManager
//               .getDefaultSharedPreferences(this);
//       count = prefs.getInt("blessing_count", 0);
//
//       String todays_blessings = BlessedData.getData()[count];


       String userName = prefs.getString("user_name", null);

       String txt = "Good Morning, " + userName + " " + todays_blessings;
       System.out.println(txt);


       builder.setContentIntent(pendingIntent)
		            .setSmallIcon(R.drawable.dp)
		            .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.icon))
		            .setTicker(res.getString(R.string.notification_title))
		            .setAutoCancel(true)
		            .setContentTitle("Good Morning, " + userName)
		            .setContentText(todays_blessings);

		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID, builder.build());

		if(count<=198) {

            SharedPreferences.Editor editor = prefs
                    .edit();
            editor.putInt("blessing_count",
                    count++);
            editor.commit();
        }
        else
        {
            SharedPreferences.Editor editor = prefs
                    .edit();
            editor.putInt("blessing_count",
                    0);
            editor.commit();
        }

		Log.i(TAG,"Notifications sent.");

    }
 
}