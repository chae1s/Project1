package com.toy.project1.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.toy.project1.domain.Comment;
import com.toy.project1.domain.Diary;
import com.toy.project1.domain.SessionUser;
import com.toy.project1.domain.User;
import com.toy.project1.dto.CommentResponseDTO;
import com.toy.project1.dto.CommentSaveRequestDTO;
import com.toy.project1.dto.CommentUpdateRequestDTO;
import com.toy.project1.repository.CommentRepository;
import com.toy.project1.repository.DiaryRepository;
import com.toy.project1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	
	private final UserRepository userRepository;
	private final DiaryRepository diaryRepository;
	private final CommentRepository commentRepository;
	
	@Transactional
	public void saveComment(Long diaryId, CommentSaveRequestDTO commentDTO, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
		
		commentDTO.setDiary(diary);
		commentDTO.setUser(user);
		
		commentRepository.save(commentDTO.toEntity());
	}
	
	public List<CommentResponseDTO> commentList(Long diary_id) {
		List<CommentResponseDTO> commentDTO = commentRepository.findByDiaryId(diary_id, Sort.by(Sort.Direction.ASC, "id")).stream()
												.map(comment -> new CommentResponseDTO(comment))
												.collect(Collectors.toList());
		
		return commentDTO;
	}
	
	@Transactional
	public void deleteComment(Long diaryId, Long commentId, Long userId) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
		if(comment.getUser().getId() != userId) {
			new IllegalArgumentException("삭제할 수 없습니다.");
		}
		
		commentRepository.delete(comment);
	}
	
	@Transactional
	public void updateComment(Long commentId, CommentUpdateRequestDTO commentDTO) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
		
		comment.update(commentDTO.getContent());

	}
	
	public CommentResponseDTO findById(Long id) {
		Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
		
		return new CommentResponseDTO(comment);
	}

}
