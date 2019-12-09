package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sideeg on 8/1/2018.
 */

public class RequestView extends FragmentActivity {
    SharedPreferences preferences ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_dialog2);

        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        TextView passngerName = (TextView) findViewById(R.id.nameTextView2);
        TextView passnerPhone = (TextView) findViewById(R.id.phoneTextView2);

//        int user_id = preferences.getInt(Config.PREFERENCES_PASSENGER_ID, 0);
        int groupNumber = preferences.getInt(Config.PREFERENCES_PASSENGER_GROUPNUmBER, 0);

        String name = preferences.getString(Config.PREFERENCES_request_PASSENGER_NAME, "");
        String phoneNumber = preferences.getString(Config.PREFERENCES_request_PASSENGER_PHONE, "");




        passngerName.setText(name);
        passnerPhone.setText(String.valueOf(phoneNumber));


       }


    /*

     */
    public void cancel(View view) {
        Intent intent = new Intent(RequestView.this,MainActivity.class);
        startActivity(intent);
    }




    /*


     */

    public  void denyClick(View view) {
        if (TrackingService.mediaPlayer.isPlaying()) {
            TrackingService.mediaPlayer.stop();
        }
        Intent service = new Intent(this, TrackingService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(service);
        }
        else {
            startService(service);
        }
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);


        }



}
