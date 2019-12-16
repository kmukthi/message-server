package com.sweagle.messageserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sweagle.messageserver.entity.User;
import com.sweagle.messageserver.service.UserService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/user")
@Api(value = "Operations pertaining to users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(path ="/save", method = RequestMethod.POST)
	public User saveUser(@RequestBody User user) {
		return userService.save(user);
	}
	
	@RequestMapping(path = "/getById", method = RequestMethod.GET)
	public User getUserById(@RequestParam("email") String email) {
		return userService.findByEmailId(email);
	}
	
	@RequestMapping(path = "/findAll", method = RequestMethod.GET)
	public List<User> findAll() {
		return userService.findAll();
	}
	
	@RequestMapping(path = "/deleteUser", method = RequestMethod.DELETE)
	public void deleteUser(@RequestParam("email") String email) {
		userService.deleteByEmailId(email);
	}
	
}
