package com.example.yangwensing.myapplication.main;

import java.io.Serializable;

public class Account implements Serializable {
	private String account;

	public Account(String account) {
		super();
		this.account = account;

	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}
}
