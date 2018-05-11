package com.example.yangwensing.myapplication.ExamSubject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class AchievementFragment extends Fragment {
    private final static String TAG = "MainFragment";
    private MyTask myTask;
    private TextView tvSubject, tvClass,tvTeacher;
    private Button btSend;
    private RecyclerView renumber;
    private ImageView ivAnalysis;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.subject_achievement_signin, container, false);
        tvSubject = view.findViewById(R.id.tvSubject);
        btSend = view.findViewById(R.id.btSend);
        tvSubject = view.findViewById(R.id.tvSubject);
        renumber = view.findViewById(R.id.reNumber);
        tvTeacher = view.findViewById(R.id.tvTeacher);
        tvClass = view.findViewById(R.id.tvClass);
        ivAnalysis = view.findViewById(R.id.ivAnalysis);
        ivAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PerformanceAnalysisChart();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        renumber.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        List<Exam> exams = new ArrayList<>();
        int Class = 2;

        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/Subject";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "id");
            jsonObject.addProperty("ClassID", Class);
            String jsonOut = jsonObject.toString();


            myTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = myTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<Exam>>() {
                }.getType();//
                exams = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (exams == null || exams.isEmpty()) {
                Common.showToast(getActivity(), "No User Found");
            } else {
                renumber.setAdapter(new ReNumber(getActivity(), exams));//context物件跟spots傳進來
            }
        } else {
            Common.showToast(getActivity(), "No network connection available");
        }


        Bundle bundle = getArguments();
        String text = bundle.getString("name");
        tvSubject.setText(text);


        return view;


    }


    private class ReNumber extends RecyclerView.Adapter<ReNumber.Holder> {
        LayoutInflater layoutInflater;
        List<Exam> exams;

        public ReNumber(FragmentActivity activity, List<Exam> exams) {
            this.exams = exams;
            layoutInflater = LayoutInflater.from(activity);
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemview = layoutInflater.inflate(R.layout.subject_achievement_item, parent, false);
            return new Holder(itemview);
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, int position) {
            final Exam exam = exams.get(position);
            String Number = exam.getStudentid();
            String Name = exam.getName();
            String ClassName = exam.getClassname();
            String TeacherName = String.valueOf(exam.getTeachername());
            holder.tvNumber.setText(Number);
            holder.tvName.setText(Name);
            tvClass.setText(ClassName);
            tvTeacher.setText(TeacherName);





            btSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isValid = true;

                    int achievementid = 5;
                    String score = holder.etAchievement.getText().toString();
                    if (score.trim().isEmpty()) {
                        holder.etAchievement.setError("IsValid Achievement");
                        isValid = false;
                    }
                    if (isValid) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "insert");
                        jsonObject.addProperty("id",achievementid);
                        jsonObject.addProperty("score",score);

                        myTask = new MyTask(Common.URL + "/Subject", jsonObject.toString());

                        try {
                            int count = Integer.valueOf(myTask.execute().get());
                            if (count == 0) {
                                Toast.makeText(getActivity(), "Add failed!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Fragment fragment = new ExamFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.main_content, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }

                        } catch (Exception e) {
                            Log.e(TAG, "error massage" + e.toString());
                        }

                    } else {
                        Toast.makeText(getActivity(), "connection to network failed",
                                Toast.LENGTH_SHORT).show();
                    }


                }
            });






        }


        @Override
        public int getItemCount() {
            return exams.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvNumber, tvName;
            EditText etAchievement;


            public Holder(View itemview) {
                super(itemview);
                tvNumber = itemview.findViewById(R.id.tvNumber);
                tvName = itemview.findViewById(R.id.tvName);
                etAchievement = itemview.findViewById(R.id.etAchievement);



            }
        }
    }

}
