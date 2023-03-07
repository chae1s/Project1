package com.toy.project1.controller;


import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.toy.project1.domain.User;
import com.toy.project1.dto.UserSaveRequestDTO;
import com.toy.project1.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private UserRepository userRepository;

	@After
	public void tearDown() throws Exception {
		userRepository.deleteAll();
	}
	
	@Test
	public void object() throws Exception {
		//given
		String email = "test01@mail.com";
		UserSaveRequestDTO userDTO = UserSaveRequestDTO.builder()
				.email(email)
				.password("test1230")
				.name("김테스트")
				.nickname("조미김")
				.build();
		
		String url = "http://localhost/users/join";

		//when
		ResponseEntity<User> responseEntity = restTemplate.postForEntity(url, userDTO, User.class);
		
		Optional<User> users = userRepository.findByEmail(email);
		User user = users.get();
		
		//then
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		Assertions.assertThat(responseEntity.getBody()).isEqualTo(userDTO.getId());
		
		Assertions.assertThat(users).isNotEmpty();
		Assertions.assertThat(user.getEmail()).isEqualTo(email);
		
	}

}
