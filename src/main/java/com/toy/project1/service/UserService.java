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
	
<<<<<<< HEAD
	@Transactional
	public void edit(Long id, UserUpdateRequestDTO userDTO) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		System.out.println(userDTO.getIntroduce());
		user.update(userDTO.getNickname(), user.getFileName(), userDTO.getIntroduce());
	}
	
	@Transactional
	public String editFile(Long id, UserUpdateRequestDTO userDTO, MultipartFile file) throws Exception {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		
		String fileName = user.getFileName();
		if(file != null && !file.isEmpty()) {
			fileName = fileHandler.fileUpload("images/upload/user", file);
		}
		
		user.update(user.getNickname(), fileName, user.getIntroduce());
		return fileName;
	}
	
	@Transactional
	public boolean deleteFile(Long id, UserUpdateRequestDTO userDTO) throws Exception {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		boolean result = fileHandler.fileDelete("images/upload/user", user.getFileName());
		
		if(result) {
			user.update(user.getNickname(), "profil.png", user.getIntroduce());
		}
		
		return result;
	}
	
	@Transactional
	public void tempPW(String email, String authNum) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		
		user.update(encoder.encode(authNum));
	}
	
	@Transactional
	public int changePw(Long id, String prevPassword, UserUpdateRequestDTO userDTO) throws Exception {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		
		/*
		 * 이전 비밀번호와 현재 비밀번호가 다를 때 check = 1s
		 */
		int check = 1;
		if(encoder.matches(prevPassword, user.getPassword())) {
			if(encoder.matches(userDTO.getPassword(), user.getPassword())) {
				//이전 비밀번호와 새 비밀번호가 같으면 check = 2;
				check = 2;
			} else {
				/*
				 * 이전 비밀번호가 현재 비밀번호와 일치하고
				 * 이전 비밀번호와 새 비밀번호가 다를 때 check = 0
				 * check = 0일 때 비밀번호 변경 성공
				 */
				user.update(encoder.encode(userDTO.getPassword()));
				check = 0;
			}
		}
		
		return check;
	}
	
	@Transactional
	public void delete(Long id) throws Exception {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		if(!user.getFileName().equals("profil.png")) {			
			fileHandler.fileDelete("images/upload/user", user.getFileName());
		}
		userRepository.delete(user);
	}
	
	
=======
>>>>>>> parent of f3529ad (회원정보 수정, 비밀번호 변경, 탈퇴)
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
