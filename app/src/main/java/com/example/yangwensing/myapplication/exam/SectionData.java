package com.example.yangwensing.myapplication.exam;

import android.support.annotation.NonNull;


import java.util.ArrayList;
import java.util.Collection;

public class SectionData extends ArrayList {
    private String examSujbectId;

    //繼承自動產生的建構式，只有第二項有用
    public SectionData(int initialCapacity, String examSujbectId) {
        super(initialCapacity);
        this.examSujbectId = examSujbectId;
    }

    public SectionData(String examSujbectId) {
        this.examSujbectId = examSujbectId;
    }

    public SectionData(@NonNull Collection<?> c, String examSujbectId) {
        super(c);
        this.examSujbectId = examSujbectId;
    }

    public String getExamSujbectId() {
        return examSujbectId;
    }

    public void setExamSujbectId(String examSujbectId) {
        this.examSujbectId = examSujbectId;
    }

    //確認日期不會重複
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SectionData)) return false;
        return this.examSujbectId.equals(((SectionData) o).examSujbectId);
    }

    @Override
    public int hashCode() {

        return examSujbectId.hashCode();
    }
}
