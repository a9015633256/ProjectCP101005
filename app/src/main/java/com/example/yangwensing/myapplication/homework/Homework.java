package com.example.yangwensing.myapplication.homework;

import java.io.Serializable;
import java.sql.Date;

public class Homework implements Serializable{

    private int id;
    private String subject, teacher, title, content;
    private Date date;

    //為了新增作業資料而新增
    private int subjectId, teacherId, classId;

    public Homework() {
    }

    public Homework(int id, String subject, String teacher, String title, String content, Date date) {
        super();
        this.id = id;
        this.subject = subject;
        this.teacher = teacher;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    //為了新增作業資料而新增
    public Homework(int subjectId, int teacherId ,String title, String content,int classId) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.classId = classId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
