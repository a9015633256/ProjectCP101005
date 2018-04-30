package com.example.yangwensing.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yangwensing.myapplication.info.StudentInfoFragment;
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

    //底部導覽列跟浮動按鈕
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabShortcut;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(R.string.textLogin);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText etName = view.findViewById(R.id.etName);
        final EditText etPassword = view.findViewById(R.id.etPassword);
        bottomNavigationView = getActivity().findViewById(R.id.navigation);
        Button btLogin = view.findViewById(R.id.btLogin);
        Button btRegister = view.findViewById(R.id.btRegister);
        fabShortcut = view.findViewById(R.id.fabShortCutToStudent);

        setHasOptionsMenu(true); //這樣onCreateOptionsMenu()才有效

        //回到此頁面時隱藏底部導覽列
        bottomNavigationView.setVisibility(View.GONE);


        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragmentRegister = new RegisterFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.content, fragmentRegister);
                fragmentTransaction.commit();


            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = etName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (isUserValid(user, password)) {
                    Fragment classManager = new ClassManager();

                    Bundle bundle = new Bundle();
                    bundle.putString("name", user);
                    classManager.setArguments(bundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.replace(R.id.content, classManager);
                    fragmentTransaction.commit();
                    bottomNavigationView.setVisibility(View.VISIBLE);


                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();

                }


            }
        });

        fabShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.content, new StudentInfoFragment()).commit();
                bottomNavigationView.setVisibility(View.VISIBLE);

            }
        });


        return view;

    }

    //隱藏右上選單
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.setGroupVisible(0,false);

    }

    private boolean isUserValid(String name, String password) {
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
