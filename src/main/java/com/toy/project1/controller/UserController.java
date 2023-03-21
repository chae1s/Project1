package com.toy.project1.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.toy.project1.domain.AuthId;
import com.toy.project1.dto.UserResponseDTO;
import com.toy.project1.dto.UserSaveRequestDTO;
import com.toy.project1.dto.UserUpdateRequestDTO;
import com.toy.project1.service.CustomOAuth2UserService;
import com.toy.project1.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	

	private final UserService userService;
	
	private final CustomOAuth2UserService customOAuth2UserService;
	
	@GetMapping("/join")
	public String join() throws Exception {
		return "user/join";
	}
	
	@PostMapping("/join")
	public String join(UserSaveRequestDTO userDTO) throws Exception {
		
		userService.join(userDTO);
		
		return "redirect:/";
	}
	
	
	@PostMapping("/emailCheck")
	@ResponseBody
	public int emailCheck(@RequestParam("email") String email) {
		int check = userService.emailCheck(email);
		
		return check;
	}
	
	@PostMapping("/nicknameCheck")
	@ResponseBody
	public int nicknameCheck(@RequestParam("nickname") String nickname) {
		int check = userService.nicknameCheck(nickname);
		
		return check;
	}

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, Model model) throws Exception {
		
		model.addAttribute("error", error);
		
		return "user/login";
	}
	
	@GetMapping("/login/oauth2/code/kakao")
	public String login(@RequestParam(required = false) String code) throws Exception {
		
		String access_token = customOAuth2UserService.getKakaoAccessToken(code);
		customOAuth2UserService.saveKakao(access_token);
		System.out.println(code);
		
		return "redirect:/";
	}
	
	@GetMapping("/login/oauth2/code/naver")
	public String loginNaver(@RequestParam(required = false) String code, @RequestParam(required = false)String state) throws Exception {

		String access_token = customOAuth2UserService.getNaverAccessToken(code, state);
		customOAuth2UserService.saveNaver(access_token);
		
		return "redirect:/";
	}
	
	@GetMapping("/findPassword")
	public String findPw() throws Exception {
		return "user/passwordFind";
	}
	
	@GetMapping("/{id}")
	public ModelAndView myPage(@PathVariable Long id, Principal principal) throws Exception {
		ModelAndView mv = new ModelAndView();
		UserResponseDTO userDTO = userService.findById(id);
		
		mv.setViewName("user/myPage");
		mv.addObject("userDTO", userDTO);
		
		return mv;
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView edit(@PathVariable Long id, Principal principal) throws Exception {
		ModelAndView mv = new ModelAndView();
		UserResponseDTO userDTO = userService.findById(id);
		if(!principal.getName().contains(userDTO.getEmail())) {
			throw new IllegalArgumentException("이 페이지 이용 불가");
		}
		mv.addObject("userDTO", userDTO);
		mv.setViewName("user/edit");
		
		return mv;
	}
	
	@PutMapping("/edit/{id}")
	public String edit(@PathVariable Long id, UserUpdateRequestDTO userDTO) {
		userService.edit(id, userDTO);
		
		return "redirect:/users/edit/"+id;
	}
	
	@PostMapping("/edit/nicknameCheck/{id}")
	@ResponseBody
	public int nicknameCheck(@PathVariable Long id, String nickname) {
		int check = userService.nicknameCheck(id, nickname);
		
		return check;
	}
	
	@PutMapping("/edit/file/{id}")
	@ResponseBody
	public String editFile(@PathVariable Long id, UserUpdateRequestDTO userDTO, MultipartFile file) throws Exception {
		String fileName = userService.editFile(id, userDTO, file);
		
		return fileName;
	}
	
	@PutMapping("/edit/deleteFile/{id}")
	@ResponseBody
	public boolean deleteFile(@PathVariable Long id, UserUpdateRequestDTO userDTO) throws Exception {
		boolean result = userService.deleteFile(id, userDTO);
		
		return result;
	}
	
	@GetMapping("/edit/password/{id}")
	public String changePw(@PathVariable Long id) {
		
		return "user/passwordChange";
	}
	
	@PostMapping("/edit/password/{id}")
	@ResponseBody
	public int changePw(@PathVariable Long id, String prevPassword, UserUpdateRequestDTO userDTO) throws Exception {
		int check = userService.changePw(id, prevPassword, userDTO);
		
		return check;
	}
	
	@GetMapping("/delete/{id}")
	public String delete() throws Exception {
		
		return "user/delete";
	}
	
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id, HttpSession session) throws Exception {
		UserResponseDTO userDTO = userService.findById(id);
		if(userDTO.getAuthId().equals(AuthId.EMAIL)) {
			userService.delete(id);
		} else if(userDTO.getAuthId().equals(AuthId.KAKAO)) {
			customOAuth2UserService.deleteKakao(session.getAttribute("access_token").toString(), id);
			session.removeAttribute("access_token");
		} else if(userDTO.getAuthId().equals(AuthId.NAVER)) {
			customOAuth2UserService.deleteNaver(session.getAttribute("access_token").toString(), id);
		}
		
		SecurityContextHolder.clearContext();
		
		return "redirect:/";
	}
	
}
