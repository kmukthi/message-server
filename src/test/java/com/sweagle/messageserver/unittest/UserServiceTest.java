package com.sweagle.messageserver.unittest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sweagle.messageserver.entity.User;
import com.sweagle.messageserver.service.UserServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@Test
	public void should_fail_while_saving_a_user_with_id() {
		User user = Mockito.mock(User.class);
		when(user.getId()).thenReturn("some_id");
		assertThrows(IllegalArgumentException.class, () -> {
			userService.save(user);
	    });
	}
	
	
}
