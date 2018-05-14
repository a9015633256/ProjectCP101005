package com.example.yangwensing.myapplication.classes;

import java.io.Serializable;

public class Classes implements Serializable {
	private String classes;
	private String teacher;
	private int id,teacherid;
	public Classes(String classes, String teacher,int id,int teacherid) {
		super();
		this.classes = classes;
		this.teacher = teacher;
		this.id = id;
		this.teacherid = teacherid;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTeacherid() {
		return teacherid;
	}

	public void setTeacherid(int teacherid) {
		this.teacherid = teacherid;
	}
}
