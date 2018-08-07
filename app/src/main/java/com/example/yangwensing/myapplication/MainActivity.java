package com.example.yangwensing.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yangwensing.myapplication.chat.MotherList;
import com.example.yangwensing.myapplication.charts.BackUpStudentExamFragment;
import com.example.yangwensing.myapplication.exam.StudentExam;
import com.example.yangwensing.myapplication.exam.StudentExamQuery;
import com.example.yangwensing.myapplication.homework.StudentHomeworkFragment;
import com.example.yangwensing.myapplication.info.StudentInfoFragment;
import com.example.yangwensing.myapplication.login.LoginFragment;
import com.example.yangwensing.myapplication.main.BottomNavigationViewHelper;
import com.example.yangwensing.myapplication.main.Common;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bnForStudent;
//    private TabLayout tlForTeacherHomeworkCheck;
    public static int alarmType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BottomNavigation 學生用
        bnForStudent = findViewById(R.id.bnForStudent);
        BottomNavigationViewHelper.removeShiftMode(bnForStudent);
        bnForStudent.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListenerForStudent);


        Fragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, loginFragment);
        fragmentTransaction.commit();

//        //tabLayout給老師作業勾選
//        tlForTeacherHomeworkCheck = findViewById(R.id.tlForTeacherHomework);
//        tlForTeacherHomeworkCheck.addTab(tlForTeacherHomeworkCheck.newTab().setText(R.string.text_tabHomeworkContent));
//        tlForTeacherHomeworkCheck.addTab(tlForTeacherHomeworkCheck.newTab().setText(R.string.text_tabHomeworkCheck));


    }

    //BottomNavigation 學生用 功能設定
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListenerForStudent
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null; //要寫出null，否則會有not initialized 警告

            switch (item.getItemId()) {

                case R.id.navigation_homework:
                    selectedFragment = new StudentHomeworkFragment();
                    break;
                case R.id.navigation_exam:
                    selectedFragment = new StudentExam();
//                    selectedFragment = new ExamFragment();
                    break;
                case R.id.navigation_contact:
                    selectedFragment = new MotherList();
                    break;
                case R.id.navigation_info:
                    selectedFragment = new StudentInfoFragment();
                    break;

            }


            getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).commit();
            return true;
        }
    };

    //右上角選項
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_options, menu);



        return true;
    }

    //右上角選項
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_logOut:
                alarmType = 1;

                new AlertDialogFragment().show(getSupportFragmentManager(), "exit"); //呼叫警示視窗fragment


                break;
            case R.id.menu_settings:
                Toast.makeText(getBaseContext(), "Enter settingsView", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item); //是否要改回傳true?
    }

    //跳出App前，先跳警示訊息
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (keyCode == KeyEvent.KEYCODE_BACK && count == 0) {
            new AlertDialogFragment().show(getSupportFragmentManager(), "exit"); //呼叫警示視窗fragment
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class AlertDialogFragment
            extends DialogFragment implements DialogInterface.OnClickListener {


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            if (alarmType == 1) {
                return new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.text_logOut)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage(R.string.msg_wantToLogOut)
                        .setPositiveButton(R.string.text_logOut, this)
                        .setNegativeButton(R.string.text_No, this)
                        .create();

            }
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.text_exit)
                    .setIcon(R.drawable.ic_alert)
                    .setMessage(R.string.msg_wantToExit)
                    .setPositiveButton(R.string.text_exit, this)
                    .setNegativeButton(R.string.text_No, this)
                    .create();

        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (alarmType == 1) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //重置偏好設定檔儲存的登入設定
                        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
                        preferences.edit()
                                .putInt("studentId", 0)
                                .putInt("teacherId", 0)
                                .putInt("subjectId", 0)
                                .putInt("classId", 0)
                                .putString("className", "")
                                .apply();

                        //清除所有backStack(但會呼叫
//                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


                        //回到登入頁面
//                        getFragmentManager().beginTransaction().replace(R.id.content, new LoginFragment()).commit();

                        //回到登入頁面用開啟新activity的方式，就不需叫清理backStack了
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        // set the new task and clear flags
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);


                        alarmType = 0;
                        break;
                    default:
                        alarmType = 0;
                        dialog.cancel();
                        break;
                }

            } else {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        getActivity().finish();
                        break;
                    default:
                        dialog.cancel();
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        //結束程式時
        //重置偏好設定檔儲存的登入設定
        SharedPreferences preferences = getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        preferences.edit()
                .putInt("studentId", 0)
                .putInt("teacherId", 0)
                .putInt("subjectId", 0)
                .putInt("classId", 0)
                .putString("className", "")
                .apply();
        super.onDestroy();
    }
}
