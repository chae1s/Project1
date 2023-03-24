package com.toy.project1.dto;

import java.util.ArrayList;
import java.util.List;


import com.toy.project1.domain.Diary;
import com.toy.project1.domain.DiaryFiles;
import com.toy.project1.domain.User;

import lombok.Getter;

@Getter
public class DiaryResponseDTO {
	
	private Long id;
	private String title;
	private User user;
	private String contents;
	private String createdDate;
	private List<DiaryFiles> diaryFiles = new ArrayList<>();
	
	
	public DiaryResponseDTO(Diary diary) {
		this.id = diary.getId();
		this.title = diary.getTitle();
		this.user = diary.getUser();
		this.contents = diary.getContents();
		this.createdDate =	diary.getCreatedDate();
		this.diaryFiles = diary.getDiaryFiles();
	}
	
	public DiaryResponseDTO(Diary diary, String regTag) {
		this.id = diary.getId();
		this.id = diary.getId();
		this.title = diary.getTitle();
		this.user = diary.getUser();
		this.contents = diary.getContents().replaceAll(regTag, "");
		this.createdDate =	diary.getCreatedDate();
		this.diaryFiles = diary.getDiaryFiles();
	}
	

}
