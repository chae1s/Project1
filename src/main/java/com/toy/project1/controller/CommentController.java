package com.toy.project1.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toy.project1.auth.LoginUser;
import com.toy.project1.domain.SessionUser;
import com.toy.project1.dto.CommentResponseDTO;
import com.toy.project1.dto.CommentSaveRequestDTO;
import com.toy.project1.dto.CommentUpdateRequestDTO;
import com.toy.project1.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService commentService;
	
	@PostMapping("/diaries/{diaryId}/comments")
	public void saveComment(@PathVariable Long diaryId, CommentSaveRequestDTO commentDTO, @LoginUser SessionUser user) throws Exception {
		commentService.saveComment(diaryId, commentDTO, user.getId());
	}
	
	@GetMapping("/diaries/{diaryId}/comments/delete/{commentId}")
	public void deleteComment(@PathVariable Long diaryId, @PathVariable Long commentId, @LoginUser SessionUser user) {
		commentService.deleteComment(diaryId, commentId, user.getId());
	}
	
	@GetMapping("/diaries/{diaryId}/comments/update/{commentId}")
	public Map<String, Object> openComment(@PathVariable Long diaryId, @PathVariable Long commentId) {
		CommentResponseDTO commentDTO = commentService.findById(commentId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content", commentDTO.getContent());
		map.put("id", commentDTO.getId());
		return map;
	}
	
	@PostMapping("/diaries/{diaryId}/comments/update/{commentId}")
	public void openComment(@PathVariable Long diaryId, @PathVariable Long commentId, CommentUpdateRequestDTO commentDTO) {
		commentService.updateComment(commentId, commentDTO);
	}

}
