package com.example.yangwensing.myapplication.exam;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.example.yangwensing.myapplication.ExamSubject.AchievementFragment;
import com.example.yangwensing.myapplication.ExamSubject.Exam;
import com.example.yangwensing.myapplication.ExamSubject.ExamFragment;
import com.example.yangwensing.myapplication.ExamSubject.UpDateSubject;
import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StudentExam extends Fragment {
    private final static String TAG = "ExamFragment";
    private MyTask myTask;
    private RecyclerView recyclerView;
    String user = "";
    String Classid = "";
    String Subject = "";
    String Teacher = "";
    String teacherid = "";
    String Achievement = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_examsubject, container, false);
        getActivity().setTitle(R.string.title_examOverview);
        recyclerView = view.findViewById(R.id.studentExam);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        List<Exam> exams = new ArrayList<>();
        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        Classid = String.valueOf(preferences.getInt("studentClassID",0));


        if (Common.networkConnected(getActivity())) {
            String url = Common.URLForHen + "/LoginHelp";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "Exam");
            jsonObject.addProperty("id", Classid);
            String jsonOut = jsonObject.toString();


            myTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = myTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<Exam>>() {
                }.getType();
                exams = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (exams == null || exams.isEmpty()) {
                Common.showToast(getActivity(), "No User Found");
            } else {
                recyclerView.setAdapter(new StudentExam.ExamAdapter(getActivity(), exams));//context物件跟spots傳進來
            }
        } else {
            Common.showToast(getActivity(), "No network connection available");
        }




        return  view;
    }

     class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {
         private LayoutInflater layoutInflater;
         private List<Exam> exams;
        public ExamAdapter(FragmentActivity activity, List<Exam> exams) {
            this.exams = exams;
            layoutInflater = LayoutInflater.from(activity);
        }

         @NonNull
         @Override
         public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View itemView = layoutInflater.inflate(R.layout.student_examsubject_item, parent, false);
             return new ViewHolder(itemView);
         }

         @Override
         public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
             final Bundle bundle = new Bundle();
             final Exam exams1 = exams.get(position);
             final String text = getString(R.string.ExamSubject1) + " " + exams1.getExamtitle();

             Teacher = String.valueOf(exams1.getTeacherid());
             Subject = String.valueOf(exams1.getExamsubjectid());
             Achievement = String.valueOf(exams1.getAchievementid());
             SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
             preferences.edit().putString("Subject", Subject)
                     .putString("ClassID", Classid)
                     .apply();


             bundle.putString("name", text);
             holder.tvExam.setText(text);


             holder.tvQuery.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Fragment fragment = new StudentExamQueryScore();
                     bundle.putString("ID", Classid);
                     Subject = String.valueOf(exams1.getSubjectid());
                     Achievement = String.valueOf(exams1.getAchievementid());
                     bundle.putString("Subject", Subject);
                     bundle.putString("Achievement",Achievement);


                     fragment.setArguments(bundle);

                     FragmentManager fragmentManager = getFragmentManager();
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.content, fragment);
                     fragmentTransaction.addToBackStack(null);
                     fragmentTransaction.commit();
                 }
             });



             holder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Exam X = null;

                     String id = String.valueOf(exams1.getSubjectid());

                     if (Common.networkConnected(getActivity())) {
                         JsonObject jsonObject = new JsonObject();
                         jsonObject.addProperty("action", "findByitem");
                         jsonObject.addProperty("item", id);
                         try {
                             myTask = new MyTask(Common.URLForHen + "/LoginHelp", jsonObject.toString());
                             String jsonIn = myTask.execute().get();
                             Log.d(TAG, jsonIn);
                             X = new Gson().fromJson(jsonIn, Exam.class);
                         } catch (Exception e) {
                             Log.e(TAG, e.toString());
                         }
                         if (X == null) {
                             Common.showToast(getActivity(), "Not Found");
                         } else {
                             String title = X.getTitle();
                             String date = X.getDate();
                             String content = X.getContext();
                             Bundle b = new Bundle();
                             Fragment fragment = new StudentExamQuery();
                             b.putString("title", title);
                             b.putString("date", date);
                             b.putString("content", content);
                             b.putString("id", String.valueOf(exams1.getSubjectid()));
                             b.putString("ClassID", Classid);
                             fragment.setArguments(b);
                             FragmentManager fragmentManager = getFragmentManager();
                             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                             fragmentTransaction.replace(R.id.content, fragment);
                             fragmentTransaction.addToBackStack(null);
                             fragmentTransaction.commit();
                             recyclerView.getAdapter().notifyDataSetChanged();


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
            TextView tvExam,tvQuery;
             public ViewHolder(View itemView) {
                 super(itemView);
                 tvExam = itemView.findViewById(R.id.tvExam);
                 tvQuery = itemView.findViewById(R.id.tvQuery);
             }
         }
     }
}
