package com.example.inspiron15.my_nfc_reader.helper;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

/*
* Need these:
* <uses-permission android:name="android.permission.READ_PHONE_STATE" />
* <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
* <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
* <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
* */
public class HardwareHelper {
    private static final int REQUEST_READ_PHONE_PERMISSIONS = 1;

    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

    public static String getIMEI(Context context) {

        TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return phoneManager.getDeviceId();
    }

    public static String getMSISDN(Context context) {
        TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String msisdn = phoneManager.getLine1Number();
        return (msisdn != null && !msisdn.isEmpty()) ? msisdn : "0000";
    }

    public static String getDeviceInfo() {
        String info = "Manifacturer: " + Build.MANUFACTURER + "\n";
        info += "\nBrand: " + Build.BRAND + "\n";
        info += "Model: " + Build.MODEL + "\n";
        info += "Device: " + Build.DEVICE + "\n";
        info += "Version: " + Build.VERSION.CODENAME;
        return info;
    }


}
