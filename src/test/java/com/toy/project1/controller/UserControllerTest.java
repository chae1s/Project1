package com.toy.project1.controller;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

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
		for(int i=1;i<=20;i++) {
			String email = "user" + i + "@mail.com";
			
			UserSaveRequestDTO userDTO = UserSaveRequestDTO.builder()
					.email(email)
					.password("user1230")
					.name("김사용")
					.nickname("새우깡"+i)
					.build();
			
			//when
			userService.join(userDTO);
		}
		
//		Optional<User> users = userRepository.findByEmail(email);
//		User user = users.get();
		
		//then
	
//		Assertions.assertThat(users).isNotEmpty();
//		Assertions.assertThat(user.getEmail()).isEqualTo(email);
		
	}
	
	@Test
	public void edit() throws Exception {
		//given
		String [] nicknames = {"나사람됐다", "짱이지", "이손을봐", "대박임", "삐죽",
							   "나한테집중해", "크르릉", "찰떡콩떡", "사과떡", "고롱고로롱", 
							   "뚜루루룻", "왕크왕귀", "망곰", "고심이", "춤춰주세요", 
							   "알빠임", "루앤율", "최강마루", "임주스씨", "미안사탕줄게"};
		
		for(int i=1;i<=20;i++) {
			UserUpdateRequestDTO updateRequestDTO = UserUpdateRequestDTO.builder()
									.nickname(nicknames[i-1])
									.profile_image("profile"+i+".jpg")
									.introduce("안녕하세요. 저는 "+nicknames[i-1]+" 입니다.")
									.build();
			
			userService.edit((long)(i+1), updateRequestDTO);
		}
		
	}

}
