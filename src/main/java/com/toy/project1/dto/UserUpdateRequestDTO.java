package com.toy.project1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDTO {
	
	private String password;
	private String nickname;
	private String fileName;
	private String introduce;
	
	@Builder
	public UserUpdateRequestDTO(String nickname, String fileName, String introduce) {
		this.nickname = nickname;
		this.fileName = fileName;
		this.introduce = introduce;
	}
	
	@Builder
	public UserUpdateRequestDTO(String password) {
		this.password = password;
	}

}
