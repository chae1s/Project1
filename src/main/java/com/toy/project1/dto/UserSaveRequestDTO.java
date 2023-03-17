package com.toy.project1.dto;


import com.toy.project1.domain.AuthId;
import com.toy.project1.domain.Role;
import com.toy.project1.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSaveRequestDTO {
	
	private Long id;
	private String email;
	private String password;
	private String name;
	private String nickname;
	private AuthId authId;
	
	public void encryptPassword(String BCryptpassword) {
		this.password = BCryptpassword;
	}
	
	
	@Builder
	public UserSaveRequestDTO(Long id, String email, String password, String name, String nickname, AuthId authId) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.authId = authId;
	}
	
	public User toEntity() {
		return User.builder()
				.id(id)
				.email(email)
				.password(password)
				.name(name)
				.nickname(nickname)
				.profile_image("profile.png")
				.authId(authId)
				.enabled(true)
				.role(Role.ROLE_USER)
				.build();
	}

}
