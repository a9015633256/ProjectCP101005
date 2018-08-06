package com.example.yangwensing.myapplication.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yangwensing.myapplication.classes.ClassManager;
import com.example.yangwensing.myapplication.R;
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
    private int studentId;
    private int teacherid;
    private int subjectid;
    private FloatingActionButton fabStudent;
    private FloatingActionButton fabTeacher;

    //帳號比對格式
    String accoutwho = "\\w{1,}@{1,1}\\w{1,}\\.\\w{1,}\\.{0,}\\w{1,}";

    //底部導覽列跟浮動按鈕
    private BottomNavigationView bnForStudent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

//        getActivity().setTitle(R.string.textLogin);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText etName = view.findViewById(R.id.etName);
        final EditText etPassword = view.findViewById(R.id.etPassword);
        bnForStudent = getActivity().findViewById(R.id.bnForStudent);
        Button btLogin = view.findViewById(R.id.btLogin);
        Button btRegister = view.findViewById(R.id.btRegister);

        //偷懶輸入
        setFAB(view);


        setHasOptionsMenu(true); //這樣onCreateOptionsMenu()才有效

        //回到此頁面時隱藏底部導覽列
        bnForStudent.setVisibility(View.GONE);


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

                    if (user.matches(accoutwho)) {
                        Fragment classManager = new ClassManager();

                        Bundle bundle = new Bundle();
                        bundle.putString("name", user);
                        classManager.setArguments(bundle);

                        ///
                        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
                        preferences.edit()
                                .putString("name", user)
                                .putInt("teacherId", teacherid)
                                .putInt("subjectId",subjectid)
                                .apply();

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.addToBackStack(null);

                        fragmentTransaction.replace(R.id.content, classManager);
                        fragmentTransaction.commit();
//                        bottomNavigationView.setVisibility(View.VISIBLE);

                    } else {

                        getStudentId(user, password);
                        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
                        preferences.edit()
                                .putString("studentnumber", user)
                                .apply();

                        //底部導覽列選第一項
                        bnForStudent.setSelectedItemId(R.id.navigation_homework);
                        bnForStudent.setVisibility(View.VISIBLE);

                    }


                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();

                }


            }
        });



        return view;

    }

    private void setFAB(View view) {
        final View fragmentView = view;

        fabStudent = view.findViewById(R.id.fabAutoFillStudent);
        fabTeacher = view.findViewById(R.id.fabAutoFillTeacher);

        fabStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etName = fragmentView.findViewById(R.id.etName);
                etName.setText("student1");
                EditText etPassword = fragmentView.findViewById(R.id.etPassword);
                etPassword.setText("1");



            }
        });

        fabTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etName = fragmentView.findViewById(R.id.etName);
                etName.setText("cp102@hotmail.com");
                EditText etPassword = fragmentView.findViewById(R.id.etPassword);
                etPassword.setText("22222");



            }
        });


    }

    //隱藏右上選單
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.setGroupVisible(0, false);

    }

    private boolean isUserValid(String name, String password) {
        boolean isUserValid = false;

        if (networkConnected()) {
            String url = Common.URL + "/LoginHelp";
            JsonObject jsonObject = new JsonObject();
            if (name.matches(accoutwho)) {
                jsonObject.addProperty("action", "findByName");
            } else {
                jsonObject.addProperty("action", "findbyStudent");
            }
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("password", password);
            loginTask = new MyTask(url, jsonObject.toString());

            try {
                String jsonIN = loginTask.execute().get();
                jsonObject = new Gson().fromJson(jsonIN, JsonObject.class);
                isUserValid = jsonObject.get("isUserValid").getAsBoolean();
                teacherid = jsonObject.get("id").getAsInt();
                subjectid = jsonObject.get("subject").getAsInt();

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

    //取得學生id
    private void getStudentId(String name, String password) {


        if (Common.networkConnected(getActivity())) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findStudentIdByNameAndPassword");
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("password", password);
            MyTask getHomeworkTask = new MyTask(Common.URLForMingTa + "/HomeworkServlet", jsonObject.toString());

            try {

                String jsonIn = getHomeworkTask.execute().get();

                studentId = Integer.valueOf(jsonIn);
                SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
                preferences.edit().putInt("studentId", studentId).apply();


            } catch (Exception e) {
                e.printStackTrace();
                Common.showToast(getActivity(), R.string.text_no_server);

            }


        } else {
            Common.showToast(getActivity(), R.string.text_no_network);

        }
    }


}
