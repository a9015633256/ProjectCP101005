package com.example.yangwensing.myapplication.ExamSubject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.MyTask;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.components.Description;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//import com.github.mikephil.charting.highlight.Highlight;
//import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
//import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class PerformanceAnalysisChart extends Fragment {
    private static final String TAG = "BarChartActivity";
    private MyTask myTask;
    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subject_performance_analysis_chart, container, false);
        pieChart = view.findViewById(R.id.pieChart);
        List<Exam> exams = new ArrayList<>();
        /* 設定可否旋轉 */
        pieChart.setRotationEnabled(true);

        /* 設定圓心文字 */
        pieChart.setCenterText("August");
        /* 設定圓心文字大小 */
        pieChart.setCenterTextSize(25);

        Description description = new Description();
        description.setText("AnalysisChart");
        description.setTextSize(25);
        pieChart.setDescription(description);


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                Log.d(TAG, "entry: " + entry.toString() + "; highlight: " + highlight.toString());
                PieEntry pieEntry = (PieEntry) entry;
                String text = pieEntry.getLabel() + "\n" + pieEntry.getValue();
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

//        if (Common.networkConnected(getActivity())) {
//            String url = Common.URL + "/Subject";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "Achievement");
//            String jsonOut = jsonObject.toString();
//
//
//            myTask = new MyTask(url, jsonOut);
//            try {
//                String jsonIn = myTask.execute().get();
//                Log.d(TAG, jsonIn);
//                Type listType = new TypeToken<List<Exam>>() {
//                }.getType();
//                exams = new Gson().fromJson(jsonIn, listType);
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (exams == null || exams.isEmpty()) {
//                Common.showToast(getActivity(), "No User Found");
//            } else {
//
////                PieDataSet pieDataSet = new PieDataSet(exams,);
//            }
//
//        } else {
//            Common.showToast(getActivity(), "No network connection available");
//        }
//return view;

        /* 取得各品牌車單月銷售量資料 */
        List<PieEntry> pieEntries = getSalesEntries();

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Achievement");
        pieDataSet.setValueTextColor(Color.BLUE);
        pieDataSet.setValueTextSize(20);
        pieDataSet.setSliceSpace(3);//像素顏色

        /* 使用官訂顏色範本，顏色不能超過5種，否則官定範本要加顏色 */
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        return view;
    }



    private List<PieEntry> getSalesEntries() {
        List<PieEntry> salesEntries = new ArrayList<>();
        salesEntries.add(new PieEntry(50, ""));
        salesEntries.add(new PieEntry(45, ""));
        salesEntries.add(new PieEntry(80, ""));
        salesEntries.add(new PieEntry(11, ""));
        salesEntries.add(new PieEntry(99, ""));
        return salesEntries;



    }


}
