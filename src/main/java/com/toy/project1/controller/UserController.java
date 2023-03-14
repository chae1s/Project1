package com.toy.project1.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
<<<<<<< HEAD
	
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
		
		if(!principal.getName().equals(userDTO.getEmail())) {
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
	public String delete(@PathVariable Long id) throws Exception {
		userService.delete(id);
		
		return "redirect:/users/logout";
	}
	
	
=======
>>>>>>> parent of f3529ad (회원정보 수정, 비밀번호 변경, 탈퇴)
}
