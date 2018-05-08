package com.example.yangwensing.myapplication.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.login.LoginFragment;
import com.example.yangwensing.myapplication.main.Common;

/**
 * Created by nameless on 2018/4/26.
 */

public class ClassJoin extends Fragment {

    String user ="";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.classjoin, container, false);

        getActivity().setTitle("加入班級");

        setHasOptionsMenu(true); //這樣onCreateOptionsMenu()才有效、才能加optionsMenu進activity的options
        Bundle bundle = getArguments();
        user = bundle.getString("name");


//        getFragmentManager().popBackStack("cm",0);


        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.options_menu_classmanager, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);

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

                fragmentTransaction.replace(R.id.content,classCreate);
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

                fragmentTransactionjo.replace(R.id.content,classJoin);
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

                fragmentTransactionde.replace(R.id.content,classdelete);
                fragmentTransactionde.commit();

                break;
            case R.id.cmlogout:
                //重置偏好設定檔儲存的登入設定
                SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
                preferences.edit()
                        .putInt("studentId", 0)
                        .putInt("teacherId", 0)
                        .putInt("subjectId",0)
                        .putInt("classId",0)
                        .putString("className","")
                        .apply();

                //清除所有backStack
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                //回到登入頁面
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new LoginFragment()).commit();

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
