package com.sweagle.messageserver.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class Message {
	
	@Id
	private String id;
	
	private String sender;
	
	private String receiver;
	
	private String subject;
	
	private String content;
	
	private Date date;
	
	private Message() {
		
	}
	
	public Message(String sender, String receiver, String subject, String content) {
		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.content = content;
		this.date = new Date();
	}
	
	public Message(String sender, String receiver, String subject, String content, Date date) {
		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.content = content;
		this.date = date;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
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
