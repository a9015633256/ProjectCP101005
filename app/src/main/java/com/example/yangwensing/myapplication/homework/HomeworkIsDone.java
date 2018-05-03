package com.example.yangwensing.myapplication.homework;

import java.sql.Date;

public class HomeworkIsDone extends Homework {
	private boolean isHomewokDone;

	public HomeworkIsDone(int id, String subject, String teacher, String title, String content, Date date) {
		super(id, subject, teacher, title, content, date);
		// TODO Auto-generated constructor stub
	}

	public HomeworkIsDone(int id, String subject, String teacher, String title, String content, Date date,
			boolean isHomewokDone) {
		super(id, subject, teacher, title, content, date);
		this.isHomewokDone = isHomewokDone;
	}

	public boolean isHomewokDone() {
		return isHomewokDone;
	}

	public void setHomewokDone(boolean isHomewokDone) {
		this.isHomewokDone = isHomewokDone;
	}
	
	

}
