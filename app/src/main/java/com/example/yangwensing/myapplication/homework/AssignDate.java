package com.example.yangwensing.myapplication.homework;

import java.util.List;

public class AssignDate {
    private int year, month, date;
    private List<Homework> homeworkList;

    public AssignDate(int year, int month, int date) {
        this.year = year;
        this.month = month;
        this.date = date;
    }

    public AssignDate(int year, int month, int date, List<Homework> homeworkList) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.homeworkList = homeworkList;
    }

    public List<Homework> getHomeworkList() {
        return homeworkList;
    }

    public void setHomeworkList(List<Homework> homeworkList) {
        this.homeworkList = homeworkList;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    //串連年月日方法
    public String showDate() {
        return year +
                "年" +
                month +
                "月" +
                date +
                "日";

    }

}
