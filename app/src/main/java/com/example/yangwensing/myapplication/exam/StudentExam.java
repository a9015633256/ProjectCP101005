package com.example.yangwensing.myapplication.exam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yangwensing.myapplication.ExamSubject.Exam;
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
    private List<SectionData> hashSet = new ArrayList<>();
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

                if (exams != null){
                    for (Exam exam : exams){
                        String examSubjectId = String.valueOf(exam.getExamsubjectid());
                        int index = hashSet.indexOf(new SectionData(examSubjectId));
                        if (index == -1){
                            SectionData sectionData = new SectionData(examSubjectId);
                            hashSet.add(sectionData);
                            sectionData.add(exam);
                        }else{
                            hashSet.get(index).add(exam);
                        }
                    }
                }


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (exams == null || exams.isEmpty()) {
                Common.showToast(getActivity(), "No User Found");
            } else {
                recyclerView.setAdapter(new StudentExam.ExamAdapter(getActivity(),hashSet));//context物件跟spots傳進來
            }
        } else {
            Common.showToast(getActivity(), "No network connection available");
        }




        return  view;
    }

     class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {
         private Context context;
         private List<SectionData> hashSet;
        public ExamAdapter(Context context, List<SectionData> hashSet) {
            this.hashSet = hashSet;
            this.context = context;
        }


         @NonNull
         @Override
         public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
             View itemView = layoutInflater.inflate(R.layout.sujbect_title, parent, false);
             return new ViewHolder(itemView);
         }

         @Override
         public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
             final Bundle bundle = new Bundle();

             final SectionData sectionData = hashSet.get(position);
             switch (sectionData.getExamSujbectId()){
                 case "6":
                     holder.tvSubjectTitle.setText("JAVA");
                     break;
                 case "7":
                     holder.tvSubjectTitle.setText("Android_Studio");
                     break;
                 case "8":
                     holder.tvSubjectTitle.setText("Math");
                     break;
                 case "9":
                     holder.tvSubjectTitle.setText("English");
                     break;
                 case "10":
                     holder.tvSubjectTitle.setText("Chinese");
                     break;
                     default:
                         holder.tvSubjectTitle.setText("12345");
                         break;
             }




             for (int i = 0; i < sectionData.size(); i++) {
                 final Exam exam = (Exam) sectionData.get(i);
                 TextView tvSubject;
                 TextView tvSignIn;
                 ImageView imSignIn;
                 tvSubject = holder.linearLayout.getChildAt(i).findViewById(R.id.tvExam);
                 tvSubject.setText("考試科目 " + exam.getExamtitle());
                 tvSubject.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Exam X = null;
                         int id = exam.getSubjectid();
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
                                 b.putString("id", String.valueOf(exam.getSubjectid()));
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
                 String text = exam.getExamtitle();
                 tvSignIn = holder.linearLayout.getChildAt(i).findViewById(R.id.tvSignIn);
                 tvSignIn.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         Fragment fragment = new StudentExamQueryScore();
                         bundle.putString("ID", Classid);
                         Subject = String.valueOf(exam.getSubjectid());
                         Achievement = String.valueOf(exam.getAchievementid());

                         bundle.putString("Subject", Subject);
                         bundle.putString("Achievement", Achievement);


                         fragment.setArguments(bundle);

                         FragmentManager fragmentManager = getFragmentManager();
                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                         fragmentTransaction.replace(R.id.content, fragment);
                         fragmentTransaction.addToBackStack(null);
                         fragmentTransaction.commit();
                     }
                 });
                 imSignIn = holder.linearLayout.getChildAt(i).findViewById(R.id.ivEditAchievement);
                 imSignIn.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Fragment fragment = new StudentExamQueryScore();
                         bundle.putString("ID", Classid);
                         Subject = String.valueOf(exam.getSubjectid());
                         Achievement = String.valueOf(exam.getAchievementid());
                         bundle.putString("Subject", Subject);
                         bundle.putString("Achievement", Achievement);


                         fragment.setArguments(bundle);

                         FragmentManager fragmentManager = getFragmentManager();
                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                         fragmentTransaction.replace(R.id.content, fragment);
                         fragmentTransaction.addToBackStack(null);
                         fragmentTransaction.commit();
                     }
                 });


                 Teacher = String.valueOf(exam.getTeacherid());
                 Subject = String.valueOf(exam.getExamsubjectid());
                 Achievement = String.valueOf(exam.getAchievementid());
                 SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
                 preferences.edit().putString("Subject", Subject)
                         .putString("ClassID", Classid)
                         .apply();


                 bundle.putString("name", text);



             }
             for (int i = sectionData.size(); i < getMaxSujbectExam(); i++) {
                 holder.linearLayout.getChildAt(i).setVisibility(View.GONE);

             }


             holder.itemView.findViewById(R.id.tvSubjectTitle).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if (holder.linearLayout.getVisibility() == View.GONE) {
                         holder.linearLayout.setVisibility(View.VISIBLE);


                     } else {
                         holder.linearLayout.setVisibility(View.GONE);
                     }

                 }
             });
         }




         @Override
         public int getItemCount() {
             return hashSet.size();
         }

          class ViewHolder extends RecyclerView.ViewHolder {
           TextView tvSubjectTitle;
           LinearLayout linearLayout;
             public ViewHolder(View itemView) {
                 super(itemView);
                tvSubjectTitle = itemView.findViewById(R.id.tvSubjectTitle);
                linearLayout = itemView.findViewById(R.id.SujbectLinear);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                int maxSubjctExam = getMaxSujbectExam();
                for(int i = 0;i < maxSubjctExam; i ++){
                    View childView = layoutInflater.inflate(R.layout.subject_item,linearLayout,true);
                    childView.setId(i);
                    linearLayout.setVisibility(View.GONE);
                }


             }
         }
         int getMaxSujbectExam(){
             int maxSubjectExam = 0;
             for(int i = 0; i < hashSet.size(); i ++){
                 if (maxSubjectExam < hashSet.get(i).size()){
                     maxSubjectExam = hashSet.get(i).size();
                 }
             }
             return  maxSubjectExam;
         }
     }




    @Override
    public void onStop() {
        super.onStop();
        if (myTask != null) {
            myTask.cancel(true);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hashSet.clear();
    }
}
