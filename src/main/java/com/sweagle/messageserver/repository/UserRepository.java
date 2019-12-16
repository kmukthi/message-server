package com.sweagle.messageserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sweagle.messageserver.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

	public User findByFirstName(String firstName);
	
	public List<User> findByLastName(String firstName);
	
	public Optional<User> findById(String id);
	
	public User findByEmailId(String email);
	
	public User save(User user);
	
	public List<User> findAll();
	
	public void deleteByEmailId(String email);
}
