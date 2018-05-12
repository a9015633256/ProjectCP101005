package com.example.yangwensing.myapplication.homework;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TeacherHomeworkFragment extends Fragment {
    private static final String TAG = "TeacherHomeworkFragment";
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddHomework;

    private List<AssignDate> hashSet = new ArrayList<>(); //回傳資料按日期整理用

    //裝偏好設定檔儲存的資料
    private int classId;
    private String className;
    private int teacherId;
    private int subjectId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_homework, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true

        //假如頁面重疊、消滅頁面
        if (getFragmentManager() != null) {
            Fragment fragment = getFragmentManager().findFragmentByTag("TeacherHomeworkCheckFragment");
            if (fragment != null) {
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }

            Fragment fragment2 = getFragmentManager().findFragmentByTag("TeacherHomeworkUpdateDeleteFragment");
            if (fragment2 != null) {
                getFragmentManager().beginTransaction().remove(fragment2).commit();

            }
        }

        getDataFromPref();

        getActivity().setTitle(className + "  Homework Overview");

        findViews(view);

        //取得db資料
        getDataFromDB();


        //rv
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        HomeworkAdapter homeworkAdapter = new HomeworkAdapter(hashSet, getActivity());
        recyclerView.setAdapter(homeworkAdapter);

        //增加作業
        fabAddHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.main_content, new TeacherHomeworkAddFragment()).addToBackStack(null).commit();
            }
        });


        return view; //要改成回傳view
    }

    private void getDataFromDB() {

        if (Common.networkConnected(getActivity())) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findHomeworkByClassIdAndTeacherId");
            jsonObject.addProperty("classId", classId);
            jsonObject.addProperty("teacherId", teacherId);
            MyTask getHomeworkTask = new MyTask(Common.URLForMingTa + "/HomeworkServlet", jsonObject.toString());

            try {

                String jsonIn = getHomeworkTask.execute().get();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type listType = new TypeToken<List<HomeworkIsDone>>() {
                }.getType();

                List<Homework> homeworkList = gson.fromJson(jsonIn, listType);

                //回傳資料按日期整理
                for (Homework homework : homeworkList) {

                    //日期取出、換民國
                    Date date = homework.getDate();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.roll(Calendar.YEAR, -1911);
                    Date dateTaiwan = calendar.getTime();

                    //日期整理格式
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy年 M月 dd日", Locale.TAIWAN);
                    String formattedDate = simpleDateFormat.format(dateTaiwan);

                    //對比是否有此作業日期->有則把作業加入已存在日期、無則新創AssignDate物件並放入
                    int index = hashSet.indexOf(new AssignDate(formattedDate));
                    if (index == -1) {
                        AssignDate assignDate = new AssignDate(formattedDate);
                        hashSet.add(assignDate);
                        assignDate.add(homework);
                    } else {
                        hashSet.get(index).add(homework);

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                Common.showToast(getActivity(), R.string.text_no_server);

            }


        } else {
            Common.showToast(getActivity(), R.string.text_no_network);

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hashSet.clear();
    }

    private void getDataFromPref() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        teacherId = preferences.getInt("teacherId", 0);
        subjectId = preferences.getInt("subjectId", 0);
        classId = preferences.getInt("classId", 0);
        className = preferences.getString("className", "");


    }


    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.rvHomework);
        fabAddHomework = view.findViewById(R.id.fabAddHomework);


    }


    //rv Adapter
    private class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder> {
        private List<AssignDate> assignDateList;
        private Context context;

        HomeworkAdapter(List<AssignDate> assignDateList, Context context) {
            this.assignDateList = assignDateList;
            this.context = context;
        }

        @NonNull
        @Override
        public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.cardview_homework, parent, false);

            return new TeacherHomeworkFragment.HomeworkAdapter.HomeworkViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final HomeworkViewHolder homeworkViewHolder, final int position) {
            final AssignDate assignDate = assignDateList.get(position);
            homeworkViewHolder.textView.setText(assignDate.getFormattedDate());

            //將個別數據帶入子視窗
            for (int i = 0; i < assignDate.size(); i++) {
                final Homework homework = (Homework) assignDate.get(i);
                TextView tvSubject;
                TextView tvTitle;
                TextView tvIsCompleted;
                tvSubject = homeworkViewHolder.linearLayout.getChildAt(i).findViewById(R.id.tvSubject);
                tvSubject.setText(homework.getSubject());

                tvTitle = homeworkViewHolder.linearLayout.getChildAt(i).findViewById(R.id.tvTitle);
                tvTitle.setText(homework.getTitle());

                tvIsCompleted = homeworkViewHolder.linearLayout.getChildAt(i).findViewById(R.id.tvIsCompleted);
                tvIsCompleted.setVisibility(View.INVISIBLE);

                homeworkViewHolder.linearLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("homework", homework);
                        TeacherHomeworkUpdateDeleteFragment teacherHomeworkUpdateDeleteFragment = new TeacherHomeworkUpdateDeleteFragment();
                        teacherHomeworkUpdateDeleteFragment.setArguments(bundle);

                        if (getFragmentManager() != null) {
                            getFragmentManager().beginTransaction().replace(R.id.main_content, teacherHomeworkUpdateDeleteFragment,"TeacherHomeworkUpdateDeleteFragment").addToBackStack(null).commit();
                        }


                    }
                });

            }

            //隱藏多餘子視窗數量
            for (int i = assignDate.size(); i < getMaxNumberOfHomework(); i++) {
                homeworkViewHolder.linearLayout.getChildAt(i).setVisibility(View.GONE);

            }

            //設定按下就會展開\收起
            homeworkViewHolder.itemView.findViewById(R.id.cvAssignDate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeworkViewHolder.linearLayout.getVisibility() == View.GONE) {
                        homeworkViewHolder.linearLayout.setVisibility(View.VISIBLE);

                        //滑動到該母視窗加上子視窗底的位置
                        recyclerView.smoothScrollToPosition(position + assignDate.size());

                    } else {
                        homeworkViewHolder.linearLayout.setVisibility(View.GONE);
                    }

                }
            });


        }

        @Override
        public int getItemCount() {
            return assignDateList.size();
        }

        class HomeworkViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;
            private LinearLayout linearLayout;


            HomeworkViewHolder(View itemView) {
                super(itemView);
                this.textView = itemView.findViewById(R.id.tvDate);
                this.linearLayout = itemView.findViewById(R.id.llhomeworkDetail); //連接放childView的linearLayout

                LayoutInflater layoutInflater = LayoutInflater.from(context);

                //依最大作業數製造childView，並預設看不到
                int maxNumberOfHomework = getMaxNumberOfHomework();
                for (int i = 0; i < maxNumberOfHomework; i++) {

                    View childView = layoutInflater.inflate(R.layout.cardview_homework_detail, linearLayout, true);
                    childView.setId(i);
                    linearLayout.setVisibility(View.GONE);
                }

            }
        }

        //取得作業最多的那個日期-> 取得最大作業數
        int getMaxNumberOfHomework() {
            int maxNumberOfHomework = 0;

            for (int i = 0; i < assignDateList.size(); i++) {

                if (maxNumberOfHomework < assignDateList.get(i).size()) {
                    maxNumberOfHomework = assignDateList.get(i).size();
                }

            }

            return maxNumberOfHomework;
        }


    }


}
