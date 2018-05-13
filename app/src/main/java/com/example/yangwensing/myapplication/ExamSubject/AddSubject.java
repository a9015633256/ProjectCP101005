package com.example.yangwensing.myapplication.ExamSubject;

public class AddSubject {
     private String subjectt,teacherr,classidd,title,context,date;

        public AddSubject(String subjectt, String teacherr, String classidd, String title, String context, String date) {
            super();
            this.subjectt = subjectt;
            this.teacherr = teacherr;
            this.classidd = classidd;
            this.title = title;
            this.context = context;
            this.date = date;
        }



    public String getSubjectt() {
            return subjectt;
        }

        public void setSubjectt(String subjectt) {
            this.subjectt = subjectt;
        }

        public String getTeacherr() {
            return teacherr;
        }

        public void setTeacherr(String teacherr) {
            this.teacherr = teacherr;
        }

        public String getClassidd() {
            return classidd;
        }

        public void setClassidd(String classidd) {
            this.classidd = classidd;
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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }




}
