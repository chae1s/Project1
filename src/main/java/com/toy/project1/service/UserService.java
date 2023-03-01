package com.toy.project1.service;

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

}
