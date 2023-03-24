package com.toy.project1.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toy.project1.domain.Diary;
import com.toy.project1.domain.DiaryFiles;
import com.toy.project1.domain.SessionUser;
import com.toy.project1.domain.User;
import com.toy.project1.dto.DiaryFilesSaveRequestDTO;
import com.toy.project1.dto.DiaryResponseDTO;
import com.toy.project1.dto.DiarySaveRequestDTO;
import com.toy.project1.handler.FileHandler;
import com.toy.project1.repository.DiaryFilesRepository;
import com.toy.project1.repository.DiaryRepository;
import com.toy.project1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {
	
	private final UserRepository userRepository;
	private final DiaryRepository diaryRepository;
	private final DiaryFilesRepository diaryFilesRepository;
	private final FileHandler fileHandler; 
	
	private final String regTag = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";
	
	@Transactional
	public Long save(List<MultipartFile> files, DiarySaveRequestDTO diaryDTO, SessionUser sessionUser) throws Exception {
		User user = userRepository.findById(sessionUser.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		
		diaryDTO.setUser(user);
		Diary diary = diaryRepository.save(diaryDTO.toEntity());
		String fileName = "";
		for(MultipartFile file : files) {
			if(file.getSize() == 0L) {
				continue;
			}
			fileName = fileHandler.fileUpload("images/upload/diary", file);
			DiaryFilesSaveRequestDTO diaryFilesDTO = DiaryFilesSaveRequestDTO.builder()
					.diary(diary)
					.oriName(file.getOriginalFilename())
					.fileName(fileName)
					.build();
			
			DiaryFiles diaryFiles = diaryFilesDTO.toEntity();
			
			diaryFilesRepository.save(diaryFiles);
		}
		
		return diary.getId();
		
	}
	
	public List<DiaryResponseDTO> popularDiaryAll() {
		List<DiaryResponseDTO> diaryDTOs = diaryRepository.findAll(Sort.by(Sort.Direction.DESC, "hits")).stream()
											.map(diary -> new DiaryResponseDTO(diary, regTag))
											.limit(4)
											.collect(Collectors.toList());
		
		return diaryDTOs;
	}
	
	public Page<DiaryResponseDTO> newDiaryAll() {
		Pageable sortedById = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

		Page<DiaryResponseDTO> newDiaryDTOs = diaryRepository.findAll(sortedById)
											.map(diary -> new DiaryResponseDTO(diary, regTag));
		
		return newDiaryDTOs;
	}

}
