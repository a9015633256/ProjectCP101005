package com.example.yangwensing.myapplication.classes;

import java.io.Serializable;

public class Classes implements Serializable {
    private String classes;
    private String teacher;
    public Classes(String classes, String teacher) {
        super();
        this.classes = classes;
        this.teacher = teacher;
    }
    public  String getClasses() {
        return classes;
    }
    public void setClasses(String classes) {
        this.classes = classes;
    }
    public String getTeacher() {
        return teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }


}
