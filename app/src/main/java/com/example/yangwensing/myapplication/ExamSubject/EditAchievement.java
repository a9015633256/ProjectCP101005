package com.example.yangwensing.myapplication.ExamSubject;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.charts.TeacherExamChartFragment;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EditAchievement extends Fragment {
    private final static String TAG = "EditAchievementSignin";
    private MyTask myTask;
    private String ClassID  = "";
    private String ExamSubjectID,AchievementID;
    private TextView tvSubject,tvClassname,tvTeacherName;
    private ImageView ivAnalysis;
    private Button bttUpete,btSure;
    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subject_achievement_signin, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.reNumber);
        getActivity().setTitle(R.string.updateAchievement);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        tvSubject = view.findViewById(R.id.tvSubjectt);
        tvClassname = view.findViewById(R.id.tvClassc);
        tvTeacherName = view.findViewById(R.id.tvTeacherr);
        ivAnalysis = view.findViewById(R.id.ivAnalysis);
        bttUpete = view.findViewById(R.id.btUpdateAchievement);
        bottomNavigationView = getActivity().findViewById(R.id.btNavigation_Bar);

        btSure = view.findViewById(R.id.btSend);
        Bundle b = getArguments();
        ExamSubjectID = b.getString("Subjectid");
        AchievementID = b.getString("Achievementid");
        ClassID = b.getString(("ClassID"));
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("ClassID",ClassID);
                Fragment fragment = new ExamFragment();
                fragment.setArguments(b);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        ivAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                String text = bundle.getString("name");

                Bundle bundle2 = new Bundle();
                bundle2.putInt("examId",Integer.valueOf(ExamSubjectID));
                bundle2.putString("examName",text);

                Fragment fragment = new TeacherExamChartFragment();

                fragment.setArguments(bundle2);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, fragment);
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
                recyclerView.setAdapter(new Recyclerr(getActivity(), exams));//context物件跟spots傳進來
            }
        } else {
            Common.showToast(getActivity(), "No network connection available");
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //隱藏底部導覽列
        bottomNavigationView.setVisibility(View.GONE);

    }

    @Override
    public void onStop() {
        //重新顯示底部導覽列
        bottomNavigationView.setVisibility(View.VISIBLE);
        super.onStop();
    }

    private class Recyclerr extends RecyclerView.Adapter<Recyclerr.ViewHolder> {
        LayoutInflater layoutInflater;
        List<Exam> exams;
        public Recyclerr(FragmentActivity activity, List<Exam> exams) {
            layoutInflater = LayoutInflater.from(activity);
            this.exams = exams;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.subject_achievement_item,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final Exam exam = exams.get(position);
            String nember = exam.getStudentid();
            final String name = exam.getName();
            String score = String.valueOf(exam.getScore());
            holder.tvNumber.setText(nember);
            holder.tvName.setText(name);
            holder.etAchievement.setText(score);
            String t  = exam.getTitle();
            String c = exam.getClassname();
            String e = exam.getTeachername();
            tvSubject.setText((getText(R.string.ExamSubject1)+t));
            tvClassname .setText(c);
            tvTeacherName.setText(e);

            holder.etAchievement.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {

//                        holder.etAchievement.setHint(null);
                    } else {
//                        holder.etAchievement.setHint("NO");

                        int Score = Integer.valueOf(holder.etAchievement.getText().toString());
                        exam.setScore(Score);
//                        Common.showToast(getActivity(),"");
                    }
                }
            });

            bttUpete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = getArguments();
                    AchievementID = String.valueOf(exam.getAchievementID());
                    Gson gson = new Gson();
                    String score = holder.etAchievement.getText().toString();
                    if (Common.networkConnected(getActivity())){
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action","insertAchievement");
                        jsonObject.addProperty("scores", gson.toJson(exams));
                        jsonObject.addProperty("score",score);
                        jsonObject.addProperty("AchievementID",AchievementID);

                        int count = 0;
                        myTask = new MyTask(Common.URLForHen+"/LoginHelp",jsonObject.toString());
                        try {
                            String result = myTask.execute().get();
                            count = Integer.valueOf(result);
                        }  catch (Exception e) {
                            Log.e(TAG,e.toString());
                        }if (count == 0 ){
                            Common.showToast(getActivity(),"update fail");
                        }else {
                            Recyclerr.this.notifyDataSetChanged();
                            Common.showToast(getActivity(),"update success");

                        }
                    }
                }
            });

        }


        @Override
        public int getItemCount() {
            return exams.size();
        }

         class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvNumber, tvName;
             EditText etAchievement;

             public ViewHolder(View itemView) {
                super(itemView);
                 tvNumber = itemView.findViewById(R.id.tvNumber);
                 tvName = itemView.findViewById(R.id.tvName);
                 etAchievement = itemView.findViewById(R.id.etAchievement);
            }
        }
    }
}
