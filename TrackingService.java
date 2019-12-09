package com.example.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TrackingService extends Service {

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static MediaPlayer mediaPlayer;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    public TrackingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("forground", "on start");
        mediaPlayer = MediaPlayer.create(this, R.raw.car_alarm);
        mediaPlayer.setLooping(true);

        if (intent != null) {
            String action = intent.getAction();
if (action !=null) {
    switch (action) {
        case ACTION_START_FOREGROUND_SERVICE:
            startForegroundService();

            Log.d("forground", "Foreground service is started.");
            break;
    }
}
        }
        return super.onStartCommand(intent, flags, startId);

    }


    public static NotificationCompat.Builder getNotificationBuilder(Context context, int channelId, int importance) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prepareChannel(context, channelId + "", importance);
            builder = new NotificationCompat.Builder(context, channelId + "");
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        return builder;
    }


    private static void prepareChannel(Context context, String id, int importance) {
        final String appName = context.getString(R.string.app);
        String description = "online";
        final NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);

        if (nm != null) {
            NotificationChannel nChannel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                nChannel = nm.getNotificationChannel(id);


                if (nChannel == null) {
                    nChannel = new NotificationChannel(id, appName, importance);
                    nChannel.setDescription(description);

                    nm.createNotificationChannel(nChannel);

                }
            }
        }
    }

    /* Used to build and start foreground service. */
    private void startForegroundService() {
        Log.d("forground", "Start foreground service.");

        // Create notification default intent.
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create notification builder.
        NotificationCompat.Builder builder = getNotificationBuilder(this, 5, // Channel id
                NotificationManagerCompat.IMPORTANCE_MAX);

        builder.setContentTitle(this.getString(R.string.app));
        builder.setContentText("content");

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        builder.setLargeIcon(largeIconBitmap);
        builder.setContentIntent(pendingIntent);
        // Make the notification max priority.
        builder.setPriority(Notification.PRIORITY_MAX);
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true);
        // Build the notification.
        Notification notification = builder.build();

        // Start foreground service.
        startForeground(1, notification);

        PusherOptions options = new PusherOptions();

        options.setCluster("ap2");
        Pusher pusher = new Pusher("9b63fbaafcfe2d99d3c4", options);
        final Channel channel = pusher.subscribe("my-channel");

        channel.bind("my-event", new SubscriptionEventListener() {
//            @RequiresApi(api = 26)
            @Override
            public void onEvent(String channelName, String eventName, String data) {
                String temp = data.replace("\\", "");
                //  temp=temp.substring(1,temp.length()-1);

                SharedPreferences preferences;
                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                int PREFERENCES_PASSENGER_ID = preferences.getInt(Config.PREFERENCES_PASSENGER_ID, 0);
                int PREFERENCES_PASSENGER_GROUPNUmBER = preferences.getInt(Config.PREFERENCES_PASSENGER_GROUPNUmBER, 0);

//                    if (Integer.parseInt(data) == PREFERENCES_PASSENGER_ID) {
//
//                    }else {
                try {
                    //      JSONObject responseJSonObj = new JSONObject( URLDecoder.decode( temp, "UTF-8" ) );
//                    JSONParser parser_obj = new JSONParser();
//                    JSONArray array_obj = (JSONArray) parser_obj.parse(data);
// in your case it will be "result"

                    JSONObject jsonObject = new JSONObject(temp.substring(temp.indexOf("{"), temp.lastIndexOf("}") + 1));
                    JSONArray result = jsonObject.getJSONArray(Config.TAG_JASON_ARRAY);

                    mediaPlayer = MediaPlayer.create(TrackingService.this, R.raw.enzar);
                    mediaPlayer.setLooping(true);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String name = (jo.getString(Config.TAG_name));
                        int id = Integer.valueOf(jo.getString(Config.TAG_ID));
                        int groupNumber = Integer.valueOf(jo.getString(Config.TAG_GROUpNUMBER));
                        String phone = (jo.getString(Config.TAG_pHONE));

                        if (!(PREFERENCES_PASSENGER_ID == id))
                            if (PREFERENCES_PASSENGER_GROUPNUmBER == groupNumber) {

                                // mediaPlayer.setOnPreparedListener(Service.this);
                                if (!mediaPlayer.isPlaying()) {
                                    mediaPlayer.start();
                                }
                                RequestHandler rh = new RequestHandler();
                                HashMap<String,String> userdata = new HashMap<String, String>();
                                userdata.put(Config.KEY_ID,String.valueOf(id));
                                String res = rh.sendPostRequest(Config.URL_REQUEST_NAME,userdata);
                                if (res.equals(""))
                                    res = rh.sendPostRequest(Config.URL_REQUEST_NAME,userdata);
                                else if (res.equals("connection error"))
                                    res = rh.sendPostRequest(Config.URL_REQUEST_NAME,userdata);

                                name = res;
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(Config.PREFERENCES_request_PASSENGER_NAME, name);
                                editor.putString(Config.PREFERENCES_request_PASSENGER_PHONE, phone);
                                editor.putInt(Config.PREFERENCES_Request_GROUPNUmBER, groupNumber);
                                editor.apply();
                                NotificationManagerCompat nm = NotificationManagerCompat.from(getApplicationContext());
                                NotificationCompat.Builder notification = new NotificationCompat.Builder(TrackingService.this,CHANNEL_ID)
                                        .setContentTitle(name + "have trouple in his houes")
                                        .setContentText("his phone is" + phone)
                                        .setContentTitle(getResources().getString(R.string.app))
                                        .setTicker(getResources().getString(R.string.app))
                                        .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                                        .setOngoing(true)
                                        .setCategory(NotificationCompat.CATEGORY_ALARM).setSmallIcon(R.drawable.ic_directions_run_black_24dp);
                                Intent requestintent = new Intent(getBaseContext(), RequestView.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(TrackingService.this, 0, requestintent, PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_directions_run_black_24dp, "travels", pendingIntent);
                                notification.addAction(action);
                                notification.setAutoCancel(true);
                                notification.setOngoing(true);


//                                startForeground(1, notification);
                                Intent dialogIntent = new Intent(TrackingService.this, RequestView.class);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(dialogIntent);


                            }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                } catch (ParseException e) {
//                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                    }


            }
        });

//        pusher.connect();
        // connect to the Pusher API
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.e("pusher: State", " changed to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.e("pusher:problem", " connecting! msg:" + message);
            }
        }, ConnectionState.ALL);



        //stopSelf();

    }
}
