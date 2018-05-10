package com.example.yangwensing.myapplication.contact;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yangwensing.myapplication.R;

import java.util.List;

public class StudentContactDetailFragment extends Fragment {
    private List<Teacher> teachers;
    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_contact_detail, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true

        getActivity().setTitle(R.string.title_contactDetail);

        findViews(view);

        bottomNavigationView = getActivity().findViewById(R.id.bnForStudent);
        bottomNavigationView.setVisibility(View.GONE);

        return view; //要改成回傳view
    }


    private void findViews(View view) {

    }

    @Override
    public void onStop() {
        bottomNavigationView.setVisibility(View.VISIBLE);
        super.onStop();
    }
}
