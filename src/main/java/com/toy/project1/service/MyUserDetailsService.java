package com.toy.project1.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.toy.project1.domain.MyUserPrincipal;
import com.toy.project1.domain.SessionUser;
import com.toy.project1.domain.User;
import com.toy.project1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
	
	
	private final UserRepository userRepository;
	private final HttpSession session;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByEmail(username);
		
		if(!user.isPresent()) {
			throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
		}
		
		session.setAttribute("user", new SessionUser(user.get()));
		
		return new MyUserPrincipal(user.get());
	}

}
