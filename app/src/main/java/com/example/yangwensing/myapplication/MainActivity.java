package com.example.yangwensing.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yangwensing.myapplication.contact.StudentContactFragment;
import com.example.yangwensing.myapplication.exam.StudentExamFragment;
import com.example.yangwensing.myapplication.homework.StudentHomeworkFragment;
import com.example.yangwensing.myapplication.info.StudentInfoEditFragment;
import com.example.yangwensing.myapplication.info.StudentInfoFragment;
import com.example.yangwensing.myapplication.main.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //底部Navigation
        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Fragment loginFragment = new LoginFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, loginFragment );
        fragmentTransaction.commit();

    }

    //底部Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null; //要寫出null，否則會有not initialized 警告

            switch (item.getItemId()) {
                case R.id.navigation_info:
                    selectedFragment = new StudentInfoFragment();
                    break;
                case R.id.navigation_homework:
                    selectedFragment = new StudentHomeworkFragment();
                    break;
                case R.id.navigation_exam:
                    selectedFragment = new StudentExamFragment();
                    break;
                case R.id.navigation_contact:
                    selectedFragment = new StudentContactFragment();
                    break;

            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, selectedFragment);
            fragmentTransaction.commit();
            return true;
        }
    };

    //    右上角選項
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment selectedFragment = null; //要寫出null，否則會有not initialized 警告

        switch (item.getItemId()) {
            case R.id.menu_logOut:
                Toast.makeText(getBaseContext(), "Log Out!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_settings:
                Toast.makeText(getBaseContext(), "Enter settingsView", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_editInfo:
                selectedFragment = new StudentInfoEditFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, selectedFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            default:
                break;
        }



        return super.onOptionsItemSelected(item); //是否要改回傳true?
    }

}
