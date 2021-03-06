package TeacherMainActivityView.teacher_main_activity.Tab2Teacher;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import TeacherMainActivityView.CommonPart.Common;
import TeacherMainActivityView.CommonPart.MyTask;
import TeacherMainActivityView.teacher_main_activity.Tab1Student.tap1fragment;


//Tab導師資料
public class tap2fragment extends Fragment {
    private static final String TAG = "tap2fragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvTeachers;
    private String c;
    private TeacherGetImageTask teacherGetImageTask;
    private MyTask ClassSubjectTeacherTask, TeacherDeleteTask;
    private String title = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_tap2_contact_fragment, container, false);
        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//下拉更新
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showClassSubjectTeacher();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        rvTeachers = view.findViewById(R.id.contact_recyclerview2);
        rvTeachers.setLayoutManager(new LinearLayoutManager(getActivity()));

        getClassName();

        FloatingActionButton btAdd = view.findViewById(R.id.btAdd2);//浮動按鈕
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Tab2_AddNewTeachers();
                switchFragment(fragment);

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showClassSubjectTeacher();
    }

    private void showClassSubjectTeacher() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/TeachersListServerlet";
            List<ClassSubjectTeacher> classSubjectTeachers = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findTeachers");
            jsonObject.addProperty("Class_Name", c);
            String jsonOut = jsonObject.toString();
            ClassSubjectTeacherTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = ClassSubjectTeacherTask.execute().get();
                Log.d(TAG, jsonIn);
                Type type = new TypeToken<List<ClassSubjectTeacher>>() {
                }.getType();
                classSubjectTeachers = new Gson().fromJson(jsonIn, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (classSubjectTeachers == null || classSubjectTeachers.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoSpotsFound);
            } else {
                rvTeachers.setAdapter(new ClassSubjectTeacherAdapter(getActivity(), classSubjectTeachers));
            }

        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }

    private void switchFragment(Fragment fragment) {

        //修改為切換母fragment->這樣addToBackStack才有作用
        FragmentTransaction fragmentTransaction = getParentFragment().getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void getClassName() {
        SharedPreferences preferences = getActivity().getSharedPreferences(com.example.yangwensing.myapplication.main.Common.PREF_FILE, Context.MODE_PRIVATE);
        c = preferences.getString("c", "");
    }

    private class ClassSubjectTeacherAdapter extends RecyclerView.Adapter<ClassSubjectTeacherAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<ClassSubjectTeacher> classSubjectTeachers;
        private int imageSize;

        public ClassSubjectTeacherAdapter(Context context, List<ClassSubjectTeacher> classSubjectTeachers) {
            layoutInflater = LayoutInflater.from(context);
            this.classSubjectTeachers = classSubjectTeachers;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;//設定圖片大小
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView teacherImageView;
            TextView teacherName, teacherPhone;

            public MyViewHolder(View itemView) {
                super(itemView);
                teacherImageView = itemView.findViewById(R.id.teacherImageview);
                teacherName = itemView.findViewById(R.id.teacherName);
                teacherPhone = itemView.findViewById(R.id.teacherPhone);
            }
        }

        @Override
        public int getItemCount() {
            return classSubjectTeachers.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.teacher_tab2_teachers_contact, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final ClassSubjectTeacher classSubjectTeacher = classSubjectTeachers.get(position);
            String url = Common.URL + "/TeachersListServerlet";
            final int id = classSubjectTeacher.getId();
            final String teacher_Account = classSubjectTeacher.getTeacher_Account();
            final String teacher_Email = classSubjectTeacher.getTeacher_Email();
            final String teacher_Phone = classSubjectTeacher.getTeacher_Phone();
            final int teacher_Gender = classSubjectTeacher.getTeacher_Gender();
            final String teacher_TakeOfficeDate = classSubjectTeacher.getTeacher_TakeOfficeDate();
            teacherGetImageTask = new TeacherGetImageTask(url, id, imageSize, holder.teacherImageView);
            teacherGetImageTask.execute();//取得圖片
            holder.teacherName.setText(classSubjectTeacher.getTeacher_Account());
            holder.teacherPhone.setText(classSubjectTeacher.getTeacher_Phone());
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), v, Gravity.END);//popoutmenu
                    popupMenu.inflate(R.menu.teacher_popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete://長按刪除
                                    if (Common.networkConnected(getActivity())) {
                                        String url = Common.URL + "/TeachersListServerlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "delete");
                                        jsonObject.addProperty("teachers", new Gson().toJson(classSubjectTeacher));
                                        int count = 0;
                                        try {
                                            TeacherDeleteTask = new MyTask(url, jsonObject.toString());
                                            String result = TeacherDeleteTask.execute().get();
                                            count = Integer.valueOf(result);
                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (count == 0) {
                                            Common.showToast(getActivity(), R.string.msg_DeleteFail);
                                        } else {
                                            classSubjectTeachers.remove(classSubjectTeacher);
                                            ClassSubjectTeacherAdapter.this.notifyDataSetChanged();
                                            Common.showToast(getActivity(), R.string.msg_DeleteSuccess);
                                        }
                                    } else {
                                        Common.showToast(getActivity(), R.string.msg_NoNetwork);
                                    }
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = getActivity().getSharedPreferences(com.example.yangwensing.myapplication.main.Common.PREF_FILE, Context.MODE_PRIVATE);
                    preferences.edit()
                            .putInt("id", id)
                            .putString("teacher_Account", teacher_Account)
                            .putString("teacher_Email", teacher_Email)
                            .putString("teacher_Phone", teacher_Phone)
                            .putInt("teacher_Gender", teacher_Gender)
                            .putString("teacher_TakeOfficeDate", teacher_TakeOfficeDate)
                            .apply();

                    Fragment fragment2 = new Tab2TeacherProfile();
                    switchFragment(fragment2);
                }
            });

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (teacherGetImageTask != null) {
            teacherGetImageTask.cancel(true);
        }
        if (ClassSubjectTeacherTask != null) {
            ClassSubjectTeacherTask.cancel(true);
        }
        Log.d(TAG, title + ": onStop");


    }

}
