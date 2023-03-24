package com.toy.project1.domain;

import java.util.ArrayList;
import java.util.List;

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

@Entity
@Getter
@NoArgsConstructor
public class Diary extends BaseTimeEntity {
	
	@Id @GeneratedValue
	private Long id;
	private String title;
	@Lob
	private String contents;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;
	private Integer hits;
	@OneToMany(mappedBy = "diary")
	private List<DiaryFiles> diaryFiles = new ArrayList<DiaryFiles>();
	
	@Builder
	public Diary(Long id, String title, String contents, User user, Integer hits) {
		this.id = id;
		this.title = title;
		this.contents = contents;
		this.user = user;
		this.hits = hits;
	}
	
	public void addFiles(DiaryFiles file) {
		this.diaryFiles.add(file);
	}
	
	public void contentsTagRemove(String contents) {
		String htmlReg = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";
		this.contents = contents.replaceAll(htmlReg, "");
	}

}
