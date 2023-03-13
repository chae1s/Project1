package com.toy.project1.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class Email {
	
	
	/**
	 * to : 메일 수신자
	 * subject : 메일 제목
	 * message : 메일 내용
	 */
	
	private String to;
	private String subject;
	private String message;

}
