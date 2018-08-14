package com.example.yangwensing.myapplication.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.homework.StudentHomeworkFragment;
import com.example.yangwensing.myapplication.login.LoginFragment;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.JsonObject;

/**
 * Created by nameless on 2018/4/26.
 */

public class ClassCreate extends Fragment{
    private final static String TAG = "MainFragment";
    private EditText etClass;
    private Button btSure;
    private MyTask myTask;
    String user ="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classcreate, container, false);
        getActivity().setTitle("建立班級");

        etClass = view.findViewById(R.id.etClass);
        setHasOptionsMenu(true); //這樣onCreateOptionsMenu()才有效、才能加optionsMenu進activity的options

        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        user = preferences.getString("account","0");

        btSure = view.findViewById(R.id.btSure);
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                String ClassName = etClass.getText().toString();
                if (ClassName.trim().isEmpty()) {
                    etClass.setError("InValid ClassName");
                    isValid = false;
                }
                if (isValid)
                    if (Common.networkConnected(getActivity())) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "insertClass");
                        jsonObject.addProperty("ClassName", ClassName);
                        jsonObject.addProperty("ClassTeacher",user);


                        try {
                            myTask = new MyTask(Common.URLForHen + "/LoginHelp", jsonObject.toString());
                            int count = Integer.valueOf(myTask.execute().get());
                            if (count == 0) {
                                Toast.makeText(getActivity(), "Add failed!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Establish Succeeded", Toast.LENGTH_SHORT).show();
                                Fragment fragment = new ClassManager();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, fragment)
                                        .addToBackStack(null).commit();
                            }

                        } catch (Exception e) {
                            Log.e(TAG, "error massage" + e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "connection to network failed",
                                Toast.LENGTH_SHORT).show();
                    }
            }


        });






//        getFragmentManager().popBackStack("cm",0);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.options_menu_classmanager, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(false);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cmcreate:
                Fragment classCreate = new ClassCreate();

                Bundle bundle = new Bundle();
                bundle.putString("name", user);
                classCreate.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.content, classCreate);
                fragmentTransaction.commit();

                break;
            case R.id.cmjoin:
                Fragment classJoin = new ClassJoin();

                Bundle bundlejo = new Bundle();
                bundlejo.putString("name", user);
                classJoin.setArguments(bundlejo);

                FragmentManager fragmentManagerjo = getFragmentManager();
                FragmentTransaction fragmentTransactionjo = fragmentManagerjo.beginTransaction();

//                fragmentTransactionjo.addToBackStack(null);

                fragmentTransactionjo.replace(R.id.content, classJoin);
                fragmentTransactionjo.commit();

                break;
            case R.id.cmdelete:
                Fragment classdelete = new ClassDelete();

                Bundle bundlede = new Bundle();
                bundlede.putString("name", user);
                classdelete.setArguments(bundlede);

                FragmentManager fragmentManagerde = getFragmentManager();
                FragmentTransaction fragmentTransactionde = fragmentManagerde.beginTransaction();

//                fragmentTransactionde.addToBackStack(null);

                fragmentTransactionde.replace(R.id.content, classdelete);
                fragmentTransactionde.commit();

                break;
//            case R.id.cmlogout:
//                //重置偏好設定檔儲存的登入設定
//                SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
//                preferences.edit()
//                        .putInt("studentId", 0)
//                        .putInt("teacherId", 0)
//                        .putInt("subjectId", 0)
//                        .putInt("classId", 0)
//                        .putString("className", "")
//                        .apply();
//
//                //清除所有backStack
//                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//                //回到登入頁面
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new LoginFragment()).commit();
//
//                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
