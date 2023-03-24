package com.toy.project1.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.toy.project1.domain.Diary;
import com.toy.project1.domain.SessionUser;
import com.toy.project1.domain.User;
import com.toy.project1.dto.DiarySaveRequestDTO;
import com.toy.project1.repository.DiaryRepository;
import com.toy.project1.repository.UserRepository;
import com.toy.project1.service.DiaryService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiaryControllerTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	DiaryRepository diaryRepository;
	
	@Autowired
	DiaryService diaryService;
	
	@Autowired
	HttpSession session;

	@Test
	public void save_diary() throws Exception {
		//given
		User user = userRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		
		SessionUser sessionUser = new SessionUser(user);
		
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		DiarySaveRequestDTO diaryDTO = DiarySaveRequestDTO.builder()
				.user(user)
				.title("안녕하세요")
				.contents("여행일기 입니다.")
				.build();
		
		//when
		diaryService.save(files, diaryDTO, sessionUser);
		List<Diary> diaries = diaryRepository.findByUserId(user.getId());
		for(Diary diary : diaries) {
			System.out.println(diary.getTitle());
		}
		
		//then
		
		Assertions.assertThat(diaries).isNotEmpty();
//		Assertions.assertThat(diary.getTitle()).isEqualTo("안녕하세요");

	}

}