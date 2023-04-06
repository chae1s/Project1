package com.toy.project1.dto;

import java.util.ArrayList;
import java.util.List;

import com.toy.project1.domain.Comment;
import com.toy.project1.domain.Diary;
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
	private List<DiaryHashtag> hashtags = new ArrayList<>();
	private List<Comment> comments = new ArrayList<>();
	private String imageUrl;
	private Long commentCount;
	
	
	public DiaryResponseDTO(Diary diary, List<DiaryHashtag> hashtags) {
		this.id = diary.getId();
		this.title = diary.getTitle();
		this.user = diary.getUser();
		this.contents = diary.getContents();
		this.createdDate =	diary.getCreatedDate();
		this.hits = diary.getHits();
		this.hashtags = hashtags;
	}
	
	public DiaryResponseDTO(Diary diary, String regTag, String imageUrl, Long commentCount) {
		this.id = diary.getId();
		this.id = diary.getId();
		this.title = diary.getTitle();
		this.user = diary.getUser();
		this.contents = diary.getContents().replaceAll(regTag, " ");
		this.hits = diary.getHits();
		this.createdDate =	diary.getCreatedDate();
		this.hashtags = diary.getHashtags();
		this.imageUrl = imageUrl;
		this.commentCount = commentCount;
	}
	
	public DiaryResponseDTO(Diary diary, String regTag, List<DiaryHashtag> hashtags, String imageUrl, Long commentCount) {
		this.id = diary.getId();
		this.id = diary.getId();
		this.title = diary.getTitle();
		this.user = diary.getUser();
		this.contents = diary.getContents().replaceAll(regTag, " ");
		this.hits = diary.getHits();
		this.createdDate =	diary.getCreatedDate();
		this.hashtags = hashtags;
		this.imageUrl = imageUrl;
		this.commentCount = commentCount;
	}
	

}
