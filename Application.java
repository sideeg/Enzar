package com.sideeg.renza;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class  Application extends android.app.Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static void startTrackingService(){

        if (getAppContext() == null){

        }
        if(!Utility.isMyServiceRunning(TrackingService.class,getAppContext())){
                Log.d("ours", "service called");
                Intent intent = new Intent(getAppContext(), TrackingService.class);
                intent.setAction(TrackingService.ACTION_START_FOREGROUND_SERVICE);
                getAppContext().startService(intent);
            }

    }

    public static Context getAppContext() {
        return Application.context;
    }
}
