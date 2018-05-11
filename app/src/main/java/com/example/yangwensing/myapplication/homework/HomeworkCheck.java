package com.example.yangwensing.myapplication.homework;

public class HomeworkCheck {
    private int studentId;
    private String studentNumber, studentName;
    private boolean isHomeworkDone;

   

    public HomeworkCheck(int studentId,String studentNumber, String studentName,  boolean isHomeworkDone) {
		super();
		this.studentNumber = studentNumber;
		this.studentName = studentName;
		this.studentId = studentId;
		this.isHomeworkDone = isHomeworkDone;
	}

	public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public boolean isHomeworkDone() {
        return isHomeworkDone;
    }

    public void setHomeworkDone(boolean homeworkDone) {
        isHomeworkDone = homeworkDone;
    }

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
    
    
}