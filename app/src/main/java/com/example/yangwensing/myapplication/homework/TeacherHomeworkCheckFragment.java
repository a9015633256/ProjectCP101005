package com.example.yangwensing.myapplication.homework;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TeacherHomeworkCheckFragment extends Fragment {
    private static final String TAG = "TeacherHomeCFragment";
    private RecyclerView recyclerView;
    private Button btCheck;

    //裝偏好設定檔儲存的資料
    private int classId;
    private String className;
    private int teacherId;
    private int subjectId;

    private static Homework homework;
    private List<HomeworkCheck> homeworkCheckList = new ArrayList<>();
    private List<HomeworkCheck> newHomeworkCheckList = new ArrayList<>();
    private TabLayout tabLayout;
    private TabLayout.OnTabSelectedListener onTabSelectedListener;
    private BottomNavigationView bottomNavigationView;
    private MyTask getHomeworkTask;
    private MyTask updateHomeworkTask;

    private PieChart pieChart;
    private int doneCount = 0;
    private int unDoneCount = 0;
    private LinearLayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_homework_check, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true



        findViews(view);

        setHasOptionsMenu(true);

        //取得上一頁資料
        final Bundle bundle = getArguments();
        if (bundle != null) {
            homework = (Homework) bundle.getSerializable("homework");
        }

        if (homework == null) {
            Common.showToast(getActivity(), R.string.msg_data_error);
        }


        getDataFromDB();

        setupChart();

        //tabLayout顯示
        tabLayout = getActivity().findViewById(R.id.tlForTeacherHomework);
        tabLayout.setVisibility(View.VISIBLE);
        onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        TeacherHomeworkUpdateDeleteFragment teacherHomeworkUpdateDeleteFragment = new TeacherHomeworkUpdateDeleteFragment();
                        teacherHomeworkUpdateDeleteFragment.setArguments(bundle);
                        if (getFragmentManager() != null) {
                            getFragmentManager().beginTransaction().replace(R.id.main_content, teacherHomeworkUpdateDeleteFragment, "TeacherHomeworkUpdateDeleteFragment").commit();
                        }
                        break;
                    case 1:
                        break;
                    default:
                        break;


                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };

        tabLayout.addOnTabSelectedListener(onTabSelectedListener);


        //rv
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        HomeworkAdapter homeworkAdapter = new HomeworkAdapter(homeworkCheckList, getActivity());
        recyclerView.setAdapter(homeworkAdapter);

        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateHomeworkChecked();

            }
        });


        return view; //要改成回傳view
    }

    private void setupChart() {

        //configure chart
//        pieChart.setMaxAngle(180);
//        pieChart.setRotationAngle(180);
        pieChart.getDescription().setEnabled(false);
//        pieChart.setUsePercentValues(true);
        pieChart.animateX(1000);
//        pieChart.setCenterTextRadiusPercent(20);
        pieChart.animateY(1000);
//        pieChart.animateX(1000);

//
//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        int height = displayMetrics.heightPixels;
//        display.getMetrics(displayMetrics);
//
//        int height2 = pieChart.getHeight();
//
//        int offset = (int)(height * 2); /* percent to move */
//
//        RelativeLayout.LayoutParams rlParams =
//                (RelativeLayout.LayoutParams)pieChart.getLayoutParams();
//        rlParams.setMargins(0, 0, 0, -offset);
//        pieChart.setLayoutParams(rlParams);

        /* 取得各品牌車單月銷售量資料 */
        List<PieEntry> pieEntries = getDoneEntries();

        //用建構式放入資料、用方法設定UI
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "繳交情況");
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(20);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(20);

        /* 使用官訂顏色範本，顏色不能超過5種，否則官定範本要加顏色 */
//        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS); 預設顏色
        pieDataSet.setColors(ContextCompat.getColor(getActivity(),R.color.done_green),ContextCompat.getColor(getActivity(),R.color.undone_red));
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private List<PieEntry> getDoneEntries() {
        List<PieEntry> doneEntries = new ArrayList<>();
        doneEntries.add(new PieEntry(doneCount, "已繳交"));
        doneEntries.add(new PieEntry(unDoneCount, "未繳交"));
        return doneEntries;
    }

    private void updateHomeworkChecked() {

        if (Common.networkConnected(getActivity())) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "updateHomeworkCheck");
            jsonObject.addProperty("homeworkId", homework.getId());
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            String jsonStrForUpdateCheck = gson.toJson(newHomeworkCheckList);
            jsonObject.addProperty("homeworkCheckList", jsonStrForUpdateCheck);
            updateHomeworkTask = new MyTask(Common.URLForMingTa + "/HomeworkServlet", jsonObject.toString());

            try {

                String jsonIn = updateHomeworkTask.execute().get();
                int count = Integer.valueOf(jsonIn);

                if (count == 0) {

                    Common.showToast(getActivity(), R.string.msg_UpdateFail);
                } else {

                    Common.showToast(getActivity(), R.string.msg_UpdateSuccess);
                    getFragmentManager().popBackStack();
//                    getDataFromDB();
//                    setupChart();
//                    layoutManager.scrollToPositionWithOffset(0, 0);
//                    recyclerView.getLayoutManager().scrollToPosition(0);
//                    recyclerView.setNestedScrollingEnabled(false);

//                    recyclerView.smoothScrollToPosition(20);
//                    recyclerView.scrollBy(50,100);
//                    recyclerView.smoothScrollToPosition(0);
//                    new LinearLayoutManager.scrollToPositionWithOffset(0, 0);
//


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
    public void onStart() {
        super.onStart();

        //隱藏底部導覽列
        bottomNavigationView.setVisibility(View.GONE);

    }

    @Override
    public void onStop() {
        //重新顯示底部導覽列
        bottomNavigationView.setVisibility(View.VISIBLE);
        if (getHomeworkTask != null) {
            getHomeworkTask.cancel(true);

        }
        if (updateHomeworkTask != null) {
            updateHomeworkTask.cancel(true);

        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tabLayout.setVisibility(View.GONE);
        homeworkCheckList.clear();
    }

    private void getDataFromPref() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        teacherId = preferences.getInt("teacherId", 0);
        subjectId = preferences.getInt("subjectId", 0);
        classId = preferences.getInt("classId", 0);
        className = preferences.getString("className", "");


    }


    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.rvCheckList);
        btCheck = view.findViewById(R.id.btCheckHomework);
        pieChart = view.findViewById(R.id.pieChart);
        bottomNavigationView = getActivity().findViewById(R.id.btNavigation_Bar);


    }

    private void getDataFromDB() {


        if (Common.networkConnected(getActivity())) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findHomeworkCheckByHomeworkId");
            jsonObject.addProperty("homeworkId", homework.getId());
            getHomeworkTask = new MyTask(Common.URLForMingTa + "/HomeworkServlet", jsonObject.toString());

            try {

                String jsonIn = getHomeworkTask.execute().get();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type listType = new TypeToken<List<HomeworkCheck>>() {
                }.getType();

                homeworkCheckList = gson.fromJson(jsonIn, listType);

                //sort data for chart
                doneCount = 0;
                unDoneCount = 0;
                for (HomeworkCheck homework : homeworkCheckList){
                    if (homework.isHomeworkDone()){
                        doneCount++;
                    } else {
                        unDoneCount++;
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

    //rv Adapter
    private class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder> {
        private List<HomeworkCheck> homeworkCheckList;
        private Context context;

        HomeworkAdapter(List<HomeworkCheck> homeworkCheckList, Context context) {
            this.homeworkCheckList = homeworkCheckList;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return homeworkCheckList.size();
        }


        @NonNull
        @Override
        public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.cardview_homework_check, parent, false);

            return new TeacherHomeworkCheckFragment.HomeworkAdapter.HomeworkViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(@NonNull final HomeworkViewHolder homeworkViewHolder, final int position) {
            final HomeworkCheck homeworkCheck = homeworkCheckList.get(position);
            homeworkViewHolder.tvStudentNumber.setText(homeworkCheck.getStudentNumber());
            homeworkViewHolder.tvStudentName.setText(homeworkCheck.getStudentName());
            homeworkViewHolder.checkBox.setChecked(!homeworkCheck.isHomeworkDone());
            homeworkViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        homeworkCheck.setHomeworkDone(true);
                        newHomeworkCheckList.add(homeworkCheck);
                    } else {
                        homeworkCheck.setHomeworkDone(false);
                        newHomeworkCheckList.add(homeworkCheck);

                    }


                }
            });


        }


        class HomeworkViewHolder extends RecyclerView.ViewHolder {
            private TextView tvStudentNumber, tvStudentName;
            private CheckBox checkBox;


            HomeworkViewHolder(View itemView) {
                super(itemView);
                this.tvStudentNumber = itemView.findViewById(R.id.tvStudentNumber);
                this.tvStudentName = itemView.findViewById(R.id.tvStudentName);
                this.checkBox = itemView.findViewById(R.id.cbIsCompleted);

            }
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_options_delete_homework, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_deleteHomework:
                if (getFragmentManager() != null) {
                    new TeacherHomeworkUpdateDeleteFragment.AlertDialogFragment().show(getFragmentManager(), "delete");
                }

                break;
            default:
                break;


        }


        return super.onOptionsItemSelected(item);
    }



}