package TeacherMainActivityView.teacher_main_activity.Tab1Student;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.yangwensing.myapplication.info.StudentInfoFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import TeacherMainActivityView.CommonPart.Common;
import TeacherMainActivityView.CommonPart.MyTask;
import TeacherMainActivityView.teacher_main_activity.Tab2Teacher.ClassSubjectTeacher;

//Tab學生資料
public class tap1fragment extends Fragment {
    private static final String TAG = "tap1fragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyTask studentsGetAllTask;
    private MyTask studentsDeleteTask;
    private RecyclerView rvStudents;
    private StudentGetImageTask studentGetImageTask;
    private FloatingActionButton btAdd;
    private String c;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_tap1_contact_fragment, container, false);
        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//下拉更新
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showAllStudents();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        rvStudents = view.findViewById(R.id.contact_recyclerview);
        rvStudents.setLayoutManager(new LinearLayoutManager(getActivity()));

        getClassName();

        btAdd = view.findViewById(R.id.btAdd);//浮動按鈕
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Tab1_StudentCreatAccount();
                switchFragment(fragment);

            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        showAllStudents();
    }

    private void showAllStudents() {
        if (Common.networkConnected(getActivity())) {//連結網路
            String url = Common.URL + "/StudentServlet";
            List<ClassStudentMember> classStudentMembers = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findStudents");//利用jason物件取得所有資料
            jsonObject.addProperty("Class_Name", c);
            String jsonOut = jsonObject.toString();//jasonout.輸出
            studentsGetAllTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = studentsGetAllTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<ClassStudentMember>>() {
                }.getType();
                classStudentMembers = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (classStudentMembers == null || classStudentMembers.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoSpotsFound);
            } else {
                rvStudents.setAdapter(new StudentRecyclerViewAdapter(getActivity(), classStudentMembers));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }

    private void getClassName() {
        SharedPreferences preferences = getActivity().getSharedPreferences(com.example.yangwensing.myapplication.main.Common.PREF_FILE, Context.MODE_PRIVATE);
        c = preferences.getString("c", "");
    }

    private class StudentRecyclerViewAdapter extends RecyclerView.Adapter<StudentRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<ClassStudentMember> classStudentMembers;
        private int imageSize;

        StudentRecyclerViewAdapter(Context context, List<ClassStudentMember> classStudentMembers) {
            layoutInflater = LayoutInflater.from(context);
            this.classStudentMembers = classStudentMembers;
            /* 螢幕寬度除以4當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;//設定圖片大小
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView studentImageview;
            TextView studentName, studentPhone;

            MyViewHolder(View itemView) {
                super(itemView);
                studentImageview = itemView.findViewById(R.id.studentImageview);
                studentName = itemView.findViewById(R.id.studentName);
                studentPhone = itemView.findViewById(R.id.studentPhone);
            }
        }

        @Override
        public int getItemCount() {
            return classStudentMembers.size();
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.teacher_tab1_students_contact, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
            final ClassStudentMember classStudentMember = classStudentMembers.get(position);
            String url = Common.URL + "/StudentServlet";
            final int id = classStudentMember.getId();
            studentGetImageTask = new StudentGetImageTask(url, id, imageSize, myViewHolder.studentImageview);
            studentGetImageTask.execute();
            myViewHolder.studentName.setText(classStudentMember.getStudent_Name());
            myViewHolder.studentPhone.setText(classStudentMember.getStudent_Phone());


            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {//長按刪除，更新的部分
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.END);//popoutmenu
                    popupMenu.inflate(R.menu.teacher_popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete://長按刪除
                                    if (Common.networkConnected(getActivity())) {
                                        String url = Common.URL + "/StudentServlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "studentsDelete");
                                        jsonObject.addProperty("students", new Gson().toJson(classStudentMember));
                                        int count = 0;
                                        try {
                                            studentsDeleteTask = new MyTask(url, jsonObject.toString());
                                            String result = studentsDeleteTask.execute().get();
                                            count = Integer.valueOf(result);
                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (count == 0) {
                                            Common.showToast(getActivity(), R.string.msg_DeleteFail);
                                        } else {
                                            classStudentMembers.remove(classStudentMember);
                                            StudentRecyclerViewAdapter.this.notifyDataSetChanged();
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
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    int studentId = classStudentMember.getId();

                    Bundle bundle = new Bundle();
                    bundle.putInt("studentIdForTeacher", id);
                    Log.d(TAG, "onClick: "+id);

                    StudentInfoFragment studentInfoFragment=  new StudentInfoFragment();
                    studentInfoFragment.setArguments(bundle);

                    if (getFragmentManager() != null) {
                        getFragmentManager().beginTransaction().replace(R.id.main_content, studentInfoFragment).addToBackStack(null).commit();
                    }


                }
            });

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (studentsGetAllTask != null) {
            studentsGetAllTask.cancel(true);
        }
        if (studentGetImageTask != null) {
            studentGetImageTask.cancel(true);
        }

        if (studentsDeleteTask != null) {
            studentsDeleteTask.cancel(true);
        }

    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
