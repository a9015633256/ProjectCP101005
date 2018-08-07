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
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentExamChartFragment extends Fragment {
    private static final String TAG = "StudentExamChartFragment";
    private EditText etExamName, etStudentScore, etScoreAPlus, etScoreA, etScoreB, etScoreC, etScoreD, etScoreE, etScoreAverage;
    private LineChart lineChart;
    private int maxY = 0;
    private int studentScore;
    private String averageScore;
    private int examId;
    private String examName;
    private MyTask getAchievementTask;

    //整理資料用
    private List<Integer> scoreList = new ArrayList<>();
    private List<Integer> scoreAPlusList = new ArrayList<>();
    private List<Integer> scoreAList = new ArrayList<>();
    private List<Integer> scoreBList = new ArrayList<>();
    private List<Integer> scoreCList = new ArrayList<>();
    private List<Integer> scoreDList = new ArrayList<>();
    private List<Integer> scoreEList = new ArrayList<>();
    private List<Integer> scoreFList = new ArrayList<>();
    private List<Integer> scoreGList = new ArrayList<>();
    private List<Integer> scoreHList = new ArrayList<>();
    private List<Integer> scoreIList = new ArrayList<>();
    private List<Integer> scoreJList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_exam_chart, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true

        findViews(view);

        Bundle bundle = getArguments();

        if (bundle != null) {
            examId = bundle.getInt("examId");
//            studentScore = bundle.getInt("studentScore");
            examName = bundle.getString("examName");
        } else {
            Common.showToast(getActivity(), R.string.msg_data_error);
            //假資料
            examId = 1;
            examName = "text";
            studentScore = 100;
        }

        if (examName != null) {
            etExamName.setText(examName);
        }

        //根據examId取得db資料
        if (examId != 0) {
            getDataFromDB();

            //處理數據顯示
            setupView();
            setupChart();
        }

        return view; //要改成回傳view
    }


    private void setupView() {
        double totalScore = 0;

        for (int i : scoreList) {
            totalScore += i;

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

            } else if (i >= 50) {
                scoreEList.add(i);

            } else if (i >= 40) {
                scoreFList.add(i);

            } else if (i >= 30) {
                scoreGList.add(i);

            } else if (i >= 20) {
                scoreHList.add(i);

            } else if (i >= 10) {
                scoreIList.add(i);

            } else {
                scoreJList.add(i);

            }

        }

        maxY = scoreAPlusList.size();
        int[] listForY = {
                scoreAPlusList.size(), scoreAList.size(), scoreBList.size(), scoreCList.size(), scoreDList.size(), scoreEList.size(), scoreFList.size(), scoreGList.size(), scoreHList.size(), scoreIList.size(), scoreJList.size()
        };

        for (int i : listForY) {
            if (maxY < i) {
                maxY = i;

            }
        }


        etStudentScore.setText(String.valueOf(studentScore));

        etScoreAPlus.setText(String.valueOf(scoreAPlusList.size()) + "人");
        etScoreA.setText(String.valueOf(scoreAList.size()) + "人");
        etScoreB.setText(String.valueOf(scoreBList.size()) + "人");
        etScoreC.setText(String.valueOf(scoreCList.size()) + "人");
        etScoreD.setText(String.valueOf(scoreDList.size()) + "人");
        etScoreE.setText(String.valueOf(
                scoreEList.size() +
                        scoreFList.size() +
                        scoreGList.size() +
                        scoreHList.size() +
                        scoreIList.size() +
                        scoreJList.size()
        ) + "人");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        averageScore = decimalFormat.format(totalScore / scoreList.size());
        etScoreAverage.setText(averageScore);


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

        lineChart.setBackgroundColor(Color.WHITE);
        /* 取得並設定X軸標籤文字 */
        XAxis xAxis = lineChart.getXAxis(); //沒辦法放在下面
        /* 設定最大值到100(分) */
        xAxis.setAxisMaximum(100);
        /* 設定最小值到0(分) */
        xAxis.setAxisMinimum(0);
        xAxis.setAvoidFirstLastClipping(true);

        /* 取得左側Y軸物件 */
        YAxis yAxisLeft = lineChart.getAxisLeft();
        /* 設定左側Y軸最大值 */
//        yAxisLeft.setAxisMaximum((float) (maxY * 1.3));
        yAxisLeft.setAxisMinimum((0));

        /* 取得右側Y軸物件 */
        YAxis yAxisRight = lineChart.getAxisRight();
        /* 是否顯示右側Y軸 */
        yAxisRight.setEnabled(false);

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
        List<Entry> allScoreEntries = getAllScores();
        List<Entry> averageScoreEntries = getAverageScore();
//        List<Entry> studentScoreEntries = getStudentScore();

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
        LineDataSet lineDataSetAllScore = new LineDataSet(allScoreEntries, "AllScore");
        lineDataSetAllScore.setCircleRadius(0);
        lineDataSetAllScore.setDrawCircles(false);
        lineDataSetAllScore.setDrawCircleHole(false);
        lineDataSetAllScore.setCircleColor(Color.MAGENTA);
        lineDataSetAllScore.setColor(Color.BLACK);
        lineDataSetAllScore.setLineWidth(1);
        lineDataSetAllScore.setHighLightColor(Color.CYAN);
        lineDataSetAllScore.setValueTextColor(Color.DKGRAY);
        lineDataSetAllScore.setValueTextSize(10);
        lineDataSetAllScore.setDrawValues(false);

        LineDataSet lineDataSetAverageScore = new LineDataSet(averageScoreEntries, "AverageScore");
        lineDataSetAverageScore.setCircleRadius(4);
        lineDataSetAverageScore.setDrawCircleHole(false);
        lineDataSetAverageScore.setCircleColor(Color.BLUE);
        lineDataSetAverageScore.setColor(Color.BLUE);
        lineDataSetAverageScore.setLineWidth(4);
        lineDataSetAverageScore.setHighLightColor(Color.BLUE);
        lineDataSetAverageScore.setValueTextColor(Color.DKGRAY);
        lineDataSetAverageScore.setValueTextSize(10);
        lineDataSetAverageScore.setDrawValues(false);
        lineDataSetAverageScore.setDrawVerticalHighlightIndicator(true);

//        LineDataSet lineDataSetStudentScore = new LineDataSet(studentScoreEntries, "StudentScore");
//        lineDataSetStudentScore.setCircleRadius(4);
//        lineDataSetStudentScore.setDrawCircleHole(false);
//        lineDataSetStudentScore.setCircleColor(Color.RED);
//        lineDataSetStudentScore.setColor(Color.RED);
//        lineDataSetStudentScore.setLineWidth(4);
//        lineDataSetStudentScore.setHighLightColor(Color.RED);
//        lineDataSetStudentScore.setValueTextColor(Color.DKGRAY);
//        lineDataSetStudentScore.setValueTextSize(10);
//        lineDataSetStudentScore.setDrawValues(false);


        lineChart.setClickable(false);

        //使y軸沒有小數點
        yAxisLeft.setGranularity(1.0f);
        yAxisLeft.setGranularityEnabled(true); // Required to enable granularity

        /* 有幾個LineDataSet，就繪製幾條線 */
        List<ILineDataSet> dataSets = new ArrayList<>(); //通常"I"代表他是個interface
        dataSets.add(lineDataSetAllScore);
        dataSets.add(lineDataSetAverageScore);
//        dataSets.add(lineDataSetStudentScore);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);

//        calculateMinMax();

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
        etExamName = view.findViewById(R.id.tvExamName);
        etStudentScore = view.findViewById(R.id.tvScore);
        etScoreAPlus = view.findViewById(R.id.tvScoreAPlus);
        etScoreA = view.findViewById(R.id.tvScoreA);
        etScoreB = view.findViewById(R.id.tvScoreB);
        etScoreC = view.findViewById(R.id.tvScoreC);
        etScoreD = view.findViewById(R.id.tvScoreD);
        etScoreE = view.findViewById(R.id.tvScoreE);
        etScoreAverage = view.findViewById(R.id.tvScoreAverage);
        lineChart = view.findViewById(R.id.lineChart);

    }


    private List<Entry> getAllScores() {
        List<Entry> scoreEntries = new ArrayList<>();
        //假如要忽略沒有人數的數據
//        if (scoreAPlusList.size() != 0) {
//            scoreEntries.add(new Entry(100, scoreAPlusList.size()));
//        }
//        if (scoreAList.size() != 0) {
//            scoreEntries.add(new Entry(95, scoreAList.size()));
//        }
//        if (scoreBList.size() != 0) {
//            scoreEntries.add(new Entry(85, scoreBList.size()));
//        }
//        if (scoreCList.size() != 0) {
//            scoreEntries.add(new Entry(75, scoreCList.size()));
//        }
//        if (scoreDList.size() != 0) {
//            scoreEntries.add(new Entry(65, scoreDList.size()));
//        }
//        if (scoreEList.size() != 0) {
//            scoreEntries.add(new Entry(55, scoreEList.size()));
//        }
//        if (scoreFList.size() != 0) {
//            scoreEntries.add(new Entry(45, scoreFList.size()));
//        }
//        if (scoreGList.size() != 0) {
//            scoreEntries.add(new Entry(35, scoreGList.size()));
//        }
//        if (scoreHList.size() != 0) {
//            scoreEntries.add(new Entry(25, scoreHList.size()));
//        }
//        if (scoreIList.size() != 0) {
//            scoreEntries.add(new Entry(15, scoreIList.size()));
//        }
//        if (scoreJList.size() != 0) {
//            scoreEntries.add(new Entry(5, scoreJList.size()));
//        }

        scoreEntries.add(new Entry(100, scoreAPlusList.size()));
        scoreEntries.add(new Entry(95, scoreAList.size()));
        scoreEntries.add(new Entry(85, scoreBList.size()));
        scoreEntries.add(new Entry(75, scoreCList.size()));
        scoreEntries.add(new Entry(65, scoreDList.size()));
        scoreEntries.add(new Entry(55, scoreEList.size()));
        scoreEntries.add(new Entry(45, scoreFList.size()));
        scoreEntries.add(new Entry(35, scoreGList.size()));
        scoreEntries.add(new Entry(25, scoreHList.size()));
        scoreEntries.add(new Entry(15, scoreIList.size()));
        scoreEntries.add(new Entry(5, scoreJList.size()));
        Collections.sort(scoreEntries, new EntryXComparator());


        return scoreEntries;
    }

    private List<Entry> getAverageScore() {
        List<Entry> scoreEntries = new ArrayList<>();
        float averageScoreAsFloat = Float.valueOf(averageScore);
        scoreEntries.add(new Entry(averageScoreAsFloat, maxY));


        return scoreEntries;
    }

//    private List<Entry> getStudentScore() {
//        List<Entry> scoreEntries = new ArrayList<>();
//        scoreEntries.add(new Entry(studentScore, maxY));
//
//
//        return scoreEntries;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scoreList.clear();
        scoreAPlusList.clear();
        scoreAList.clear();
        scoreBList.clear();
        scoreCList.clear();
        scoreDList.clear();
        scoreEList.clear();
        scoreFList.clear();
        scoreGList.clear();
        scoreHList.clear();
        scoreIList.clear();
        scoreJList.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getAchievementTask != null) {
            getAchievementTask.cancel(true);
        }
    }
}

