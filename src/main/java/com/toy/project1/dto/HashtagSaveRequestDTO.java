package com.toy.project1.dto;

import com.toy.project1.domain.Hashtag;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HashtagSaveRequestDTO {
	
	private String hashtag;
	
	@Builder
	public HashtagSaveRequestDTO(String hashtag) {
		this.hashtag = hashtag;
	}
	
	public Hashtag toEntity() {
		return Hashtag.builder()
				.hashtag(hashtag)
				.build();
	}
	
}
