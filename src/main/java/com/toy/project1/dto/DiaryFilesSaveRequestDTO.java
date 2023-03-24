package com.toy.project1.dto;

import com.toy.project1.domain.Diary;
import com.toy.project1.domain.DiaryFiles;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiaryFilesSaveRequestDTO {
	
	private Diary diary;
	private String fileName;
	private String oriName;
	
	@Builder
	public DiaryFilesSaveRequestDTO(Diary diary, String fileName, String oriName) {
		this.diary = diary;
		this.fileName = fileName;
		this.oriName = oriName;
	}
	
	public DiaryFiles toEntity() {
		return DiaryFiles.builder()
				.diary(diary)
				.fileName(fileName)
				.oriName(oriName)
				.build();
	}

}
