package com.example.yangwensing.myapplication.chat;

import java.io.Serializable;

public class ChatLists implements Serializable {
	private String receiver;

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public ChatLists(String receiver) {

		this.receiver = receiver;
	}
}
