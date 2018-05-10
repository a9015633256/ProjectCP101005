package com.example.yangwensing.myapplication.homework;

import java.sql.Date;

public class HomeworkIsDone extends Homework {
	private boolean isHomeworkDone;

	public HomeworkIsDone(int id, String subject, String teacher, String title, String content, Date date) {
		super(id, subject, teacher, title, content, date);
	}

	public HomeworkIsDone(int id, String subject, String teacher, String title, String content, Date date,
			boolean isHomeworkDone) {
		super(id, subject, teacher, title, content, date);
		this.isHomeworkDone = isHomeworkDone;
	}

	public boolean isHomeworkDone() {
		return isHomeworkDone;
	}

	public void setHomeworkDone(boolean isHomeworkDone) {
		this.isHomeworkDone = isHomeworkDone;
	}



}
