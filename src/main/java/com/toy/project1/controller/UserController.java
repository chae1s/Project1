package com.toy.project1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.toy.project1.domain.User;
import com.toy.project1.dto.UserSaveRequestDTO;
import com.toy.project1.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/join")
	public String join() throws Exception {
		return "user/join";
	}
	
	@PostMapping("/join")
	public String join(UserSaveRequestDTO userDTO) throws Exception {
		
		userService.join(userDTO);
		
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() throws Exception {
		return "user/login";
	}
}
