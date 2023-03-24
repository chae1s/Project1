package com.toy.project1.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {
	
	@Id @GeneratedValue
	private Long id;
	@Column(nullable = false, unique = true)
	private String email;
	private String password;
	private String name;
	private String nickname;
	private String profile_image;
	@Enumerated(EnumType.STRING)
	private AuthId authId;
	@Lob
	private String introduce;
	private boolean enabled;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	@OneToMany(mappedBy = "user")
	private List<Diary> diaries = new ArrayList<Diary>();
	
	
	@Builder
	public User(Long id, String email, String password, String name, String nickname, String profile_image, AuthId authId,String introduce, boolean enabled, Role role) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.profile_image = profile_image;
		this.authId = authId;
		this.introduce = introduce;
		this.enabled = enabled;
		this.role = role;
	}
	
	public void update(String nickname, String profile_image, String introduce) {
		this.nickname = nickname;
		this.profile_image = profile_image;
		this.introduce = introduce;
	}
	
	public void update(String password) {
		this.password = password;
	}
}
