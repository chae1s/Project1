package com.toy.project1.domain;

import org.springframework.security.core.authority.AuthorityUtils;

import lombok.Getter;

@Getter
public class MyUserPrincipal extends org.springframework.security.core.userdetails.User {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String email;
	private User user;
	
	public MyUserPrincipal(User user) {
		super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
		
		this.id = user.getId();
		this.email = user.getEmail();
		this.user = user;
	}
}
