package com.toy.project1.dto;

import com.toy.project1.domain.Comment;
import com.toy.project1.domain.Diary;
import com.toy.project1.domain.User;

import lombok.Getter;

@Getter
public class CommentResponseDTO {
	
	private Long id;
	private String content;
	private String createdDate;
	private User user;
	private Diary diary;
	
	public CommentResponseDTO(Comment comment) {
		this.id = comment.getId();
		this.content = comment.getContent();
		this.createdDate = comment.getCreatedDate();
		this.user = comment.getUser();
		this.diary = comment.getDiary();
	}

}
