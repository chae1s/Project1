package com.toy.project1.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.toy.project1.domain.User;
import com.toy.project1.dto.UserResponseDTO;
import com.toy.project1.dto.UserSaveRequestDTO;
import com.toy.project1.dto.UserUpdateRequestDTO;
import com.toy.project1.handler.FileHandler;
import com.toy.project1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder encoder;
	
	private final FileHandler fileHandler;
	
	@Transactional
	public void join(UserSaveRequestDTO userDTO) {
		userDTO.encryptPassword(encoder.encode(userDTO.getPassword()));
		
		User user = userDTO.toEntity();
		
		userRepository.save(user);
		
	}
	
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
	
	
	public int emailCheck(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		int check = 1;
		
		System.out.println(isEmail(email));
		if(user.isEmpty()) {
			if(isEmail(email)) {				
				check = 0;
			} else {
				check = 2;
			}
		}
		
		return check;
	}
	
	//회원가입에서의 닉네임 중복 및 길이 확인
	public int nicknameCheck(String nickname) {
		User user = userRepository.findByNickname(nickname);
		
		//변경하려는 닉네임이 중복이 없고 길이가 2이상 10이하일 때 닉네임 사용 가능 check = 0
		//변경하려는 닉네임이 이미 존재하면 check = 1 
		//변경하려는 닉네임이 중복이 없지만 길이가 제한된 길이가 아닐 때 닉네임 사용 불가능 check = 2

		int check = 1;
		if(user == null) {
			if(nickname.length() > 1 && nickname.length() < 11) {
				check = 0;
			} else {
				check = 2;
			}
		}
		
		return check;
	}
	
	//회원정보 수정에서의 닉네임 중복 및 길이 확인
	public int nicknameCheck(Long id, String nickname) {
		/*
		 * check값이 0이면 사용 가능, 
		 * 1이면 중복, 
		 * 2이면 유효성검사를 통과하지 못한 것, 
		 * 3이면 이미 내가 사용하고 있는 닉네임
		 */
		
		//변경하려는 닉네임이 이미 존재하면 check = 1 
		int check = 1;
		
		User user = userRepository.findByNickname(nickname);
		User LoginUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		
		if(user == null) {
			if(nickname.length() > 1 && nickname.length() < 11) {
				//변경하려는 닉네임이 중복이 없고 길이가 2이상 10이하일 때 닉네임 사용 가능 check = 0
				check = 0;
			} else {
				//변경하려는 닉네임이 중복이 없지만 길이가 제한된 길이가 아닐 때 닉네임 사용 불가능 check = 2
				check = 2;
			}
		} else if (LoginUser.getNickname().equals(nickname)) {
			//변경하려는 닉네임이 이미 로그인한 유저가 사용하고 있는 닉네임이면 check = 3
			check = 3;
		}
		
		return check;
	}
	
	private static boolean isEmail(String email) {
		
		return Pattern.matches("^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$", email);
	}
	
	public UserResponseDTO findById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		
		return new UserResponseDTO(user);
	}
	

}
