package com.example.yangwensing.myapplication.exam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.MyTask;

public class StudentExamQuery extends Fragment {
    private final static String TAG = "MainFragment";
    private MyTask myTask;
    private TextView tvDate, tvTitle, tvContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_examsubject_query, container, false);
        tvTitle = view.findViewById(R.id.tvTitlE);
        tvDate = view.findViewById(R.id.tvDatE);
        tvContent = view.findViewById(R.id.tvContenT);
        Bundle q = getArguments();
        String title = q.getString("title");
        String date = q.getString("date");
        String content = q.getString("content");
        tvTitle.setText(title);
        tvDate.setText(date);
        tvContent.setText(content);
        return view;
    }
}
