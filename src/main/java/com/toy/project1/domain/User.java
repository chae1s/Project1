package com.toy.project1.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class User {
	
	@Id @GeneratedValue
	private Long id;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	private String name;
	@Column(unique = true)
	private String nickname;
	private String fileName;
	@Lob
	private String introduce;
	private boolean enabled;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	@Builder
	public User(Long id, String email, String password, String name, String nickname, String fileName, String introduce, boolean enabled, Role role) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.fileName = fileName;
		this.introduce = introduce;
		this.enabled = enabled;
		this.role = role;
	}
	
	public void update(String nickname, String fileName, String introduce) {
		this.nickname = nickname;
		this.fileName = fileName;
		this.introduce = introduce;
	}
	
	public void update(String password) {
		this.password = password;
	}
}
