package com.toy.project1.domain;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Hashtag {
	
	@Id @GeneratedValue
	private Long id;
	private String hashtag;
	@ToString.Exclude
	@OneToMany(mappedBy = "hashtag")
	private List<DiaryHashtag> diaries = new ArrayList<>();
	
	
	@Builder
	public Hashtag(Long id, String hashtag) {
		this.id = id;
		this.hashtag = hashtag;
	}
	
	

}
