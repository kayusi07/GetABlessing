package com.kayushi07.getablessing;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

/**
 * Created by Ayushi on 12-04-2018.
 */

public class MainActivity extends AppCompatActivity {

    public AlarmManager alarmManager;
    Intent alarmIntent;
    PendingIntent pendingIntent;
    private static final String TAG = "GET_BLESSED_ALARM";
    int mHour;
    int mMinute;
    TextView timeTxt;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private Toolbar toolbar;
    String userName;
    SharedPreferences prefs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Button getBlessing = (Button) findViewById(R.id.b_play);
        navHeader = navigationView.getHeaderView(0);
        final TextView userNameTxt = (TextView) navHeader.findViewById(R.id.userNameNav);
        timeTxt = (TextView) findViewById(R.id.time);
        prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        AdView mAdView = (AdView) findViewById(R.id.adView);

        try{
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView.loadAd(adRequest);
        }
        catch (Exception e){
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_changeName:
                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
                        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
                        alertDialogBuilderUserInput.setView(mView);

                        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                        alertDialogBuilderUserInput
                                .setCancelable(false)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {


                                        String userNameInput = userInputDialogEditText.getText().toString();
                                        int lenOfName = userNameInput.length();
                                        if (lenOfName <= 2){
                                            Toast.makeText(MainActivity.this, "Invalid Name!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            SharedPreferences.Editor editor = prefs
                                                    .edit();
                                            editor.putString("user_name",
                                                    userNameInput);
                                            editor.commit();
                                            userNameTxt.setText("" + userNameInput);
                                        }

                                    }
                                })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogBox, int id) {
                                                dialogBox.cancel();
                                            }
                                        });

                        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                        alertDialogAndroid.show();
                        break;
                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this, AboutUs.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_rate:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.kayushi07.getablessing")));
                        break;
                    case R.id.nav_feedback:
                        Intent localIntent = new Intent(Intent.ACTION_SEND);
                        localIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kayushi07@gmail.com"});
                        localIntent.putExtra(Intent.EXTRA_CC, "");
                        String str = null;
                        try {
                            str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                            localIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Your Android App");
                            localIntent.putExtra(Intent.EXTRA_TEXT, "\n\n----------------------------------\n Device OS: Android \n Device OS version: " +
                                    Build.VERSION.RELEASE + "\n App Version: " + str + "\n Device Brand: " + Build.BRAND +
                                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER);
                            localIntent.setType("message/rfc822");
                            startActivity(Intent.createChooser(localIntent, "Choose an Email client :"));
                        } catch (Exception e) {
                            Log.d("OpenFeedback", e.getMessage());
                        }

                        break;
                }
                return true;
            }
        });

         /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        userName = prefs.getString("user_name", null);
        if (userName == null) {
            Intent i = new Intent(MainActivity.this, GetUserName.class);
            startActivity(i);
            finish();
        }
        userNameTxt.setText("" + userName);

        mHour = prefs.getInt("mmHour", 8);
        mMinute = prefs.getInt("mmMinute", 0);

        timeTxt.setText("Your Blessing Notification will display at " + mHour + ":" + mMinute + " daily.");

        getBlessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                final Calendar c = Calendar.getInstance();
//                mHour = c.get(Calendar.HOUR_OF_DAY);
//                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                timeTxt.setText("Your Blessing Notification will display at " + hourOfDay + ":" + minute + " daily.");
                                setAlarm(hourOfDay, minute);

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();




            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        super.onBackPressed();
    }


    public void setAlarm(int selHour, int selMinute) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        Calendar alarmStartTime = Calendar.getInstance();

        alarmStartTime.set(Calendar.HOUR_OF_DAY, selHour);
        alarmStartTime.set(Calendar.MINUTE, selMinute);

        alarmManager.setInexactRepeating(AlarmManager.RTC, alarmStartTime.getTimeInMillis(), getInterval(), pendingIntent);

        SharedPreferences.Editor editor = prefs
                .edit();
        editor.putInt("mmHour",
                selHour);

        editor.putInt("mmMinute",
                selMinute);

        if(selHour >= 0 && selHour < 12){
            editor.putString("greetTitle", "Good Morning " + userName + "!");
        }else if(selHour >= 12 && selHour < 16){
            editor.putString("greetTitle", "Good Afternoon " + userName + "!");
        }else if(selHour >= 16 && selHour < 21){
            editor.putString("greetTitle", "Good Evening " + userName + "!");
        }else if(selHour >= 21 && selHour < 24){
            editor.putString("greetTitle", "Good Night " + userName + "!");
        }
        editor.commit();

    }

    private int getInterval() {
        int seconds = 60;
        int milliseconds = 1000;
        int repeatMS = 24 * seconds * 60 * milliseconds;
        return repeatMS;
    }

}