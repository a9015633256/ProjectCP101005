package com.example.yangwensing.myapplication.classes;

public class Classa {
	private String name;
	private int id,classname,teacherid;
	public Classa(int id, String name,int classname,int teacherid) {
		super();

		this.id = id;
		this.name = name;
		this.classname = classname;
		this.teacherid = teacherid;

	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getClassname() {
		return classname;
	}

	public void setClassname(int classname) {
		this.classname = classname;
	}

	public int getTeacherid() {
		return teacherid;
	}

	public void setTeacherid(int teacherid) {
		this.teacherid = teacherid;
	}
}
