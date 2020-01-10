package com.example.inspiron15.my_nfc_reader.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.example.inspiron15.my_nfc_reader.R;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bass on 13/12/2018.
 */

public class Setting extends SugarRecord {
    public int  app_standby_time;
    public String api_secret;
    public String key;
    public String telephone;
    public String refresh_token;
    public String app_url;
    public String notification;
    public Setting() {

    }


    public Setting(int app_standby_time, String api_secret, String key, String telephone, String refresh_token, String app_url, String notification) {
        this.app_standby_time = app_standby_time;
        this.api_secret = api_secret;
        this.key = key;
        this.telephone = telephone;
        this.refresh_token = refresh_token;
        this.app_url = app_url;
        this.notification = notification;
    }

    public static Setting refreshSettings(Context context){
        PreferenceManager.setDefaultValues(context, R.xml.settings, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Setting setting=new Setting(
                preferences.getInt("app_standby_time", 10),
                preferences.getString("api_secret", "1111"),
                preferences.getString("key", "0000"),
                preferences.getString("telephone", "770000000"),
                preferences.getString("refresh_token", "refresh_token"),
                preferences.getString("app_url", "http://192.168.1.200/interaktive/backend/web/app_dev.php/api"),
                preferences.getString("notification", "pas operation")


        );
        return setting;
    }

    public static void updateSettings(Context context, JSONObject configs){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        try {
            editor.putString("key", (configs.get("key").toString()));
            editor.putString("account_number", (configs.get("account_number").toString()));

            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppRegistry.getInstance().refreshSettings(context);
    }

    public static void updateKey(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("key", key);
        // editor.putString("account_number", (configs.get("account_number").toString()));
        editor.commit();
        AppRegistry.getInstance().refreshSettings(context);
    }
 public static void updateToken(Context context, String telephone){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("telephone", telephone);
        // editor.putString("account_number", (configs.get("account_number").toString()));
        editor.commit();
        AppRegistry.getInstance().refreshSettings(context);
    }
 public static void updateRefreshToken(Context context, String refresh_token){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("refresh_token", refresh_token);
        // editor.putString("account_number", (configs.get("account_number").toString()));
        editor.commit();
        AppRegistry.getInstance().refreshSettings(context);
    }
 public static void updateNotification(Context context, String notification){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("notification", notification);
        // editor.putString("account_number", (configs.get("account_number").toString()));
        editor.commit();
        AppRegistry.getInstance().refreshSettings(context);
    }


}
