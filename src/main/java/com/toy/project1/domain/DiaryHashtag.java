package com.toy.project1.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class DiaryHashtag {
	
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DIARY_ID")
	private Diary diary;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HASHTAG_ID")
	private Hashtag hashtag;
	
	@Builder
	public DiaryHashtag(Long id, Diary diary, Hashtag hashtag) {
		this.id = id;
		this.diary = diary;
		this.hashtag = hashtag;
	}

}


