package com.toy.project1.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.toy.project1.domain.User;
import com.toy.project1.dto.UserSaveRequestDTO;
import com.toy.project1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder encoder;
	
	@Transactional
	public void join(UserSaveRequestDTO userDTO) {
		userDTO.encryptPassword(encoder.encode(userDTO.getPassword()));
		
		User user = userDTO.toEntity();
		
		userRepository.save(user);
		
	}
	
	public int emailCheck(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		int check;
		if(user.isEmpty()) {
			if(isEmail(email)) {				
				check = 0;
			} else {
				check = 2;
			}
		} else {
			check = 1;
		}
		
		return check;
	}
	
	public int nicknameCheck(String nickname) {
		List<User> user = userRepository.findByNickname(nickname);
		int check = 1;
		if(user.isEmpty()) {
			if(nickname.length() > 1 && nickname.length() < 11) {				
				check = 0;
			} else {
				check = 2;
			}
		}
		
		return check;
	}
	
	private static boolean isEmail(String email) {
		
		return Pattern.matches("^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$", email);
	}
	

}
