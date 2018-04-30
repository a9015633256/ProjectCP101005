package com.example.yangwensing.myapplication.homework;

import java.sql.Date;

public class Homework {
	
	private int id;
	private String subject,teacher,title,content;
	private Date date;
	public Homework(int id, String subject, String teacher, String title, String content, Date date) {
		super();
		this.id = id;
		this.subject = subject;
		this.teacher = teacher;
		this.title = title;
		this.content = content;
		this.date = date;
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
	
	
	


}
