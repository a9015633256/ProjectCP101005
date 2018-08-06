package com.example.yangwensing.myapplication.charts;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class ScoreAxisValueFormatter implements IAxisValueFormatter {


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String stringValue = String.valueOf(value);

        switch (stringValue) {
            case "6.0" :
                return "100分";
            case "5.0" :
                return "90~99分";
            case "4.0" :
                return "80~89分";
            case "3.0" :
                return "70~79分";
            case "2.0" :
                return "60~69分";
            case "1.0" :
                return "0~59分";
            default:
                return "";
        }

    }
}
