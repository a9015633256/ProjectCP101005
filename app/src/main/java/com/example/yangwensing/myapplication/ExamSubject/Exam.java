package com.example.yangwensing.myapplication.ExamSubject;


class Exam {
    private int id, teacher, subject, classid;
    private String studentid, examtitle, date, title, context, name, classname,teachername;



    public Exam(int id, String title, String date , String context){

        this.id = id;
        this.title = title;
        this.date = date;
        this.context = context;

    }

    public Exam(int classid,String studentid,String name,String classname,String teachername){
        this.classid =classid;
        this.studentid = studentid;
        this.name = name;
        this.classname = classname;
        this.teachername = teachername;

    }


    public Exam(int id , int subject, int teacher,int classid, String title, String context ,String date) {
        this.id = id;
        this.teacher = teacher;
        this.subject = subject;
        this.classid = classid;
        this.date = date;
        this.title = title;
        this.context = context;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }


    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getExamtitle() {
        return examtitle;
    }

    public void setExamtitle(String examtitle) {
        this.examtitle = examtitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }


}
