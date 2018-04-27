package com.example.yangwensing.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by nameless on 2018/4/16.
 */

public class LoginFragment extends Fragment {
    private final static String TAG = "LoginFragment";
    private MyTask loginTask;

    private BottomNavigationView bottomNavigationView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(R.string.textLogin);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText etName = view.findViewById(R.id.etName);
        final EditText etPassword = view.findViewById(R.id.etPassword);
        Button btlogin = view.findViewById(R.id.btLogin);
        Button btRegister = view.findViewById(R.id.btRegister);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragmentRegister = new RegisterFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.content,fragmentRegister);
                fragmentTransaction.commit();


            }
        });

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = etName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                    if (isUserValid(user,password)) {
                        Fragment classManager = new ClassManager();

                        Bundle bundle = new Bundle();
                        bundle.putString("name", user);
                        classManager.setArguments(bundle);

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.addToBackStack(null);

                        fragmentTransaction.replace(R.id.content,classManager);
                        fragmentTransaction.commit();
                        bottomNavigationView.setVisibility(View.VISIBLE);



                        Toast.makeText(getActivity(),"Success", Toast.LENGTH_SHORT).show();

                    } else{
                        Toast.makeText(getActivity(),"fail", Toast.LENGTH_SHORT).show();

                    }


            }
        });



        return view;

    }
    private boolean isUserValid(String name,String password) {
        boolean isUserValid = false;

        if (networkConnected()) {
            String url = Common.URL + "/LoginHelp";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findByName");
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("password", password);
            loginTask = new MyTask(url, jsonObject.toString());

            try {
                String jsonIN = loginTask.execute().get();
                jsonObject = new Gson().fromJson(jsonIN, JsonObject.class);
                isUserValid = jsonObject.get("isUserValid").getAsBoolean();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
            return isUserValid;
        }


    @Override
    public void onStop() {
        super.onStop();
        if (loginTask != null) {
            loginTask.cancel(true);
        }
    }
    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }
}
