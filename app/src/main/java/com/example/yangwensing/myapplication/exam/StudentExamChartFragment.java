package com.example.yangwensing.myapplication.exam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.yangwensing.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class StudentExamChartFragment extends Fragment {
    private EditText etExamName, etStudentScore, etScoreA, etScoreB, etScoreC, etScoreD, etScoreE, etScoreAverage;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_exam_chart, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true

        findViews(view);


        //假資料
        List<Integer> scoreList = new ArrayList<>();
        scoreList.add(100);
        scoreList.add(50);
        scoreList.add(80);
        scoreList.add(88);
        scoreList.add(76);
        scoreList.add(99);
        scoreList.add(50);
        scoreList.add(33);
        scoreList.add(40);
        scoreList.add(55);
        scoreList.add(50);
        scoreList.add(33);
        scoreList.add(33);
        scoreList.add(345);
        scoreList.add(54);
        scoreList.add(2);

//        int[] scores = scoreList.toArray()[scoreList.size()];


//        //取得上一頁資料
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            homeworkIsDone = (HomeworkIsDone) bundle.getSerializable("homework");
//        }
//
//        if (homeworkIsDone != null) {
//            etTitle.setText(homeworkIsDone.getTitle());
//            etContent.setText(homeworkIsDone.getContent());
//        } else {
//            Common.showToast(getActivity(), R.string.text_data_error);
//        }


        //設標題
//        getActivity().setTitle(homeworkIsDone.getSubject()+"   授課老師:"+homeworkIsDone.getTeacher());


        return view; //要改成回傳view
    }

    private void findViews(View view) {
        etExamName = view.findViewById(R.id.tvExamName);
        etStudentScore = view.findViewById(R.id.tvScore);
        etScoreA = view.findViewById(R.id.tvScoreA);
        etScoreB = view.findViewById(R.id.tvScoreB);
        etScoreC = view.findViewById(R.id.tvScoreC);
        etScoreD = view.findViewById(R.id.tvScoreD);
        etScoreE = view.findViewById(R.id.tvScoreE);
        etScoreAverage = view.findViewById(R.id.tvScoreAverage);


    }



}

