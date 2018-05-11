package com.example.yangwensing.myapplication.main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by nameless on 2018/4/23.
 */

public class Common {

    //檢查網路
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    //常用字串
    public final static String URL = "http://10.0.2.2:8080/SchoolConnectionBook";
    public final static String URLForMingTa = "http://10.0.2.2:8080/iContact";
    public final static String PREF_FILE = "preference";
    private static final String TAG = "Common";

    //Toast功能
    public  static void showToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public  static void showToast(Context context,int messageResId){
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }


}
