package com.toy.project1.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthId {
	
	EMAIL("이메일 로그인"),
	KAKAO("카카오 로그인"),
	NAVER("네이버 로그인"),
	GOOGLE("구글 로그인"),
	TWITTER("트위터 로그인");
	
	private final String explanation;

}
