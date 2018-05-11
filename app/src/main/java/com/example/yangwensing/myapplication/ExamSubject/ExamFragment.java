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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subject_all, container, false);
        recyclerView = view.findViewById(R.id.recyclerr);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        List<Exam> exams = new ArrayList<>();


        Button btAdd = view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CreateSubject();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        setHasOptionsMenu(true);


        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/Subject";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "Exam");
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
            final String text ="ExamSubject : " + exams1.getExamtitle();
            bundle.putString("name", text);
            holder.tvExam.setText(text);
            holder.tvSingIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment fragment = new AchievementFragment();
                    bundle.putString("ID",String.valueOf(exams1.getId()));
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

                    String id = String.valueOf(exams1.getId());

                    if (Common.networkConnected(getActivity())){
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action","findByitem");
                        jsonObject.addProperty("item",id);
                        try {
                        myTask = new MyTask(Common.URL+"/Subject",jsonObject.toString());
                            String jsonIn = myTask.execute().get();
                            Log.d(TAG, jsonIn);
//                            Type listType = new TypeToken<List<Exam>>() {
////                            }.getType();
                            X = new Gson().fromJson(jsonIn, Exam.class);
                        }  catch (Exception e) {
                            Log.e(TAG,e.toString());
                        }
                        if ( X == null){
                            Common.showToast(getActivity(),"Not Found");
                        }
                        else {
                            String title = X.getTitle();
                            String date = X.getDate();
                            String content = X.getContext();
                            Bundle b = new Bundle();
                            Fragment fragment = new UpDateSubject();
                            b.putString("title",title);
                            b.putString("date",date);
                            b.putString("content",content);
                            b.putString("id", String.valueOf(exams1.getId()));
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
                            switch (item.getItemId()) {
                                case R.id.DeleteSubject:
                                    if (Common.networkConnected(getActivity())) {
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "delete");
                                        jsonObject.addProperty("subject", new Gson().toJson(exams1));
                                        int count = 0;

                                        try {
                                            myTask = new MyTask(Common.URL + "/Subject", jsonObject.toString());
                                            String result = myTask.execute().get();
                                            count = Integer.valueOf(result);
                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (count == 0) {
                                            Common.showToast(getActivity(), "delete fail");
                                        }
                                    } else {
                                        exams.remove(exams1);
                                        ExamAdapter.this.notifyDataSetChanged();
                                        Common.showToast(getActivity(), "success");
                                        recyclerView.invalidate();
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

            public ViewHolder(View itemView) {
                super(itemView);
                tvExam = itemView.findViewById(R.id.tvExam);
                tvSingIn = itemView.findViewById(R.id.tvSignIn);
            }

        }
    }
}