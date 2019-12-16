package com.sweagle.messageserver.service;

import java.time.ZonedDateTime;
import java.util.List;

import com.sweagle.messageserver.entity.Message;

public interface MessageService {
	
	Message saveMessage(Message message);
	
	List<Message> getMessagesReceivedByEmail(String email);
	
	List<Message> getMessagesSentByEmail(String email);

	Message getMessageById(String id);
	
	long getProbableCountOfMessagesToSendForTheRestOfTheDay(ZonedDateTime rightNow);
	
	long getProbableCountOfMessagesToSendForTheRestOfTheWeek(ZonedDateTime rightNow);
	
	void deleteMessageBySender(String sender);
	
}
