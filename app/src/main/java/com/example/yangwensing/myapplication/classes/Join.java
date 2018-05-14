package com.example.yangwensing.myapplication.classes;

public class Join {
    private int ClassName,ClassTeacher;

    public Join(int className, int classTeacher) {
        ClassName = className;
        ClassTeacher = classTeacher;
    }

    public int getClassName() {
        return ClassName;
    }

    public void setClassName(int className) {
        ClassName = className;
    }

    public int getClassTeacher() {
        return ClassTeacher;
    }

    public void setClassTeacher(int classTeacher) {
        ClassTeacher = classTeacher;
    }
}
