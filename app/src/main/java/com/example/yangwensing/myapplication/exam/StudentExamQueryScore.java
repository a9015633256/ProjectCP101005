package com.example.yangwensing.myapplication.exam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yangwensing.myapplication.ExamSubject.EditAchievement;
import com.example.yangwensing.myapplication.ExamSubject.Exam;
import com.example.yangwensing.myapplication.ExamSubject.PerformanceAnalysisChart;
import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.charts.StudentSingleExamChartFragment;
import com.example.yangwensing.myapplication.charts.TeacherSingleExamChartFragment;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StudentExamQueryScore extends Fragment {
    private final static String TAG = "EditAchievementSignin";
    private MyTask myTask;
    private String ClassID  = "";
    private String ExamSubjectID,AchievementID;
    private TextView tvSubject,tvClassname,tvTeacherName;
    private ImageView ivAnalysis;
    private BottomNavigationView bottomNavigationView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_score, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.reNumber);
        getActivity().setTitle(R.string.query);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        tvSubject = view.findViewById(R.id.tvSubjectt);
        tvClassname = view.findViewById(R.id.tvClassc);
        tvTeacherName = view.findViewById(R.id.tvTeacherr);
        ivAnalysis = view.findViewById(R.id.ivAnalysis);
        Bundle b = getArguments();
        ExamSubjectID = b.getString("Subject");
        AchievementID = b.getString("Achievement");
        ClassID = b.getString(("ID"));

        bottomNavigationView = getActivity().findViewById(R.id.bnForStudent);
        bottomNavigationView.setVisibility(View.GONE);

        ivAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                String text = bundle.getString("name");

                Bundle bundle2 = new Bundle();
                bundle2.putInt("examId",Integer.valueOf(ExamSubjectID));
                bundle2.putString("examName",text);

                Fragment fragment = new StudentSingleExamChartFragment();

                fragment.setArguments(bundle2);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        List<Exam> exams = new ArrayList<>();

        if (Common.networkConnected(getActivity())) {
            String url = Common.URLForHen + "/LoginHelp";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "id");
            jsonObject.addProperty("Achievementid",AchievementID);
            jsonObject.addProperty("ExamSubjectID", ExamSubjectID);
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
                recyclerView.setAdapter(new StudentExamQueryScore.ScoreAdapter(getActivity(), exams));//context物件跟spots傳進來
            }
        } else {
            Common.showToast(getActivity(), "No network connection available");
        }

        return  view;
    }

    private class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.MyViewHolder> {
        LayoutInflater layoutInflater;
        List<Exam> exams;
        public ScoreAdapter(FragmentActivity activity, List<Exam> exams) {
            layoutInflater = LayoutInflater.from(activity);
            this.exams = exams;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.student_score_item,parent,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Exam exam = exams.get(position);
            String nember = exam.getStudentid();
            final String name = exam.getName();
            String score = String.valueOf(exam.getScore());
            holder.tvNumber.setText(nember);
            holder.tvName.setText(name);
            holder.tvAchievement.setText(score);
            String t  = exam.getTitle();
            String c = exam.getClassname();
            String e = exam.getTeachername();
            tvSubject.setText((getText(R.string.ExamSubject1)+t));
            tvClassname .setText(c);
            tvTeacherName.setText(e);

        }


        @Override
        public int getItemCount() {
            return exams.size();
        }

         class MyViewHolder extends RecyclerView.ViewHolder {
             TextView tvNumber, tvName,tvAchievement;
            public MyViewHolder(View itemView) {
                super(itemView);
                tvNumber = itemView.findViewById(R.id.tvNumber);
                tvName = itemView.findViewById(R.id.tvName);
                tvAchievement = itemView.findViewById(R.id.tvAchievement);
            }
        }
    }
    @Override
    public void onStop() {
        bottomNavigationView.setVisibility(View.VISIBLE);

        super.onStop();
    }
}
