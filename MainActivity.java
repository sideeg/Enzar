package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startTrackingService();
        Button button = (Button) findViewById(R.id.request);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTrackingService();

                Request insert = new Request();
                insert.execute();
            }
        });
        Button button2 = (Button) findViewById(R.id.closerequest);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TrackingService.mediaPlayer.isPlaying()) {
                    TrackingService.mediaPlayer.stop();
                }
                Intent service = new Intent(MainActivity.this, TrackingService.class);
                startService(service);


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utility.isMyServiceRunning(TrackingService.class, getApplicationContext())) {
            startTrackingService();
        }
    }

    public void startTrackingService() {


        if (!Utility.isMyServiceRunning(TrackingService.class, this)) {
            Log.d("ours", "service called");
            Intent intent = new Intent(this, TrackingService.class);
            intent.setAction(TrackingService.ACTION_START_FOREGROUND_SERVICE);
            this.startService(intent);
        }

    }

    class Request extends AsyncTask<Void, Void, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this, "loading ....", "... wait", false, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (loading != null)
                loading.dismiss();

            if (s.trim().equals("done")) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(MainActivity.this, "network error", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... v) {

            RequestHandler rh = new RequestHandler();

            LoginActivity.preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            int id = LoginActivity.preferences.getInt(Config.PREFERENCES_PASSENGER_ID, 0);
            int groupnumber = LoginActivity.preferences.getInt(Config.PREFERENCES_PASSENGER_GROUPNUmBER, 0);

            HashMap<String, String> userdata = new HashMap<String, String>();
            userdata.put(Config.KEY_ID, String.valueOf(id));
            userdata.put(Config.KEY_USER_PHONE, String.valueOf(groupnumber));
            String res = rh.sendPostRequest(Config.URL_REQUEST, userdata);
            return res;
        }


    }
}
