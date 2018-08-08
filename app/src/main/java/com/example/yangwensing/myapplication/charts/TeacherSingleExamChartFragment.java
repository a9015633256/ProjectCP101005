package com.example.yangwensing.myapplication.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TeacherSingleExamChartFragment extends Fragment {
    private static final String TAG = "TeacherSingleExamChartFragment";
    private EditText etScoreAPlus, etScoreA, etScoreB, etScoreC, etScoreD, etScoreE, etScoreAverage, etScoreHighest, etScoreLowest;
    private TextView tvExamName;
    private BarChart lineChart;
    private int examId;
    private MyTask getAchievementTask;
    private BottomNavigationView bottomNavigationView;

    //整理資料用
    private List<Integer> scoreList = new ArrayList<>();
    private List<Integer> scoreAPlusList = new ArrayList<>();
    private List<Integer> scoreAList = new ArrayList<>();
    private List<Integer> scoreBList = new ArrayList<>();
    private List<Integer> scoreCList = new ArrayList<>();
    private List<Integer> scoreDList = new ArrayList<>();
    private List<Integer> scoreEList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_exam_chart, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true

        getActivity().setTitle(R.string.title_chart_exam);

        findViews(view);

        Bundle bundle = getArguments();

        //取得上一頁傳來的examID跟examName
        String examName;
        if (bundle != null) {
            examId = bundle.getInt("examId");
            examName = bundle.getString("examName");

        } else {
            Common.showToast(getActivity(), R.string.msg_data_error);

            //假資料
            examId = 1;
            examName = "examName";
        }
        if (examName != null) {
            tvExamName.setText(examName);
        }

        //根據examID取得db資料
        if (examId != 0) {
            getDataFromDB();

            //處理數據顯示
            setupView();
            setupChart();
        }

        return view; //要改成回傳view
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
        if (getAchievementTask != null) {
            getAchievementTask.cancel(true);
        }
        super.onStop();
    }


    private void setupView() {
        double totalScore = 0;
        int highestScore = 0;
        int lowestScore = 100;

        //清空資料
        scoreAPlusList.clear();
        scoreAList.clear();
        scoreBList.clear();
        scoreCList.clear();
        scoreDList.clear();
        scoreEList.clear();

        //分類
        for (int i : scoreList) {
            totalScore += i;
            if (i > highestScore) {
                highestScore = i;
            }
            if (i < lowestScore) {
                lowestScore = i;
            }

            if (i == 100) {
                scoreAPlusList.add(i);
            } else if (i >= 90) {
                scoreAList.add(i);

            } else if (i >= 80) {
                scoreBList.add(i);

            } else if (i >= 70) {
                scoreCList.add(i);

            } else if (i >= 60) {
                scoreDList.add(i);

            } else {
                scoreEList.add(i);

            }

        }


        etScoreAPlus.setText(String.valueOf(scoreAPlusList.size()) + "人");
        etScoreA.setText(String.valueOf(scoreAList.size()) + "人");
        etScoreB.setText(String.valueOf(scoreBList.size()) + "人");
        etScoreC.setText(String.valueOf(scoreCList.size()) + "人");
        etScoreD.setText(String.valueOf(scoreDList.size()) + "人");
        etScoreE.setText(String.valueOf(
                scoreEList.size()
        ) + "人");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String averageScore = decimalFormat.format(totalScore / scoreList.size());
        etScoreAverage.setText(averageScore + "分");
        etScoreHighest.setText(highestScore + "分");
        etScoreLowest.setText(lowestScore + "分");


    }

    private void getDataFromDB() {
        if (Common.networkConnected(getActivity())) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findAchievementByExamId");
            jsonObject.addProperty("examId", examId);
            getAchievementTask = new MyTask(Common.URLForMingTa + "/ExamServlet", jsonObject.toString());

            try {

                String jsonIn = getAchievementTask.execute().get();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type listType = new TypeToken<List<Integer>>() {
                }.getType();

                scoreList.clear();
                scoreList = gson.fromJson(jsonIn, listType);


            } catch (Exception e) {
                e.printStackTrace();
                Common.showToast(getActivity(), R.string.text_no_server);
            }
        } else {
            Common.showToast(getActivity(), R.string.text_no_network);
        }
    }

    private void setupChart() {

        lineChart.setBackgroundColor(Color.TRANSPARENT); //背景透明
        lineChart.getLegend().setEnabled(false); //隱藏顏色label(每個顏色代表什麼意義的label)
        lineChart.setTouchEnabled(false); //關閉圖表互動功能

        /* 取得並設定X軸標籤文字 */
        XAxis xAxis = lineChart.getXAxis(); //沒辦法放在下面
        /* 設定最大值到100(分) */
//        xAxis.setAxisMaximum(7); //把分數分成六個區間，0跟7作為左右留空
        /* 設定最小值到0(分) */
//        xAxis.setAxisMinimum(0);
//        xAxis.setLabelCount(8, true); //強制顯示每個label，其中count要與max, min相符才有效
//        xAxis.setAvoidFirstLastClipping(true);

        //設定x軸文字
        IAxisValueFormatter xAxisFormatter = new ScoreAxisValueFormatter(); //自創類別，實作其方法:根據x數值回傳想要的字串
        xAxis.setValueFormatter(xAxisFormatter);

        /* 取得左側Y軸物件 */
        YAxis yAxisLeft = lineChart.getAxisLeft();
        /* 設定左側Y軸最大值 */
//        yAxisLeft.setAxisMaximum((float) (maxY * 1.3));
        yAxisLeft.setAxisMinimum((0)); //maxY預設就會比最大數值高一點點
        //使y軸沒有小數點
        yAxisLeft.setGranularity(1.0f);
        yAxisLeft.setGranularityEnabled(true); // Required to enable granularity

        /* 取得右側Y軸物件 */
        YAxis yAxisRight = lineChart.getAxisRight();
        /* 是否顯示右側Y軸 */
        yAxisRight.setDrawLabels(false); //設定顯示右側y軸，但不要有數值也不要格線->就只是作為右框線
        yAxisRight.setDrawGridLines(false);
//        yAxisRight.setEnabled(false);

        /* 設定右下角描述文字 */
        Description description = new Description();
        description.setText("");
        description.setTextSize(16);
        lineChart.setDescription(description);

        /* 監聽是否點選chart內容值 */
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            /* entry: 儲存著被點選項目的值；包含X與Y軸的值
             *  highlight: 儲存著標記相關資訊，也包含X與Y軸的值
             *  有entry物件就不太需要highlight物件 */
            public void onValueSelected(Entry entry, Highlight highlight) { //Entry為API裡的類別
//                Log.d(TAG, "entry: " + entry.toString() + "; highlight: " + highlight.toString());
//                String text = (int) entry.getX() + "\n" + (int) entry.getY();
//                Common.showToast(getActivity(), text);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        /* 取得各品牌車每月銷售量資料 */
        List<BarEntry> allScoreEntries = getAllScores(); //自方，把分數資料做分類

        //設定用建構式放入資料、用方法設定UI
        BarDataSet lineDataSetAllScore = new BarDataSet(allScoreEntries, null); //label是legend，即左下角的label
//        lineDataSetAllScore.setColor(R.color.orange); //預設是淡藍色
        lineDataSetAllScore.setHighLightColor(Color.CYAN);
        lineDataSetAllScore.setValueTextColor(Color.DKGRAY);
        lineDataSetAllScore.setValueTextSize(10);
        lineDataSetAllScore.setDrawValues(true);

        //自定類別，設定資料上面的文字標記
        IValueFormatter peopleCountValueFormatter = new PeopleCountValueFormatter();
        lineDataSetAllScore.setValueFormatter(peopleCountValueFormatter);

        /* 有幾個LineDataSet，就繪製幾條線 */
        List<IBarDataSet> dataSets = new ArrayList<>(); //通常"I"代表他是個interface
        dataSets.add(lineDataSetAllScore);
        BarData lineData = new BarData(dataSets);
        lineChart.setData(lineData);

        lineChart.invalidate(); //最後重繪

    }

    //避免最大最小值太接近，但是目前不知道labelCount是什麼
    private void calculateMinMax(LineChart chart, int labelCount) {
        float maxValue = chart.getData().getYMax();
        float minValue = chart.getData().getYMin();

        if ((maxValue - minValue) < labelCount) {
            float diff = labelCount - (maxValue - minValue);
            maxValue = maxValue + diff;
            chart.getAxisLeft().setAxisMaximum(maxValue);
            chart.getAxisLeft().setAxisMinimum(minValue);
        }
    }

    private void findViews(View view) {
        tvExamName = view.findViewById(R.id.tvExamName);
        etScoreAPlus = view.findViewById(R.id.tvScoreAPlus);
        etScoreA = view.findViewById(R.id.tvScoreA);
        etScoreB = view.findViewById(R.id.tvScoreB);
        etScoreC = view.findViewById(R.id.tvScoreC);
        etScoreD = view.findViewById(R.id.tvScoreD);
        etScoreE = view.findViewById(R.id.tvScoreE);
        etScoreAverage = view.findViewById(R.id.etScoreAverage);
        etScoreHighest = view.findViewById(R.id.etScoreHighest);
        etScoreLowest = view.findViewById(R.id.etScoreLowest);
        lineChart = view.findViewById(R.id.lineChart);
        bottomNavigationView = getActivity().findViewById(R.id.btNavigation_Bar);

    }


    private List<BarEntry> getAllScores() {
        List<BarEntry> scoreEntries = new ArrayList<>();

        scoreEntries.add(new BarEntry(6, scoreAPlusList.size()));
        scoreEntries.add(new BarEntry(5, scoreAList.size()));
        scoreEntries.add(new BarEntry(4, scoreBList.size()));
        scoreEntries.add(new BarEntry(3, scoreCList.size()));
        scoreEntries.add(new BarEntry(2, scoreDList.size()));
        scoreEntries.add(new BarEntry(1, scoreEList.size()));
        Collections.sort(scoreEntries, new EntryXComparator());

        return scoreEntries;
    }


}

