package com.toy.project1.dto;

import com.toy.project1.domain.Diary;
import com.toy.project1.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiarySaveRequestDTO {
	
	private User user;
	private String title;
	private String contents;
	
	@Builder
	public DiarySaveRequestDTO(User user, String title, String contents) {
		this.user = user;
		this.title = title;
		this.contents = contents;
	}
	
	public Diary toEntity() {
		return Diary.builder()
				.user(user)
				.title(title)
				.contents(contents)
				.hits(0)
				.build();
	}

}
