package com.toy.project1.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.toy.project1.auth.LoginUser;
import com.toy.project1.domain.SessionUser;
import com.toy.project1.dto.DiaryResponseDTO;
import com.toy.project1.dto.DiarySaveRequestDTO;
import com.toy.project1.dto.HashtagSaveRequestDTO;
import com.toy.project1.service.DiaryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
	
	private final DiaryService diaryService;
	
	@GetMapping("")
	public ModelAndView list(@RequestParam(value = "page", defaultValue = "0") int page) throws Exception {
		ModelAndView mv = new ModelAndView();
		List<DiaryResponseDTO> popularDiaryDTO = diaryService.popularDiaryList();
		Page<DiaryResponseDTO> newDiaryDTO = diaryService.newDiaryList(page-1);
		
		mv.setViewName("diary/list");
		mv.addObject("popDiaryDTO", popularDiaryDTO);
		mv.addObject("newDiaryDTO", newDiaryDTO);
		return mv;
	}
	
	@GetMapping("/save")
	public String save() throws Exception {
		
		return "diary/save";
	}
	
	@PostMapping("/save")
	public String save(@RequestPart(value = "files", required = false) List<MultipartFile> files,
			DiarySaveRequestDTO diaryDTO, @LoginUser SessionUser user, HashtagSaveRequestDTO hashtagDTO) throws Exception {
		
		Long getId = diaryService.save(files, diaryDTO, user, hashtagDTO);
		
		return "redirect:/diary/"+getId;
	}
	
	@GetMapping("/{id}")
	public String openDiary(@PathVariable Long id) {
		
		return "diary/open";
	}

}
