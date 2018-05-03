package com.example.yangwensing.myapplication.homework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.JsonObject;

public class StudentHomeworkDetailFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private EditText etTitle, etContent;

    //接上一頁資料用
    private HomeworkIsDone homeworkIsDone;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_homework_detail, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true

        findViews(view);

        bottomNavigationView = getActivity().findViewById(R.id.navigation);
        bottomNavigationView.setVisibility(View.GONE);


        //取得上一頁資料
        Bundle bundle = getArguments();
        if (bundle != null) {
            homeworkIsDone = (HomeworkIsDone) bundle.getSerializable("homework");
        }

        if (homeworkIsDone != null) {
            etTitle.setText(homeworkIsDone.getTitle());
            etContent.setText(homeworkIsDone.getContent());
        } else {
            Common.showToast(getActivity(), R.string.text_data_error);
        }


        getActivity().setTitle(homeworkIsDone.getSubject()+"   授課老師:"+homeworkIsDone.getTeacher());


        return view; //要改成回傳view
    }

    private void findViews(View view) {
        etTitle = view.findViewById(R.id.etAddHomeworkTitle);
        etContent = view.findViewById(R.id.etAddHomeworkContent);


    }

    @Override
    public void onStop() {
        bottomNavigationView.setVisibility(View.VISIBLE);

        super.onStop();
    }


}

