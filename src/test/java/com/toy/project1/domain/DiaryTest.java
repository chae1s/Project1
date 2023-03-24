package com.toy.project1.domain;

import static org.junit.Assert.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.toy.project1.repository.DiaryRepository;
import com.toy.project1.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiaryTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	DiaryRepository diaryRepository;

	@Test
	public void insertDiary() throws Exception {
		//given
		String title = "trip title";
		User user = userRepository.findById(21L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		Diary diary = Diary.builder()
				.title(title)
				.contents("trip contents")
				.user(user)
				.build();

		//when
		diaryRepository.save(diary);
		List<Diary> diaries = diaryRepository.findAll();
		Diary findDiary = diaries.get(0);

		//then
		Assertions.assertThat(findDiary.getTitle()).isEqualTo(title);
		Assertions.assertThat(findDiary.getUser().getId()).isEqualTo(21L);
	}

}
