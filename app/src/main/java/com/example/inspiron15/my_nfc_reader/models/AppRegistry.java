package com.example.inspiron15.my_nfc_reader.models;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

//import com.ecash.alexpos.models.User;

public class AppRegistry {
    public static final String APP_NAME = "COUD";
    public static final String APP_VERSION = "1.0.0";
    public static final String HOST = "payema.com";
    public static String COUNTRY_CODE = "SN";
    public static String COUNTRY_ID = "1";
    private static AppRegistry instance;
    private Context appContext;
    private Throttler throttler;
    private Setting settings;

    private AppRegistry() {

    }

    public static AppRegistry getInstance() {
        if (AppRegistry.instance == null)
            AppRegistry.instance = new AppRegistry();
        return AppRegistry.instance;
    }

    public void init(Context context) {
        this.appContext = context.getApplicationContext();
        this.throttler = new Throttler();
        this.refreshSettings(context);
        // this.user =null;
        //extract settings or create default settings
        //this.settings = Setting.findById(Setting.class, AppRegistry.APP_SETTINGS_ID);
        //if(this.settings == null)
        //  this.settings = Setting.createDefaultSetting();
    }
    public void refreshSettings(Context context) {
        this.settings = Setting.refreshSettings(context);
    }

    public Setting getSettings() {
        return this.settings;
    }
    public Context getApplicationContext() {
        return this.appContext;
    }

   /* public User getCurrentUser(){
        return this.user;
    }
    public void setCurrentUser(User user){
        this.user = user;
    }
    public boolean hasRootAccess(){
        return this.user.isAdmin();
    }
    */

    public boolean CheckInactivity(Context context) {
        return this.throttler.Check(context);
    }
    //=========versionning=================
    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException ex) {}
        return 0;
    }

    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException ex) {}
        return null;
    }

}
