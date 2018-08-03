package com.example.yangwensing.myapplication.ExamSubject;


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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExamFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.subject_all, container, false);
        getActivity().setTitle(R.string.title_examOverview);
        recyclerView = view.findViewById(R.id.recyclerr);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        List<Exam> exams = new ArrayList<>();
        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        Classid = String.valueOf(preferences.getInt("classId",0));


        Button btAdd = view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bb = new Bundle();
                SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
                Classid = String.valueOf(preferences.getInt("classId",0));
                bb.putString("ClassID", Classid);
                Fragment fragment = new CreateSubject();
                fragment.setArguments(bb);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }


        });
        setHasOptionsMenu(true);


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
                recyclerView.setAdapter(new ExamAdapter(getActivity(), exams));//context物件跟spots傳進來
            }
        } else {
            Common.showToast(getActivity(), "No network connection available");
        }
        return view;
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
            View itemView = layoutInflater.inflate(R.layout.subject_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final Bundle bundle = new Bundle();
            final Exam exams1 = exams.get(position);
            final String text = getString(R.string.ExamSubject1) + " " + exams1.getExamtitle();

            Teacher = String.valueOf(exams1.getTeacherid());
            Subject = String.valueOf(exams1.getExamsubjectid());
            Achievement = String.valueOf(exams1.getAchievementid());
            SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
            preferences.edit().putString("Subject", Subject)
//                    .putString("Teacher", Teacher)
                    .putString("ClassID", Classid)
                    .apply();


            bundle.putString("name", text);
            holder.tvExam.setText(text);
            holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();

                    String Studentid= String.valueOf(exams1.getSubjectid());
                    String Achievementid = String.valueOf(exams1.getAchievementid());
                    b.putString("Subjectid",Studentid);
                    b.putString("Achievementid",Achievementid);
                    b.putString("Title",exams1.getTitle());
                    b.putString("ClassID",Classid);
                    b.putString("classname",exams1.getClassname());
                    b.putString("score", String.valueOf(exams1.getScore()));
                    b.putString("teachername",exams1.getTeachername());

                    Fragment fragment = new EditAchievement();
                    fragment.setArguments(b);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            holder.tvSingIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment fragment = new AchievementFragment();
                    bundle.putString("ID", Classid);
                    Subject = String.valueOf(exams1.getSubjectid());
                    Achievement = String.valueOf(exams1.getAchievementid());
                    bundle.putString("Subject", Subject);
                    bundle.putString("Achievement",Achievement);


                    fragment.setArguments(bundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content, fragment);
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
//                            Type listType = new TypeToken<List<Exam>>() {
////                            }.getType();
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
                            Fragment fragment = new UpDateSubject();
                            b.putString("title", title);
                            b.putString("date", date);
                            b.putString("content", content);
                            b.putString("id", String.valueOf(exams1.getSubjectid()));
                            b.putString("ClassID", Classid);
                            fragment.setArguments(b);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_content, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            recyclerView.getAdapter().notifyDataSetChanged();


                        }

                    }

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        popupMenu = new PopupMenu(getActivity(), v, Gravity.END);
                    }
                    popupMenu.inflate(R.menu.menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            String Subject = String.valueOf(exams1.getSubjectid());
                            switch (item.getItemId()) {
                                case R.id.DeleteSubject:
                                    if (Common.networkConnected(getActivity())) {
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "deleteSubject");
                                        jsonObject.addProperty("subject", Subject);
                                        int count = 0;

                                        try {
                                            myTask = new MyTask(Common.URLForHen + "/LoginHelp", jsonObject.toString());
                                            String result = myTask.execute().get();
                                            count = Integer.valueOf(result);
                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (count == 0) {
                                            Common.showToast(getActivity(), "delete fail");
                                        }else {
                                            exams.remove(exams1);
                                            ExamAdapter.this.notifyDataSetChanged();
                                            Common.showToast(getActivity(), "success");
                                            recyclerView.invalidate();
                                        }
                                    }

                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });


        }


        @Override
        public int getItemCount() {
            return exams.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvExam;
            TextView tvSingIn;
            ImageView ivEdit;

            public ViewHolder(View itemView) {
                super(itemView);
                tvExam = itemView.findViewById(R.id.tvExam);
                tvSingIn = itemView.findViewById(R.id.tvSignIn);
                ivEdit = itemView.findViewById(R.id.ivEditAchievement);
            }

        }
    }
}