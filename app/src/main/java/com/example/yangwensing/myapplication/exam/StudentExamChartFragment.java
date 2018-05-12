package com.example.yangwensing.myapplication.exam;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.yangwensing.myapplication.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StudentExamChartFragment extends Fragment {
    private EditText etExamName, etStudentScore, etScoreA, etScoreB, etScoreC, etScoreD, etScoreE, etScoreAverage;
    private LineChart lineChart;
    private String TAG = "StudentExamChartFragment";
    private int maxY = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_exam_chart, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true

        findViews(view);


        int studentScore = 50;

        //假資料
        List<Integer> scoreList = new ArrayList<>();
        scoreList.add(100);
        scoreList.add(50);
        scoreList.add(80);
        scoreList.add(88);
        scoreList.add(76);
        scoreList.add(99);
        scoreList.add(50);
        scoreList.add(33);
        scoreList.add(40);
        scoreList.add(55);
        scoreList.add(50);
        scoreList.add(33);
        scoreList.add(33);
        scoreList.add(34);
        scoreList.add(60);
        scoreList.add(2);

        List<Integer> scoreAList = new ArrayList<>();
        List<Integer> scoreBList = new ArrayList<>();
        List<Integer> scoreCList = new ArrayList<>();
        List<Integer> scoreDList = new ArrayList<>();
        List<Integer> scoreEList = new ArrayList<>();
        double totalScore = 0;

        for (int i : scoreList) {
            totalScore += i;

            if (i >= 90) {
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

        maxY = scoreAList.size();
        int[] listForY = {scoreAList.size(), scoreBList.size(), scoreCList.size(), scoreDList.size(), scoreEList.size()};

        for (int i : listForY) {
            if (maxY < i) {
                maxY = i;

            }
        }


        etStudentScore.setText(String.valueOf(studentScore));

        etScoreA.setText(String.valueOf(scoreAList.size()) + "人");
        etScoreB.setText(String.valueOf(scoreBList.size()) + "人");
        etScoreC.setText(String.valueOf(scoreCList.size()) + "人");
        etScoreD.setText(String.valueOf(scoreDList.size()) + "人");
        etScoreE.setText(String.valueOf(scoreEList.size()) + "人");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String averageScore = decimalFormat.format(totalScore / scoreList.size());
        etScoreAverage.setText(averageScore);

        setupChart();


//        int[] scores = scoreList.toArray()[scoreList.size()];


//        //取得上一頁資料
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            homeworkIsDone = (HomeworkIsDone) bundle.getSerializable("homework");
//        }
//
//        if (homeworkIsDone != null) {
//            etTitle.setText(homeworkIsDone.getTitle());
//            etContent.setText(homeworkIsDone.getContent());
//        } else {
//            Common.showToast(getActivity(), R.string.text_data_error);
//        }


        //設標題
//        getActivity().setTitle(homeworkIsDone.getSubject()+"   授課老師:"+homeworkIsDone.getTeacher());


        return view; //要改成回傳view
    }

    private void setupChart() {

        lineChart.setBackgroundColor(Color.WHITE);
        /* 取得並設定X軸標籤文字 */
        XAxis xAxis = lineChart.getXAxis(); //沒辦法放在下面
        /* 設定最大值到100(分) */
        xAxis.setAxisMaximum(100);

        /* 取得左側Y軸物件 */
        YAxis yAxisLeft = lineChart.getAxisLeft();
        /* 設定左側Y軸最大值 */
        yAxisLeft.setAxisMaximum((float) (maxY*1.3));

        /* 取得右側Y軸物件 */
        YAxis yAxisRight = lineChart.getAxisRight();
        /* 是否顯示右側Y軸 */
        yAxisRight.setEnabled(false);

//        /* 設定右下角描述文字 */
//        Description description = new Description();
//        description.setText("Car Sales in Taiwan");
//        description.setTextSize(16);
//        lineChart.setDescription(description);

//        /* 監聽是否點選chart內容值 */
//        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            /* entry: 儲存著被點選項目的值；包含X與Y軸的值
//             *  highlight: 儲存著標記相關資訊，也包含X與Y軸的值
//             *  有entry物件就不太需要highlight物件 */
//            public void onValueSelected(Entry entry, Highlight highlight) { //Entry為API裡的類別
//                Log.d(TAG, "entry: " + entry.toString() + "; highlight: " + highlight.toString());
//                String text = (int) entry.getX() + "\n" + (int) entry.getY();
//                Common.showToast(getActivity(), text);
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });

        /* 取得各品牌車每月銷售量資料 */
//        List<Entry> toyotaEntries = getToyotaEntries();
        List<Entry> scoreEntries = getScores();

//        /* 利用List<Entry>資料建立LineDataSet，line chart需要LineDataSet資料集來繪圖 */
//        LineDataSet lineDataSetToyota = new LineDataSet(toyotaEntries, "Toyota");
//        /* 設定資料圓點半徑 */
//        lineDataSetToyota.setCircleRadius(4);
//        /* 設定資料圓點是否中空 */
//        lineDataSetToyota.setDrawCircleHole(true);
//        /* 設定資料圓點顏色 */
//        lineDataSetToyota.setCircleColor(Color.RED);
//        /* 設定線的顏色 */
//        lineDataSetToyota.setColor(Color.BLUE);
//        /* 設定線的粗細 */
//        lineDataSetToyota.setLineWidth(4);
//        /* 設定highlight線的顏色，highlight線為點擊就會顯示的線 */
//        lineDataSetToyota.setHighLightColor(Color.CYAN);
//        /* 設定資料點上的文字顏色 */
//        lineDataSetToyota.setValueTextColor(Color.DKGRAY);
//        /* 設定資料點上的文字大小 */
//        lineDataSetToyota.setValueTextSize(10);

        //設定用建構式放入資料、用方法設定UI
        LineDataSet lineDataSetNissan = new LineDataSet(scoreEntries, "Score");
        lineDataSetNissan.setCircleRadius(4);
        lineDataSetNissan.setDrawCircleHole(false);
        lineDataSetNissan.setCircleColor(Color.MAGENTA);
        lineDataSetNissan.setColor(Color.GREEN);
        lineDataSetNissan.setLineWidth(4);
        lineDataSetNissan.setHighLightColor(Color.CYAN);
        lineDataSetNissan.setValueTextColor(Color.DKGRAY);
        lineDataSetNissan.setValueTextSize(10);

        /* 有幾個LineDataSet，就繪製幾條線 */
        List<ILineDataSet> dataSets = new ArrayList<>(); //通常"I"代表他是個interface
//        dataSets.add(lineDataSetToyota);
        dataSets.add(lineDataSetNissan);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate(); //最後重繪
    }

    private void findViews(View view) {
        etExamName = view.findViewById(R.id.tvExamName);
        etStudentScore = view.findViewById(R.id.tvScore);
        etScoreA = view.findViewById(R.id.tvScoreA);
        etScoreB = view.findViewById(R.id.tvScoreB);
        etScoreC = view.findViewById(R.id.tvScoreC);
        etScoreD = view.findViewById(R.id.tvScoreD);
        etScoreE = view.findViewById(R.id.tvScoreE);
        etScoreAverage = view.findViewById(R.id.tvScoreAverage);
        lineChart = view.findViewById(R.id.lineChart);


    }

//    private List<Entry> getToyotaEntries() {
//        List<Entry> toyotaEntries = new ArrayList<>();
//        /* 一個Entry儲存著一筆資訊，在此代表X與Y軸的值 */
//        toyotaEntries.add(new Entry(1, 10000));
//        toyotaEntries.add(new Entry(2, 12000));
//        toyotaEntries.add(new Entry(3, 13500));
//        toyotaEntries.add(new Entry(4, 12500));
//        toyotaEntries.add(new Entry(5, 13300));
//        toyotaEntries.add(new Entry(6, 12400));
//        toyotaEntries.add(new Entry(7, 11500));
//        toyotaEntries.add(new Entry(8, 12500));
//        toyotaEntries.add(new Entry(9, 12300));
//        toyotaEntries.add(new Entry(10, 13000));
//        toyotaEntries.add(new Entry(11, 13200));
//        toyotaEntries.add(new Entry(12, 14000));
//        return toyotaEntries;
//    }

    private List<Entry> getScores() {
        List<Entry> scoreEntries = new ArrayList<>();
//        scoreEntries.add(new Entry(95,))




//        nissanEntries.add(new Entry(1, 3000));
//        nissanEntries.add(new Entry(2, 3200));
//        nissanEntries.add(new Entry(3, 3500));
//        nissanEntries.add(new Entry(4, 3150));
//        nissanEntries.add(new Entry(5, 3300));
//        nissanEntries.add(new Entry(6, 3400));
//        nissanEntries.add(new Entry(7, 3120));
//        nissanEntries.add(new Entry(8, 3250));
//        nissanEntries.add(new Entry(9, 3300));
//        nissanEntries.add(new Entry(10, 3230));
//        nissanEntries.add(new Entry(11, 3350));
//        nissanEntries.add(new Entry(12, 3400));
        return scoreEntries;
    }


}

