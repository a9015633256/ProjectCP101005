package com.example.yangwensing.myapplication.main;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by nameless on 2018/4/23.
 */

public class MyTask extends AsyncTask<String, Integer, String> {
    private final static String TAG = "MyTask";
    private String url, outStr;

    public MyTask(String url, String outStr) {
        this.url = url;
        this.outStr = outStr;
    }

    @Override
    protected String doInBackground(String... strings) {
        String instr = getRemoteData(url, outStr);

        return instr;
    }

    private String getRemoteData(String url, String outstr) {
        HttpURLConnection connection = null;
        StringBuilder instr = new StringBuilder();

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(outstr);
            Log.d(TAG,"outStr"+outstr);
            bw.close();

            int responseCode = connection.getResponseCode();
            if (responseCode==200){


                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line=br.readLine())!= null){
                    instr.append(line);

                 }
            }else {
                Log.d(TAG,"responseCode"+responseCode);

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        Log.d(TAG, "input: " + instr);
        return instr.toString();
    }
}
