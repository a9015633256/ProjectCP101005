package com.example.yangwensing.myapplication.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

public class TeacherHomeworkAddFragment extends Fragment {
    //    private BottomNavigationView bottomNavigationView;
    private EditText etTitle, etContent;
    private Button btAdd;

    //接上一頁資料用
    private int classId = 0;
    private int teacherId = 0;
    private int subjectId = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_homework_add, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true
        getActivity().setTitle(R.string.title_homeworkAdd);

        findViews(view);

//        bottomNavigationView = getActivity().findViewById(R.id.navigation);
//        bottomNavigationView.setVisibility(View.GONE);

        //取得上一頁資料
        Bundle bundle = getArguments();
        if(bundle!=null){
            classId = bundle.getInt("classId");
            teacherId = bundle.getInt("teacherId");
            subjectId = bundle.getInt("subjectId");
        }


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.networkConnected(getActivity())) {

                    //檢查是否上一頁資料正確傳入
                    if (classId * teacherId * subjectId == 0 ){
                        Common.showToast(getActivity(),R.string.text_data_error);
                        return;
                    }


                    String title = etTitle.getText().toString().trim();
                    String content = etContent.getText().toString().trim();
                    Homework homework = new Homework(subjectId, teacherId, title, content, classId);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "insertHomework");
                    jsonObject.addProperty("homework", new Gson().toJson(homework));

                    int newHomeworkId = -1;
                    try {
                        String jsonIn = new MyTask(Common.URLForMingTa + "/HomeworkServlet", jsonObject.toString()).execute().get();
                        newHomeworkId = Integer.valueOf(jsonIn);

                        if (newHomeworkId == -1) {

                            Common.showToast(getActivity(), "Establish homework failed!!");
                        } else {

                            Common.showToast(getActivity(), "Establish succeeded!!");
                            getFragmentManager().beginTransaction().replace(R.id.content, new TeacherHomeworkFragment()).commit();


                        }


                    } catch (Exception e) {
                        Common.showToast(getActivity(), R.string.text_no_server);

                        e.printStackTrace();
                    }


                } else {
                    Common.showToast(getActivity(), R.string.text_no_network);


                }
            }

        });


        return view; //要改成回傳view
    }

    private void findViews(View view) {
        etTitle = view.findViewById(R.id.etAddHomeworkTitle);
        etContent = view.findViewById(R.id.etAddHomeworkContent);
        btAdd = view.findViewById(R.id.btAddHomework);


    }

    @Override
    public void onStop() {
//        bottomNavigationView.setVisibility(View.VISIBLE);

        super.onStop();
    }


}

