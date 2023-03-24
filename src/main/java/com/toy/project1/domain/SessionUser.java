package com.toy.project1.domain;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class SessionUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String email;
	private String nickname;
	private String profile_image;
	
	public SessionUser(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.nickname = user.getNickname();
		this.profile_image = user.getProfile_image();
	}

}
