package com.sweagle.messageserver.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sweagle.messageserver.entity.CachedData;
import com.sweagle.messageserver.entity.Message;
import com.sweagle.messageserver.entity.User;
import com.sweagle.messageserver.repository.MessageRepository;
import com.sweagle.messageserver.service.MessageServiceImpl;
import com.sweagle.messageserver.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootApplication(exclude = {
	    MongoAutoConfiguration.class, 
	    MongoDataAutoConfiguration.class
	})
public class MessageServiceTest {
	
	private static String senderEmail = "sender@abc.com";
	private static String receiverEmail = "receiver@abc.com";
	
	@InjectMocks
	private MessageServiceImpl messageService;
	
	@Mock
	private MessageRepository messageRepository;
	
	@Mock
	private UserService userService;
	
	@Mock
	private CachedData cachedData;
	
	@Test
	public void should_fail_when_message_is_saved_with_invalid_user() {
		Message message = Mockito.mock(Message.class);
		when(message.getSender()).thenReturn(senderEmail);
		when(message.getReceiver()).thenReturn(receiverEmail);
		when(userService.findByEmailId(Mockito.anyString())).thenReturn(null);
		assertThrows(IllegalArgumentException.class, () -> {
			messageService.saveMessage(message);
	    });
	}
	
	@Test
	public void should_pass_when_message_is_valid() {
		Message message = Mockito.mock(Message.class);
		User validUser = Mockito.mock(User.class);
		when(message.getSender()).thenReturn(senderEmail);
		when(message.getReceiver()).thenReturn(receiverEmail);
		when(message.getId()).thenReturn(null);
		when(userService.findByEmailId(Mockito.anyString())).thenReturn(validUser);
		when(messageRepository.save(message)).thenReturn(message);
		Message savedMessage = messageService.saveMessage(message);
		assertEquals(savedMessage, message);
	}
	
	@Test
	public void should_fail_while_saving_a_message_with_id() {
		Message message = Mockito.mock(Message.class);
		User validUser = Mockito.mock(User.class);
		when(message.getSender()).thenReturn(senderEmail);
		when(message.getReceiver()).thenReturn(receiverEmail);
		when(message.getId()).thenReturn("some_id");
		when(userService.findByEmailId(Mockito.anyString())).thenReturn(validUser);
		assertThrows(IllegalArgumentException.class, () -> {
			messageService.saveMessage(message);
	    });
	}
	
	@Test
	public void should_return_zero_as_count_when_message_is_empty() {
		when(cachedData.getBucketMap()).thenReturn(new HashMap<>());
		when(messageRepository.countOfMessagesFromDateTime(Mockito.any(Date.class))).thenReturn((long) 0);
		long count = messageService.getProbableCountOfMessagesToSendForTheRestOfTheDay(ZonedDateTime.now());
		assertEquals(count, 0);
		count = messageService.getProbableCountOfMessagesToSendForTheRestOfTheWeek(ZonedDateTime.now());
		assertEquals(count, 0);
	}
	
	@Test
	public void should_return_correct_message_count_when_previous_weeks_data_is_missing_but_current_days_message_is_present() {
		ZonedDateTime rightNow = ZonedDateTime.now();
		int diff =DayOfWeek.WEDNESDAY.compareTo(rightNow.getDayOfWeek());
		ZonedDateTime wednesday = ZonedDateTime.of(rightNow.getYear(), rightNow.getMonthValue(), rightNow.plusDays(diff).getDayOfMonth(), 12, 0, 0, 0, rightNow.getZone());
		when(cachedData.getBucketMap()).thenReturn(new HashMap<>());
		when(messageRepository.countOfMessagesFromDateTime(Mockito.any(Date.class))).thenReturn((long) 1);
		long count = messageService.getProbableCountOfMessagesToSendForTheRestOfTheDay(wednesday);
		assertEquals(count, 1);
		count = messageService.getProbableCountOfMessagesToSendForTheRestOfTheWeek(wednesday);
		assertEquals(count, 9);
	}
	
	

}
