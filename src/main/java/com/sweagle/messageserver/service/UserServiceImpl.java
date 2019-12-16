package com.sweagle.messageserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweagle.messageserver.entity.User;
import com.sweagle.messageserver.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User save(User user) {
		userRepository.save(user);
		return user;
	}

	@Override
	public User findByEmailId(String email) {
		return userRepository.findByEmailId(email);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void deleteByEmailId(String email) {
		userRepository.deleteByEmailId(email);
	}

}
