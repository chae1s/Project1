package com.toy.project1.domain;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.toy.project1.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Test
	public void insertUser() throws Exception {
		//given
		User user = User.builder()
				.email("admin@trip.com")
				.password(encoder.encode("admin1230"))
				.name("관리자")
				.nickname("관리자")
				.enabled(true)
				.role(Role.ROLE_ADMIN)
				.build();
		//when
		userRepository.save(user);

		//then
		List<User> users = userRepository.findAll();
		User user1 = users.get(0);
		
		Assertions.assertThat(user1.getEmail()).isEqualTo("admin@trip.com");
	}

}
