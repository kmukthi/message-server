package com.sweagle.messageserver.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

	@Id 
	private String id;
	@Indexed(unique = true)
	private String emailId;
	private String firstName;
	private String lastName;
	
	private User() {
		
	}
	
	public User(String emailId, String firstName, String lastName) {
		this.emailId = emailId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	  
	public String getId() {
		return id;
	}
		
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmailId() {
		return emailId;
	}
		
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

  
}
