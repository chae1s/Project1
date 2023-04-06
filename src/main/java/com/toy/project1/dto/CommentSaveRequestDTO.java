package com.toy.project1.dto;

import com.toy.project1.domain.Comment;
import com.toy.project1.domain.Diary;
import com.toy.project1.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentSaveRequestDTO {
	
	private String content;
	private User user;
	private Diary diary;
	
	@Builder
	public CommentSaveRequestDTO(String content, User user, Diary diary) {
		this.content = content;
		this.user = user;
		this.diary = diary;
	}
	
	public Comment toEntity() {
		return Comment.builder()
				.content(content)
				.user(user)
				.diary(diary)
				.build();
	}

}
