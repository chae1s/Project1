package com.toy.project1.controller;


import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.toy.project1.domain.Role;
import com.toy.project1.domain.User;
import com.toy.project1.dto.UserSaveRequestDTO;
import com.toy.project1.dto.UserUpdateRequestDTO;
import com.toy.project1.repository.UserRepository;
import com.toy.project1.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder encoder;

	/*
	 * @After public void tearDown() throws Exception { userRepository.deleteAll();
	 * }
	 */
	
//	@Test
	public void join_admin() throws Exception {
		//given
		String email = "admin@trip.com";
		UserSaveRequestDTO userDTO = UserSaveRequestDTO.builder()
				.email(email)
				.password("admin1230")
				.name("관리자")
				.nickname("관리자")
				.build();
		
		System.out.println(userDTO.getEmail());

		//when
		userService.join(userDTO);
		
		Optional<User> users = userRepository.findByEmail(email);
		User user = users.get();
		
		//then
	
		Assertions.assertThat(users).isNotEmpty();
		Assertions.assertThat(user.getEmail()).isEqualTo(email);
		
	}
	
//	@Test
	public void join_user() throws Exception {
		//given
		String email = "user01@mail.com";
		UserSaveRequestDTO userDTO = UserSaveRequestDTO.builder()
				.email(email)
				.password("user1230")
				.name("김사용")
				.nickname("새우깡")
				.build();
		
		System.out.println(userDTO.getEmail());

		//when
		userService.join(userDTO);
		
		Optional<User> users = userRepository.findByEmail(email);
		User user = users.get();
		
		//then
	
		Assertions.assertThat(users).isNotEmpty();
		Assertions.assertThat(user.getEmail()).isEqualTo(email);
		
	}
	
//	@Test
	public void edit() throws Exception {
		//given
		User user = userRepository.save(
										User.builder()
											.email("user02@mail.com")
											.password(encoder.encode("user1230"))
											.name("박사용")
											.nickname("양파링")
											.fileName("profil.png")
											.role(Role.ROLE_USER)
											.enabled(true)
											.build()
										);
		Long updateId = user.getId();
		String editNickname = "감자깡";
		String editFileName = "profil.png";
		String editIntroduce = "맛있는 감자깡";
		
		UserUpdateRequestDTO userDTO = UserUpdateRequestDTO.builder()
				.nickname(editNickname)
				.introduce(editIntroduce)
				.build();
		
		//when
		userService.edit(updateId, userDTO);
		Optional<User> users = userRepository.findById(updateId);
		User editUser = users.get();
		
		//then
		Assertions.assertThat(editUser.getNickname()).isEqualTo(editNickname);
		Assertions.assertThat(editUser.getFileName()).isEqualTo(editFileName);
		Assertions.assertThat(editUser.getIntroduce()).isEqualTo(editIntroduce);
	}
	
}
