package com.example.yangwensing.myapplication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by nameless on 2018/4/23.
 */

public class Common {

    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public final static String URL = "http://10.0.2.2:8080/PleaseLogin";
    public final static String PREF_FILE = "preference";
    private static final String TAG = "Common";
}
