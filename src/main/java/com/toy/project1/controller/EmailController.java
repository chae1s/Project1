package com.toy.project1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toy.project1.domain.EmailMessage;
import com.toy.project1.service.EmailService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/send-mail")
@RequiredArgsConstructor
public class EmailController {
	
	private final EmailService emailService;
	
	@PostMapping("/email")
	@ResponseBody
	public String sendJoinMail(@RequestParam("email") String email) throws Exception {
		
		EmailMessage emailMessage = EmailMessage.builder()
				.to(email)
				.subject("[여행일기] 이메일 인증을 위한 인증 번호 발송")
				.build();
		
		String code = emailService.sendMail(emailMessage, "email");
		
		return code;
	}
	
	@PostMapping("/password")
	public String sendPasswordMail(String email) throws Exception {
		EmailMessage emailMessage = EmailMessage.builder()
				.to(email)
				.subject("[여행일기] 임시 비밀번호 발급")
				.build();
		
		emailService.sendMail(emailMessage, "password");
		
		return "redirect:/users/login";
	}

}
