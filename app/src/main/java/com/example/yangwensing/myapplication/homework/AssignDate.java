package com.example.yangwensing.myapplication.homework;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

public class AssignDate extends ArrayList{
    private String formattedDate;

    //繼承自動產生的建構式，只有第二項有用
    public AssignDate(int initialCapacity, String formattedDate) {
        super(initialCapacity);
        this.formattedDate = formattedDate;
    }

    public AssignDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public AssignDate(@NonNull Collection<?> c, String formattedDate) {
        super(c);
        this.formattedDate = formattedDate;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    //確認日期不會重複
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssignDate)) return false;
        return this.formattedDate.equals(((AssignDate) o).formattedDate);
    }

    @Override
    public int hashCode() {

        return formattedDate.hashCode();
    }
}
