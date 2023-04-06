package com.toy.project1.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

	@Id @GeneratedValue
	private Long id;
	private String content;
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;
	@ManyToOne
	@JoinColumn(name = "DIARY_ID")
	private Diary diary;
	
	@Builder
	public Comment(Long id, String content, User user, Diary diary) {
		this.id = id;
		this.content = content;
		this.user = user;
		this.diary = diary;
	}
	
	public void update(String content) {
		this.content = content;
	}
}
