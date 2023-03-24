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

@Entity
@Getter
@NoArgsConstructor
public class DiaryFiles {
	
	@Id @GeneratedValue
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DIARY_ID")
	private Diary diary;
	private String fileName;
	private String oriName;
	
	@Builder
	public DiaryFiles(Diary diary, String fileName, String oriName) {
		this.diary = diary;
		this.fileName = fileName;
		this.oriName = oriName;
	}
	

}
