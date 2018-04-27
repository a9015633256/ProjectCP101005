package com.example.yangwensing.myapplication.homework;

public class Homework {
    private boolean isCompleted = true;
    private String subject;

    public Homework(String subject, boolean isCompleted) {

        this.subject = subject;
        this.isCompleted = isCompleted;

    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


}
