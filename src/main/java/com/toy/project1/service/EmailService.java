package com.toy.project1.service;

import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.toy.project1.domain.Email;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;
	
	private final UserService userService;
	
	public String sendMail(Email email, String type) throws Exception {
		String authNum = createCode(type);
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		if(type.equals("password")) userService.tempPW(email.getTo(), authNum);
		
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
		mimeMessageHelper.setTo(email.getTo());
		mimeMessageHelper.setSubject(email.getSubject());
		mimeMessageHelper.setText(setContext(authNum, type), true);
		
		mailSender.send(mimeMessage);
		
		return authNum;
	}
	
	public String createCode(String type) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		
		//인증번호 및 임시 비밀번호 생성
		if(type.equals("password")) {
			for(int i=0;i<8;i++) {
				int index = random.nextInt(4);
				
				switch (index) {
				case 0: sb.append((char) ((int) random.nextInt(26) + 97)); break;
				case 1: sb.append((char) ((int) random.nextInt(26) + 65)); break;
				default: sb.append(random.nextInt(9));
				}
			}
		} else {			
			for(int i=0;i<6;i++) {
				sb.append(random.nextInt(9));
			}
		}
		
		return sb.toString();
	}
	
	public String setContext(String code, String type) {
		Context context = new Context();
		context.setVariable("code", code);
		
		return templateEngine.process(type, context);
	}

}
