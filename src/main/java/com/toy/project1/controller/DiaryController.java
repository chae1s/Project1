package com.toy.project1.controller;

import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
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
	public String saveDiary(@RequestPart(value = "files", required = false) List<MultipartFile> files,
			DiarySaveRequestDTO diaryDTO, @LoginUser SessionUser user, HashtagSaveRequestDTO hashtagDTO) throws Exception {
		
		Long getId = diaryService.saveDiary(files, diaryDTO, user, hashtagDTO);
		
		return "redirect:/diary/"+getId;
	}
	
	@ResponseBody
	@PostMapping("/upload/image")
	public Map<String, Object> uploadImage(@RequestParam Map<String, Object> paramMap, MultipartRequest request) throws Exception {
		MultipartFile uploadFile = request.getFile("upload");
		String fileName = diaryService.uploadImage(uploadFile);
		System.out.println(fileName);
		paramMap.put("url", "../images/upload/diary/" + fileName);
		return paramMap;
	}
	
	@GetMapping("/{id}")
	public String openDiary(@PathVariable Long id, Model model) {
		DiaryResponseDTO diaryDTO = diaryService.openDiary(id);
		model.addAttribute("diary", diaryDTO);
		
		return "diary/open";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteDiary(@PathVariable Long id) throws Exception {
		diaryService.deleteDiary(id);
		
		return "redirect:/diary";
	}

}
