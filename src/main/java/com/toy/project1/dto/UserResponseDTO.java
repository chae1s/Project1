package com.toy.project1.dto;

import com.toy.project1.domain.User;

import lombok.Getter;

@Getter
public class UserResponseDTO {
	
	private Long id;
	private String email;
	private String name;
	private String nickname;
	private String fileName;
	private String introduce;
	
	public UserResponseDTO(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.name = user.getName();
		this.nickname = user.getNickname();
		this.fileName = user.getFileName();
		this.introduce = user.getIntroduce();
	}

}
