package com.example.yangwensing.myapplication.ExamSubject;


class Exam {
    private int id, teacher, subject, classid,teacherid,subjectid,examsubjectid,examstudent,AchievementID,examsubject,achievementid,score;
    private String studentid, examtitle, date, title, context, name, classname,teacheraccount,teachername;


    public Exam (int classid,String examtitle,int subjectid,int teacherid,int examsubjectid,int score){
        this.classid = classid;
        this.examtitle = examtitle;
        this.subjectid = subjectid;
        this.teacherid = teacherid;
        this.examsubjectid = examsubjectid;
        this.score = score;
    }

    public Exam(int id, String title, String date , String context){

        this.id = id;
        this.title = title;
        this.date = date;
        this.context = context;

    }

    public Exam(int AchievementID,int examsubjectid,int examstudent,int subjectid,String studentid,String name,
                int classid,String classname,int teacherid,String teacheraccount,String teachername,int score,String title){
        this.examsubjectid = examsubjectid;
        this.AchievementID = AchievementID;
        this.examstudent = examstudent;
        this.subjectid = subjectid;
        this.studentid = studentid;
        this.name = name;
        this.classid = classid;
        this.classname = classname;
        this.teacherid = teacherid;
        this.teacheraccount = teacheraccount;
        this.teachername = teachername;
        this.score = score;
        this.title = title;
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

    public int getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(int teacherid) {
        this.teacherid = teacherid;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public int getExamsubjectid() {
        return examsubjectid;
    }

    public void setExamsubjectid(int examsubjectid) {
        this.examsubjectid = examsubjectid;
    }

    public int getExamstudent() {
        return examstudent;
    }

    public void setExamstudent(int examstudent) {
        this.examstudent = examstudent;
    }

    public int getAchievementID() {
        return AchievementID;
    }

    public void setAchievementID(int achievementID) {
        AchievementID = achievementID;
    }

    public int getExamsubject() {
        return examsubject;
    }

    public void setExamsubject(int examsubject) {
        this.examsubject = examsubject;
    }

    public int getAchievementid() {
        return achievementid;
    }

    public void setAchievementid(int achievementid) {
        this.achievementid = achievementid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public String getTeacheraccount() {
        return teacheraccount;
    }

    public void setTeacheraccount(String teacheraccount) {
        this.teacheraccount = teacheraccount;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }
}
