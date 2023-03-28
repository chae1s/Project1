package com.toy.project1.dto;

import java.util.ArrayList;
import java.util.List;

import com.toy.project1.domain.Diary;
import com.toy.project1.domain.DiaryFiles;
import com.toy.project1.domain.DiaryHashtag;
import com.toy.project1.domain.User;

import lombok.Getter;

@Getter
public class DiaryResponseDTO {
	
	private Long id;
	private String title;
	private User user;
	private String contents;
	private String createdDate;
	private Integer hits;
	private List<DiaryFiles> diaryFiles = new ArrayList<>();
	private List<DiaryHashtag> hashtags = new ArrayList<>();
	
	
	public DiaryResponseDTO(Diary diary, List<DiaryHashtag> hashtags) {
		this.id = diary.getId();
		this.title = diary.getTitle();
		this.user = diary.getUser();
		this.contents = diary.getContents();
		this.createdDate =	diary.getCreatedDate();
		this.hits = diary.getHits();
		this.diaryFiles = diary.getDiaryFiles();
		this.hashtags = hashtags;
	}
	
	public DiaryResponseDTO(Diary diary, String regTag) {
		this.id = diary.getId();
		this.id = diary.getId();
		this.title = diary.getTitle();
		this.user = diary.getUser();
		this.contents = diary.getContents().replaceAll(regTag, " ");
		this.hits = diary.getHits();
		this.createdDate =	diary.getCreatedDate();
		this.diaryFiles = diary.getDiaryFiles();
		this.hashtags = diary.getHashtags();
	}
	
	public DiaryResponseDTO(Diary diary, String regTag, List<DiaryHashtag> hashtags) {
		this.id = diary.getId();
		this.id = diary.getId();
		this.title = diary.getTitle();
		this.user = diary.getUser();
		this.contents = diary.getContents().replaceAll(regTag, " ");
		this.hits = diary.getHits();
		this.createdDate =	diary.getCreatedDate();
		this.diaryFiles = diary.getDiaryFiles();
		this.hashtags = hashtags;
	}
	

}
