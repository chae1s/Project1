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
	private String profile_image;
	private String introduce;
	
	@Builder
	public UserUpdateRequestDTO(String nickname, String profile_image, String introduce) {
		this.nickname = nickname;
		this.profile_image = profile_image;
		this.introduce = introduce;
	}
	
	@Builder
	public UserUpdateRequestDTO(String password) {
		this.password = password;
	}

}
