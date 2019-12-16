package com.sweagle.messageserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sweagle.messageserver.entity.Message;

public interface MessageRepository extends MongoRepository<Message, String>, CustomMessageRepository {
	
	public Message save(Message message);
	
	public List<Message> findMessagesBySender(String email);
	
	public List<Message> findMessagesByReceiver(String email);
	
	public Optional<Message> findById(String id);
	
	public long deleteBySender(String sender);
	
	//public List<Message> getMessageByReceiverId(String id);
	
	
}
