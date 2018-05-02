package com.example.yangwensing.myapplication.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.yangwensing.myapplication.R;

public class StudentInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_info, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true
        getActivity().setTitle(R.string.title_info);


        setHasOptionsMenu(true); //這樣onCreateOptionsMenu()才有效、才能加optionsMenu進activity的options


        return view; //要改成回傳view
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_options_edit_info, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_editInfo:
                getFragmentManager().beginTransaction().replace(R.id.content, new StudentInfoEditFragment()).addToBackStack(null).commit();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

