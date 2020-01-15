package com.example.coudapp.models;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.coudapp.MainActivity;


public class Throttler {
    private long lastTime;
    public Throttler()
    {
        this.lastTime = System.currentTimeMillis();
    }
    public boolean Check(Context context){
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - this.lastTime;

        int elapsedSeconds = (int)(elapsedTime / 1000);
        int maxTimeOfInactivity= AppRegistry.getInstance().getSettings().app_standby_time;
        maxTimeOfInactivity = maxTimeOfInactivity * 60; //convert minutes in seconds
        this.lastTime=currentTime;
        if(elapsedSeconds < maxTimeOfInactivity)
            return false;
        AppRegistry registry = AppRegistry.getInstance();
        Activity activity = (Activity) context;
        registry.init(activity);
        Intent activityLauncher = new Intent(context, MainActivity.class);
        context.startActivity(activityLauncher);
        return true;
    }

}
