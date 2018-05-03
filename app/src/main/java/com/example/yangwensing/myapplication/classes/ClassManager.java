package com.example.yangwensing.myapplication.classes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ClassManager extends Fragment {

    private static final String TAG = "ClassManager";
//    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvClass;
    private MyTask classgetTask;


    String user ="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_teacher_classlist, container, false);

        Bundle bundle = getArguments();
        user = bundle.getString("name");



        rvClass = view.findViewById(R.id.rvNews);
        rvClass.setLayoutManager(new LinearLayoutManager(getActivity()));
//        swipeRefreshLayout =
//                view.findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                showAllClasses();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });

        return view;
    }

    private void showAllClasses() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/LoginHelp";

            List<Classes> classes = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            jsonObject.addProperty("name",user);
            String jsonOut = jsonObject.toString();
            classgetTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = classgetTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<Classes>>() {
                }.getType();
                classes = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (classes == null || classes.isEmpty()) {
                Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show();

            } else {
                rvClass.setAdapter(new ClassesRecyclerViewAdapter(getActivity(), classes));
            }
        } else {
            Toast.makeText(getActivity(), "No Net", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        showAllClasses();
    }


    private class ClassesRecyclerViewAdapter extends RecyclerView.Adapter<ClassesRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Classes> classes;


        ClassesRecyclerViewAdapter(Context context, List<Classes> classes) {
            layoutInflater = LayoutInflater.from(context);
            this.classes = classes;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.fragment_teacher_class_manager, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public int getItemCount() {
            return classes.size();
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Classes c = classes.get(position);
            String url = Common.URL + "/LoginHelp";
            holder.tvClass.setText(c.getClasses());
            holder.tvTeacher.setText(c.getTeacher());

        }



        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvClass, tvTeacher;

            MyViewHolder(View itemView) {
                super(itemView);
                tvClass = itemView.findViewById(R.id.tvClass);
                 tvTeacher= itemView.findViewById(R.id.tvTeacher);

            }
        }
    }
    public void onStop() {
        super.onStop();
        if (classgetTask != null) {
            classgetTask.cancel(true);
        }
    }
}
