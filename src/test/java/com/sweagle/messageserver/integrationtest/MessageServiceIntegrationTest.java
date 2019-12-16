package com.sweagle.messageserver.integrationtest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sweagle.messageserver.entity.Message;
import com.sweagle.messageserver.entity.User;
import com.sweagle.messageserver.service.MessageService;
import com.sweagle.messageserver.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceIntegrationTest {

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;
	
	private static int[] numberOFmsgs = {20, 14, 42, 70};
	
	private static int MID_NIGHT = 0;
	private static int THREE_AM = 3;
	private static int NINE_AM = 9;
	private static int THREE_PM = 15;
	private static int NINE_PM = 21;
	
	private static String VALID_SENDER = "sender"+UUID.randomUUID()+"@abc.com";
	private static String VALID_RECEIVER = "receiver"+UUID.randomUUID()+"@xyz.com";
	
	@Before
	public void setup() {
		createSenderAndUser(VALID_SENDER, VALID_RECEIVER);
	}
	
	@After
	public void tearDown() {
		deleteAllMessages();
		deleteUsers();
	}
	
	@Test
	public void should_save_message() {
		Message message = new Message(VALID_SENDER, VALID_RECEIVER, "Subject", "Testing");
		messageService.saveMessage(message);
		assertNotNull(message.getId());
	}
	
	@Test
	public void should_return_correct_number_of_message_count_for_the_rest_of_the_day() {
		createMessages();
		
		long count = getProbableCountOfMessagesToSendForTheRestOfTheDay(MID_NIGHT);
		assertTrue(count >= 21);
		
		count = getProbableCountOfMessagesToSendForTheRestOfTheDay(THREE_AM);
		assertTrue(count >= 19);
		
		count = getProbableCountOfMessagesToSendForTheRestOfTheDay(NINE_AM);
		assertTrue(count >= 17);
		
		count = getProbableCountOfMessagesToSendForTheRestOfTheDay(THREE_PM);
		assertTrue(count >= 13);
		
		count = getProbableCountOfMessagesToSendForTheRestOfTheDay(NINE_PM);
		assertTrue(count >= 5);
		
	}
	
	@Test
	public void should_return_correct_number_of_message_count_for_the_rest_of_the_week() {
		createMessages();
		
		long count = getProbableCountOfMessagesToSendForTheRestOfTheWeek(MID_NIGHT);
		assertTrue(count >= 21);
		
		count = getProbableCountOfMessagesToSendForTheRestOfTheWeek(THREE_AM);
		assertTrue(count >= 19);
		
		count = getProbableCountOfMessagesToSendForTheRestOfTheWeek(NINE_AM);
		assertTrue(count >= 17);
		
		count = getProbableCountOfMessagesToSendForTheRestOfTheWeek(THREE_PM);
		assertTrue(count >= 13);
		
		count = getProbableCountOfMessagesToSendForTheRestOfTheWeek(NINE_PM);
		assertTrue(count >= 5);
		
	}
	
	@Test
	public void should_return_messages_received_by_user() {
		String subject_1 = "Received Message1";
		String subject_2 = "Received Message2";
		Message message1 = new Message(VALID_SENDER, VALID_RECEIVER, subject_1, "This is message is recieved by "+VALID_RECEIVER);
		messageService.saveMessage(message1);
		Message message2 = new Message(VALID_SENDER, VALID_RECEIVER, subject_2, "This is message is also recieved by "+VALID_RECEIVER);
		messageService.saveMessage(message2);
		Message message3 = new Message(VALID_RECEIVER, VALID_SENDER, "Sent Message3", "This is message is also recieved by "+VALID_SENDER);
		messageService.saveMessage(message3);
		List<Message> receivedMessages = messageService.getMessagesReceivedByEmail(VALID_RECEIVER);
		assertEquals(receivedMessages.size(), 2);
		assertTrue(receivedMessages.stream().filter(m -> m.getSubject().equals(subject_1)).findFirst().isPresent());
		assertTrue(receivedMessages.stream().filter(m -> m.getSubject().equals(subject_2)).findFirst().isPresent());
	}
	
	@Test
	public void should_return_messages_sent_by_user() {
		String subject_1 = "Received Message1";
		String subject_2 = "Received Message2";
		Message message1 = new Message(VALID_SENDER, VALID_RECEIVER, subject_1, "This is message is recieved by "+VALID_RECEIVER);
		messageService.saveMessage(message1);
		Message message2 = new Message(VALID_SENDER, VALID_RECEIVER, subject_2, "This is message is also recieved by "+VALID_RECEIVER);
		messageService.saveMessage(message2);
		Message message3 = new Message(VALID_RECEIVER, VALID_SENDER, "Sent Message3", "This is message is also recieved by "+VALID_SENDER);
		messageService.saveMessage(message3);
		List<Message> sentMessages = messageService.getMessagesSentByEmail(VALID_SENDER);
		assertEquals(sentMessages.size(), 2);
		assertTrue(sentMessages.stream().filter(m -> m.getSubject().equals(subject_1)).findFirst().isPresent());
		assertTrue(sentMessages.stream().filter(m -> m.getSubject().equals(subject_2)).findFirst().isPresent());
	}
	
	@Test
	public void should_be_able_to_read_a_message() {
		String subject = "Received Message1";
		String content = "This is message is recieved by "+VALID_RECEIVER;
		Message message = new Message(VALID_SENDER, VALID_RECEIVER, subject, content);
		messageService.saveMessage(message);
		String idOfSavedMessage = message.getId();
		Message savedMessage = messageService.getMessageById(idOfSavedMessage);
		assertEquals(savedMessage.getSubject(), subject);
		assertEquals(savedMessage.getContent(), content);
	}
	
	private long getProbableCountOfMessagesToSendForTheRestOfTheDay(int hour) {
		ZonedDateTime rightNow = createZonedDateAndTime(hour);
		return messageService.probableCountOfMessagesToSendForTheRestOfTheDay(rightNow);
	}
	
	private long getProbableCountOfMessagesToSendForTheRestOfTheWeek(int hour) {
		ZonedDateTime rightNow = createZonedDateAndTime(hour);
		return messageService.probableCountOfMessagesToSendForTheRestOfTheDay(rightNow);
	}
	
	private ZonedDateTime createZonedDateAndTime(int hour) {
		ZonedDateTime rightNow = ZonedDateTime.now();
		return ZonedDateTime.of(rightNow.getYear(), rightNow.getMonthValue(), rightNow.getDayOfMonth(), hour, 0, 0, 0, rightNow.getZone());
	}
	
	private void createMessages() {
		ZonedDateTime rightNow = ZonedDateTime.now();
		ZonedDateTime aWeekBack = rightNow.minusDays(7);
		ZonedDateTime startingDate = aWeekBack.toLocalDate().atStartOfDay().atZone(aWeekBack.getZone());
		for(int i = 0; i< numberOFmsgs.length; i++) {
			int inclusiveBeginningHour = i * 6;
			int exclusiveEndingHour = inclusiveBeginningHour + 6;
			for (int j = 0; j < numberOFmsgs[i]; j++) {
				int hour = generateRandomNumber(inclusiveBeginningHour,exclusiveEndingHour);
				int day = generateRandomNumber(0,6);
				ZonedDateTime messageDate = ZonedDateTime.of(startingDate.getYear(), startingDate.getMonthValue(), startingDate.plusDays(day).getDayOfMonth(), hour, 0, 0, 0, rightNow.getZone());
				Message message = new Message(VALID_SENDER, VALID_RECEIVER, "Subject"+i+j, "Cont"+i+j, Date.from(messageDate.toInstant()));
				messageService.saveMessage(message);
			}
		}
	}
	
	private void createSenderAndUser(String senderEmail, String receverEmail) {
		User sender = new User(senderEmail, "Mukthi", "Nath");
		User receiver = new User(receverEmail, "Cindrella", "Samuel");
		userService.save(sender);
		userService.save(receiver);
	}
	
	private int generateRandomNumber(int min, int max) {
		Random random = new Random();
		return random.ints(min, max).findAny().getAsInt();
	}
	
	private void deleteUsers() {
		userService.deleteByEmailId(VALID_SENDER);
		userService.deleteByEmailId(VALID_RECEIVER);
	}
	
	private void deleteAllMessages() {
		messageService.deleteMessageBySender(VALID_SENDER);
		messageService.deleteMessageBySender(VALID_RECEIVER);
	}
	
	
}
