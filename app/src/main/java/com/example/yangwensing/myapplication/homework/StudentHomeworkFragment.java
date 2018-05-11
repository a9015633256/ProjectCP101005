package com.example.yangwensing.myapplication.homework;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.chat.ChatList;
import com.example.yangwensing.myapplication.chat.MotherList;
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

public class StudentHomeworkFragment extends Fragment {
    private static final String TAG = "StudentHomeworkFragment";
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private List<AssignDate> hashSet = new ArrayList<>(); //回傳資料按日期整理用
    Button btttest;


    private int studentId;
    String ccc;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_homework, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true

        getActivity().setTitle(R.string.title_homework);



        btttest = view.findViewById(R.id.btttest);
        btttest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment motherList = new MotherList();



                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.content,motherList);
                fragmentTransaction.commit();
            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        ccc = preferences.getString("","user");


        findViews(view);

        //取得偏好設定存的學生id
        getDataFromPref();


        //取得db資料

        getDataFromDB();


        //rv
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        HomeworkAdapter homeworkAdapter = new HomeworkAdapter(hashSet, getActivity());
        recyclerView.setAdapter(homeworkAdapter);

        return view; //要改成回傳view
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView.setVisibility(View.VISIBLE); //放在onResume才不會比畫面還快顯示出來

    }


    private void getDataFromDB() {


        if (Common.networkConnected(getActivity())) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findHomeworkIsDoneByStudentId");
            jsonObject.addProperty("studentId", studentId);
            MyTask getHomeworkTask = new MyTask(Common.URLForMingTa + "/HomeworkServlet", jsonObject.toString());

            try {

                String jsonIn = getHomeworkTask.execute().get();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type listType = new TypeToken<List<HomeworkIsDone>>() {
                }.getType();

                List<HomeworkIsDone> homeworkIsDoneList; //放db回傳資料

                homeworkIsDoneList = gson.fromJson(jsonIn, listType);

                if (homeworkIsDoneList != null) {
                    //回傳資料按日期整理
                    for (HomeworkIsDone homeworkIsDone : homeworkIsDoneList) {

                        //日期取出、換民國
                        Date date = homeworkIsDone.getDate();
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
                            assignDate.add(homeworkIsDone);
                        } else {
                            hashSet.get(index).add(homeworkIsDone);

                        }

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

    private void getDataFromPref() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        studentId = preferences.getInt("studentId", 0);


    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.rvHomework);
        bottomNavigationView = getActivity().findViewById(R.id.bnForStudent);

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

            return new StudentHomeworkFragment.HomeworkAdapter.HomeworkViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final HomeworkViewHolder homeworkViewHolder, final int position) {
            final AssignDate assignDate = assignDateList.get(position);
            homeworkViewHolder.textView.setText(assignDate.getFormattedDate());

            //將個別數據帶入子視窗
            for (int i = 0; i < assignDate.size(); i++) {
                final HomeworkIsDone homeworkIsDone = (HomeworkIsDone) assignDate.get(i);
                TextView tvSubject;
                TextView tvTitle;
                TextView tvIsCompleted;
                tvSubject = homeworkViewHolder.linearLayout.getChildAt(i).findViewById(R.id.tvSubject);
                tvSubject.setText(homeworkIsDone.getSubject());

                tvTitle = homeworkViewHolder.linearLayout.getChildAt(i).findViewById(R.id.tvTitle);
                tvTitle.setText(homeworkIsDone.getTitle());

                tvIsCompleted = homeworkViewHolder.linearLayout.getChildAt(i).findViewById(R.id.tvIsCompleted);
                if (homeworkIsDone.isHomeworkDone()) {
                    tvIsCompleted.setText("未完成");
                }

                homeworkViewHolder.linearLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("homework", homeworkIsDone);
                        StudentHomeworkDetailFragment studentHomeworkDetailFragment = new StudentHomeworkDetailFragment();
                        studentHomeworkDetailFragment.setArguments(bundle);

                        getFragmentManager().beginTransaction().replace(R.id.content, studentHomeworkDetailFragment).addToBackStack(null).commit();


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

    //按返回鍵時也會清空資料
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hashSet.clear();
    }
}
