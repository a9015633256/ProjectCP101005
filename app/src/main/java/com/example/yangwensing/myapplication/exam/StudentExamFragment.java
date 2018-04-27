package com.example.yangwensing.myapplication.exam;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.homework.AssignDate;
import com.example.yangwensing.myapplication.homework.Homework;

import java.util.ArrayList;
import java.util.List;

public class StudentExamFragment extends Fragment {
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_exam, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true

        getActivity().setTitle(R.string.title_exam);

        findViews(view);

        //取得db資料


        List<AssignDate> assignDateList = getAssignDateList();


        //rv
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        HomeworkAdapter homeworkAdapter = new HomeworkAdapter(assignDateList, getActivity());
        recyclerView.setAdapter(homeworkAdapter);
        homeworkAdapter.setOnScrollListener(new OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                recyclerView.scrollToPosition(pos);
            }
        });

        return view; //要改成回傳view
    }

    //rv假資料
    private List<AssignDate> getAssignDateList() {
        List<AssignDate> assignDateList = new ArrayList<>();

        Homework homework = new Homework("物理", false);
        Homework homework2 = new Homework("數學", false);
        Homework homework3 = new Homework("國文", true);
        Homework homework4 = new Homework("歷史", false);
        Homework homework5 = new Homework("作文", true);

        List<Homework> homeworkList = new ArrayList<>();
        homeworkList.add(homework);
        homeworkList.add(homework2);
        homeworkList.add(homework3);
        homeworkList.add(homework4);
        homeworkList.add(homework5);

        List<Homework> homeworkList2 = new ArrayList<>();
        homeworkList2.add(homework);

        List<Homework> homeworkList3 = new ArrayList<>();
        homeworkList3.add(homework);
        homeworkList3.add(homework2);
        homeworkList3.add(homework5);


        assignDateList.add(new AssignDate(107, 4, 10, homeworkList));
        assignDateList.add(new AssignDate(107, 4, 6, homeworkList2));
        assignDateList.add(new AssignDate(107, 4, 3, homeworkList3));

        return assignDateList;
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.rvExam);

    }


    //rv Adapter
    private class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder> {
        private List<AssignDate> assignDateList;
        private Context context;
        OnScrollListener mOnScrollListener;

        HomeworkAdapter(List<AssignDate> assignDateList, Context context) {
            this.assignDateList = assignDateList;
            this.context = context;
        }

        @NonNull
        @Override
        public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.cardview_homework, parent, false);

            return new StudentExamFragment.HomeworkAdapter.HomeworkViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final HomeworkViewHolder homeworkViewHolder, int position) {
            final AssignDate assignDate = assignDateList.get(position);
            homeworkViewHolder.textView.setText(assignDate.showDate());

            //將個別數據帶入子視窗
            for (int i = 0; i < assignDate.getHomeworkList().size(); i++) {
                Homework homework = assignDate.getHomeworkList().get(i);
                TextView tvSubject;
                TextView tvIsCompleted;
                tvSubject = homeworkViewHolder.linearLayout.getChildAt(i).findViewById(R.id.tvSubject);
                tvSubject.setText(homework.getSubject());

                tvIsCompleted = homeworkViewHolder.linearLayout.getChildAt(i).findViewById(R.id.tvIsCompleted);
                if (!homework.isCompleted()) {
                    tvIsCompleted.setText("未完成");
                }
            }

            //隱藏多餘子視窗數量
            for (int i = assignDate.getHomeworkList().size(); i < getMaxNumberOfHomework(); i++) {
                homeworkViewHolder.linearLayout.getChildAt(i).setVisibility(View.GONE);

            }

            //設定按下就會展開\收起
            homeworkViewHolder.itemView.findViewById(R.id.cvAssignDate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeworkViewHolder.linearLayout.getVisibility() == View.GONE) {
                        homeworkViewHolder.linearLayout.setVisibility(View.VISIBLE);
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

                if (maxNumberOfHomework < assignDateList.get(i).getHomeworkList().size()) {
                    maxNumberOfHomework = assignDateList.get(i).getHomeworkList().size();
                }

            }

            return maxNumberOfHomework;
        }


        //滾動監聽
        public void setOnScrollListener(OnScrollListener onScrollListener) {
            this.mOnScrollListener = onScrollListener;
        }


    }

    //滾動監聽
    public interface OnScrollListener {
        void scrollTo(int pos);
    }


}
