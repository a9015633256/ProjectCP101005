package com.example.yangwensing.myapplication.charts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yangwensing.myapplication.R;

public class BackUpStudentExamFragment extends Fragment {
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_exam, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true

        getActivity().setTitle(R.string.title_examOverview);

        findViews(view);


        return view; //要改成回傳view
    }


    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.rvExam);

    }





}
