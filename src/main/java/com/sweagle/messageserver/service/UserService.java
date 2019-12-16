package com.sweagle.messageserver.service;

import java.util.List;

import com.sweagle.messageserver.entity.User;

public interface UserService {
	
	User save(User user);
	
	User findByEmailId(String email);
	
	List<User> findAll();
	
	void deleteByEmailId(String email);

}
