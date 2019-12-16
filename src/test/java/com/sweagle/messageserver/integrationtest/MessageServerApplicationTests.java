package com.sweagle.messageserver.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweagle.messageserver.controller.MessageController;
import com.sweagle.messageserver.entity.Message;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessageServerApplicationTests {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private MessageController messageController;

	@Test
	public void contextLoads() throws Exception {
	}
	
//	@Test
//	public void thisIsJustAtest() throws Exception {
//		MvcResult s = mockMvc.perform(get("/message/getSentMessage?id=a")).andExpect(status().isOk()).andDo(print()).andReturn();
//		List<Message> list = mapper.readValue(s.getResponse().getContentAsString(), List.class);
//		assertThat(list).isEmpty();
//	}
//	
//	@Test
//	public void blaaa() throws Exception {
//		List<Message> messages = messageController.getSentMessages("a");
//		assertThat(messages).isEmpty();
//	}
	
//	@Test 
//	public void should_save_message() throws Exception {
//		Message message = new Message("kmukthi@gmail.com", "cindrellasamuel3@gmail.com", "integration-test", "Just like that");
//		assertThat(message.getId()).isNull();
//		messageController.sendMessage(message);
//		assertThat(message.getId()).isNotNull();
//	}

}
