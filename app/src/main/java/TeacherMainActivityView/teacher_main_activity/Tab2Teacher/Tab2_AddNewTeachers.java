package TeacherMainActivityView.teacher_main_activity.Tab2Teacher;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import TeacherMainActivityView.CommonPart.Common;
import TeacherMainActivityView.CommonPart.MyTask;
import TeacherMainActivityView.teacher_main_activity.MainActivity;


public class Tab2_AddNewTeachers extends Fragment {
    private final static String TAG = "Tab2_AddNewTeachers";
    private Button btSearchTeacher;
    private EditText edSearchTeacher;
    private RecyclerView rlTeachers;
    private MyTask SearchTask;
    private TeacherGetImageTask teacherGetImageTask;
    private MyTask InsertTask;
    private int classId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_tab2_add_new_teacher, container, false);
        btSearchTeacher = view.findViewById(R.id.btSearchTeacher);
        edSearchTeacher = view.findViewById(R.id.edSearchTeacher);
        rlTeachers = view.findViewById(R.id.rlTeachers);
        rlTeachers.setLayoutManager(new LinearLayoutManager(getActivity()));
        btSearchTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchTeacherID();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).hideFloatingActionButton();//沒辦法消失，暫時找不到辦法
    }

    private void SearchTeacherID() {
        String TeacherID = edSearchTeacher.getText().toString();
        showResult(TeacherID);

    }

    private void showResult(String teacherID) {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/TeachersListServerlet";
            List<Teachers> teachers = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findById");
            jsonObject.addProperty("Teacher_Account", teacherID);
            String jsonOut = jsonObject.toString();
            SearchTask = new MyTask(url, jsonOut);
            try {
                String result = SearchTask.execute().get();
                Log.d(TAG, result);
                Type listType = new TypeToken<List<Teachers>>() {
                }.getType();
                teachers = new Gson().fromJson(result, listType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (teachers == null || teachers.isEmpty()) {
                teachers = new ArrayList<>();
                rlTeachers.setAdapter(new rlSearchTeacher(getActivity(), teachers));
                Toast.makeText(getContext(), R.string.IDNotFount, Toast.LENGTH_SHORT).show();
            } else {
                rlTeachers.setAdapter(new rlSearchTeacher(getActivity(), teachers));
            }

        } else {
            Toast.makeText(getContext(), R.string.msg_NoNetwork, Toast.LENGTH_SHORT).show();
        }
    }

    private class rlSearchTeacher extends RecyclerView.Adapter<rlSearchTeacher.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Teachers> teachers;
        private int imageSize;
        private int classId = 0;

        public rlSearchTeacher(Context context, List<Teachers> teachers) {
            layoutInflater = LayoutInflater.from(context);
            this.teachers = teachers;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView teacherImageview;
            TextView teacherName, teacherPhone;

            public MyViewHolder(View itemView) {
                super(itemView);
                teacherImageview = itemView.findViewById(R.id.teacherImageview);
                teacherName = itemView.findViewById(R.id.teacherName);
                teacherPhone = itemView.findViewById(R.id.teacherPhone);

            }
        }

        @Override
        public int getItemCount() {
            return teachers.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.teacher_tab2_teachers_contact, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Teachers teacher = teachers.get(position);
            String url = Common.URL + "/TeachersListServerlet";
            final int id = teacher.getId();
            teacherGetImageTask = new TeacherGetImageTask(url, id, imageSize, myViewHolder.teacherImageview);
            teacherGetImageTask.execute();
            myViewHolder.teacherName.setText(teacher.getTeacher_Account());
            myViewHolder.teacherPhone.setText(teacher.getTeacher_Phone());

            classId = getClassId();

            SharedPreferences preferences = getActivity().getSharedPreferences
                    (com.example.yangwensing.myapplication.main.Common.PREF_FILE, Context.MODE_PRIVATE);
            preferences.edit().putInt("id", id);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setTitle(R.string.AddNewTeacher)
                            .setMessage(R.string.doYouReallyWantToAddThisTeacher)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Common.networkConnected(getActivity())) {
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "SubjectTeacherInsert");
                                        jsonObject.addProperty("Class_Name", classId);
                                        jsonObject.addProperty("Class_SubjectTeacher", id);
                                        try {
                                            InsertTask = new MyTask(Common.URL + "/TeachersListServerlet", jsonObject.toString());
                                            int count = Integer.valueOf(InsertTask.execute().get());
                                            if (count == 0) {
                                                Toast.makeText(getActivity(), "SignUp Success", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception e) {
                                            Log.e(TAG, "error message" + toString());
                                        }

                                    } else {
                                        Toast.makeText(getActivity(), "NetWork connection fail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (SearchTask != null) {
            SearchTask.cancel(true);
        }
        ((MainActivity) getActivity()).showFloatingActionButton();
    }

    private int getClassId() {
        SharedPreferences preferences = getActivity().getSharedPreferences(com.example.yangwensing.myapplication.main.Common.PREF_FILE, Context.MODE_PRIVATE);
        int id = preferences.getInt("classId", 0);
        return id;
    }


}
