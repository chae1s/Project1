package com.toy.project1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
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
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import com.toy.project1.auth.LoginUser;
import com.toy.project1.domain.SessionUser;
import com.toy.project1.dto.CommentResponseDTO;
import com.toy.project1.dto.DiaryResponseDTO;
import com.toy.project1.dto.DiarySaveRequestDTO;
import com.toy.project1.dto.DiaryUpdateRequestDTO;
import com.toy.project1.dto.HashtagSaveRequestDTO;
import com.toy.project1.service.CommentService;
import com.toy.project1.service.DiaryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {
	
	private final DiaryService diaryService;
	private final CommentService commentService;
	
	@GetMapping("")
	public ModelAndView DiaryList(@RequestParam(value = "page", defaultValue = "1") int page) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<DiaryResponseDTO> popularDiaryDTO = diaryService.popularDiaryList();
		Page<DiaryResponseDTO> newDiaryDTO = diaryService.newDiaryList(page-1);
		
		mv.setViewName("diary/list");
		mv.addObject("popDiaryDTO", popularDiaryDTO);
		mv.addObject("newDiaryDTO", newDiaryDTO);
		return mv;
	}
	
	@GetMapping("/save")
	public String saveDiary() throws Exception {
		
		return "diary/save";
	}
	
	@PostMapping("/save")
	public String saveDiary(DiarySaveRequestDTO diaryDTO, @LoginUser SessionUser user, HashtagSaveRequestDTO hashtagDTO) throws Exception {
		
		Long getId = diaryService.saveDiary(diaryDTO, user, hashtagDTO);
		
		return "redirect:/diaries/"+getId;
	}
	
	@ResponseBody
	@PostMapping("/upload/image")
	public Map<String, Object> uploadImage(@RequestParam Map<String, Object> paramMap, MultipartRequest request) throws Exception {
		MultipartFile uploadFile = request.getFile("upload");
		String fileName = diaryService.uploadImage(uploadFile);
		System.out.println(fileName);
		paramMap.put("url", "/images/upload/diary/" + fileName);
		return paramMap;
	}
	
	@GetMapping("/{id}")
	public String openDiary(@PathVariable Long id, Model model) {
		DiaryResponseDTO diaryDTO = diaryService.openDiary(id);
		List<CommentResponseDTO> commentDTO = commentService.commentList(id);
		model.addAttribute("diary", diaryDTO);
		model.addAttribute("comments", commentDTO);
		
		return "diary/open";
	}
	
	@GetMapping("/update/{id}")
	public String updateDiary(@PathVariable Long id, @LoginUser SessionUser user, Model model) throws Exception {
		DiaryResponseDTO diaryDTO = diaryService.openDiary(id);
		if(user.getId() != diaryDTO.getUser().getId()) {
			throw new IllegalArgumentException("이 페이지 이용 불가");
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<diaryDTO.getHashtags().size();i++) {
			sb.append("#");
			sb.append(diaryDTO.getHashtags().get(i).getHashtag().getHashtag());
			sb.append(" ");
		}
		model.addAttribute("diary", diaryDTO);
		model.addAttribute("hashtag", sb.toString());
		return "diary/update";
	}
	
	@PutMapping("/update/{id}")
	public String updateDiary(@PathVariable Long id, DiaryUpdateRequestDTO diaryDTO, HashtagSaveRequestDTO hashtagDTO) throws Exception{
		System.out.println("update");
		diaryService.updateDiary(id, diaryDTO, hashtagDTO);
		
		return "redirect:/diaries/"+id;
	}
	
	@GetMapping("/delete/{id}")
	public String deleteDiary(@PathVariable Long id) throws Exception {
		diaryService.deleteDiary(id);
		
		return "redirect:/diaries";
	}

}
