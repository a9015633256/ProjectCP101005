package com.example.yangwensing.myapplication.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

/**
 * Created by nameless on 2018/4/26.
 */


public class ClassDelete extends Fragment {


    private static final String TAG = "Classdelete";

    private RecyclerView rvDelete;
    private MyTask classdelteTask, checkdelteTask;
    private Button btdelete;
//    TextView tvClass, tvTeacher;


    String user = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Delete");
        View view = inflater.inflate(R.layout.classdeletelist, container, false);

        Bundle bundle = getArguments();
        user = bundle.getString("name");


        rvDelete = view.findViewById(R.id.rvClass);
        rvDelete.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }

    private void showAllClasses() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/LoginHelp";

            List<Classes> classes = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            jsonObject.addProperty("name", user);
            String jsonOut = jsonObject.toString();
            classdelteTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = classdelteTask.execute().get();
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
                rvDelete.setAdapter(new DeleteRecyclerViewAdapter(getActivity(), classes));

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


    private class DeleteRecyclerViewAdapter extends RecyclerView.Adapter<ClassDelete.DeleteRecyclerViewAdapter.MyViewHolder> {

        private LayoutInflater layoutInflater;
        private List<Classes> classes;

        DeleteRecyclerViewAdapter(Context context, List<Classes> classes) {
            layoutInflater = LayoutInflater.from(context);
            this.classes = classes;

        }

        @Override
        public DeleteRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.classdelete, parent, false);
            return new ClassDelete.DeleteRecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final DeleteRecyclerViewAdapter.MyViewHolder holder, int position) {
            final Classes c = classes.get(position);
            String url = Common.URL + "/LoginHelp";
            holder.tvClass.setText(c.getClasses());
            holder.tvTeacher.setText(c.getTeacher());

            holder.btcheckdlete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("注意")
                            .setMessage("學生資料會跟著消失")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (Common.networkConnected(getActivity())) {
                                String url = Common.URL + "/LoginHelp";
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("action", "delete");
                                jsonObject.addProperty("classname", c.getClasses());
                                jsonObject.addProperty("teachername", c.getTeacher());

                                String jsonOut = jsonObject.toString();
                                int count = 0;
                                checkdelteTask = new MyTask(url, jsonOut);
                                try {
                                    String result = checkdelteTask.execute().get();
                                    count = Integer.valueOf(result);
                                } catch (Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                                if (count == 0) {
                                    Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT);
                                } else {
                                    classes.remove(c);
                                    DeleteRecyclerViewAdapter.this.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT);
                                }
                            } else {
                                Toast.makeText(getActivity(), "no network", Toast.LENGTH_SHORT);
                            }
                        }
                    }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();


                }

            });


        }

        @Override
        public int getItemCount() {
            return classes.size();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvClass, tvTeacher;
            Button btcheckdlete;


            public MyViewHolder(View itemView) {
                super(itemView);
                tvClass = itemView.findViewById(R.id.tvClass);
                tvTeacher = itemView.findViewById(R.id.tvTeacher);
                btcheckdlete = itemView.findViewById(R.id.btcheckdelete);
            }
        }


    }

    public void onStop() {
        super.onStop();
        if (classdelteTask != null) {
            classdelteTask.cancel(true);
        }
    }


}
