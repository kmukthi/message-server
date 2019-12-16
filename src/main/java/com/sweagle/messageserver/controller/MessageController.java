package com.sweagle.messageserver.controller;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sweagle.messageserver.entity.Message;
import com.sweagle.messageserver.service.MessageService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/message")
@Api(value = "Operations pertaining to messages")
public class MessageController {
	
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(path ="/send", method = RequestMethod.POST)
	public Message sendMessage(@RequestBody Message message) {
		return messageService.saveMessage(message);
	}
	
	@RequestMapping(path ="/getReceivedMessages", method = RequestMethod.GET)
	public List<Message> getReceivedMessages(@RequestParam String email) {
		return messageService.getMessagesReceivedByEmail(email);
	}

	@RequestMapping(path ="/getSentMessages", method = RequestMethod.GET)
	public List<Message> getSentMessages(@RequestParam String email) {
		return messageService.getMessagesSentByEmail(email);
	}
	
	@RequestMapping(path ="/getMessageById", method = RequestMethod.GET)
	public Message getMessageById(@RequestParam String id) {
		return messageService.getMessageById(id);
	}
	@RequestMapping(path ="/getProbableCountOfMessagesToSendForTheRestOfTheDay", method = RequestMethod.GET)
	public long probableCountOfMessagesToSendForTheRestOfTheDay() {
		ZonedDateTime rightNow = ZonedDateTime.now();
		return messageService.probableCountOfMessagesToSendForTheRestOfTheDay(rightNow);
	}
	
	@RequestMapping(path ="/getProbableCountOfMessagesToSendForTheRestOfTheWeek", method = RequestMethod.GET)
	public long probableCountOfMessagesToSendForTheRestOfTheWeek() {
		ZonedDateTime rightNow = ZonedDateTime.now();
		return messageService.probableCountOfMessagesToSendForTheRestOfTheWeek(rightNow);
	}
	
	
}
