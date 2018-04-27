package com.example.yangwensing.myapplication.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yangwensing.myapplication.R;

public class StudentInfoEditFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_info_edit, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true
        getActivity().setTitle(R.string.title_infoEdit);

        bottomNavigationView = getActivity().findViewById(R.id.navigation);
        bottomNavigationView.setVisibility(View.GONE);

        return view; //要改成回傳view
    }

    @Override
    public void onStop() {
        bottomNavigationView.setVisibility(View.VISIBLE);

        super.onStop();
    }


}

