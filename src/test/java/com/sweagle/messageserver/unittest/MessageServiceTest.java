package com.sweagle.messageserver.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
		when(userService.findByEmailId(Mockito.anyString())).thenReturn(validUser);
		when(messageRepository.save(message)).thenReturn(message);
		Message savedMessage = messageService.saveMessage(message);
		assertEquals(savedMessage, message);
	}
	
	

}
