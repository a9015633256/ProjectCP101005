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
import com.example.yangwensing.myapplication.exam.StudentExamChartFragment;
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
    private TextView tvSubject, tvClass, tvTeacher;
    private Button btSend, bttUpete;
    ;
    private RecyclerView renumber;
    private ImageView ivAnalysis;
    private String Classid = "";
    private String ExamSubjectID = "";
    private String Studentid = "";
    private String Teacherid = "";
    private String AchievementID = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.subject_achievement_signin, container, false);
        getActivity().setTitle(R.string.AchievementSignin);
        btSend = view.findViewById(R.id.btSend);
        tvSubject = view.findViewById(R.id.tvSubjectt);
        renumber = view.findViewById(R.id.reNumber);
        tvTeacher = view.findViewById(R.id.tvTeacherr);
        tvClass = view.findViewById(R.id.tvClassc);
        ivAnalysis = view.findViewById(R.id.ivAnalysis);
        bttUpete = view.findViewById(R.id.btUpdateAchievement);
        bttUpete.setVisibility(View.GONE);


        Bundle b = getArguments();
        Classid = b.getString("ID");
        ExamSubjectID = b.getString("Subject");
        AchievementID = b.getString("Achievement");
        ivAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getArguments();
                int i   = b.getInt("e");
                int socre = b.getInt("score");
                String examName = b.getString("examName");
                b.putInt("ex",i);
                b.putInt("studentScore",socre);
                b.putString("examName",examName);
                Fragment fragment = new StudentExamChartFragment();
                fragment.setArguments(b);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        renumber.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        List<Exam> exams = new ArrayList<>();

        if (Common.networkConnected(getActivity())) {
            String url = Common.URLForHen + "/LoginHelp";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "id");
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
            String TeacherName = exam.getTeachername();
            String a = String.valueOf(exam.getAchievementID());
            holder.tvNumber.setText(Number);
            holder.tvName.setText(Name);
            tvClass.setText(ClassName);
            tvTeacher.setText(TeacherName);
            holder.etAchievement.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {

//                        holder.etAchievement.setHint(null);
                    } else {
//                        holder.etAchievement.setHint("NO");

                        int Score = Integer.valueOf(holder.etAchievement.getText().toString());
                        exam.setScore(Score);

                    }
                }
            });


            btSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isValid = true;

                    Studentid = String.valueOf(exam.getExamstudent());
                    AchievementID = String.valueOf(exam.getAchievementID());
                    String score = holder.etAchievement.getText().toString();
                    Gson gson = new Gson();
//                    if (score.trim().isEmpty()) {
//                        holder.etAchievement.setError("IsValid Achievement");
//                        isValid = false;
//                    }
                    if (isValid) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "insertAchievement");
                        jsonObject.addProperty("scores", gson.toJson(exams));
                        jsonObject.addProperty("score", score);
                        jsonObject.addProperty("AchievementID", AchievementID);

                        myTask = new MyTask(Common.URLForHen + "/LoginHelp", jsonObject.toString());

                        try {
                            int count = Integer.valueOf(myTask.execute().get());
                            if (count == 0) {
                                Toast.makeText(getActivity(), "Add failed!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Bundle b = new Bundle();
                                b.putString("score", score);
                                b.putString("ClassID", Classid);
                                Fragment fragment = new ExamFragment();
                                fragment.setArguments(b);
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.main_content, fragment);
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
