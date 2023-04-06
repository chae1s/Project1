package com.toy.project1.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Diary extends BaseTimeEntity {
	
	@Id @GeneratedValue
	private Long id;
	private String title;
	@Lob
	private String contents;
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;
	private Integer hits;
	@ToString.Exclude
	@OneToMany(mappedBy = "diary")
	private List<DiaryHashtag> hashtags = new ArrayList<>();
	@ToString.Exclude
	@OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<Comment> comments = new ArrayList<Comment>();
	
	
	
	@Builder
	public Diary(Long id, String title, String contents, User user, Integer hits) {
		this.id = id;
		this.title = title;
		this.contents = contents;
		this.user = user;
		this.hits = hits;
	}
	
	public void updateHits() {
		this.hits += 1;
	}
	
	public void update(String title, String contents) {
		this.title = title;
		this.contents = contents;
	}
	
}
