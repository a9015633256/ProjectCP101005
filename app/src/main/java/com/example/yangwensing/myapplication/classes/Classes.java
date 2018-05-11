package com.example.yangwensing.myapplication.classes;

import java.io.Serializable;

public class Classes implements Serializable {
	private int id;
	private String classes;
	private String teacher;
	public Classes(int id, String classes, String teacher) {
		super();
		this.id = id;
		this.classes = classes;
		this.teacher = teacher;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClasses() {
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


}
