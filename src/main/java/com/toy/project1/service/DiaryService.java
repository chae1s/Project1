package com.toy.project1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toy.project1.domain.Diary;
import com.toy.project1.domain.DiaryFiles;
import com.toy.project1.domain.DiaryHashtag;
import com.toy.project1.domain.Hashtag;
import com.toy.project1.domain.SessionUser;
import com.toy.project1.domain.User;
import com.toy.project1.dto.DiaryFilesSaveRequestDTO;
import com.toy.project1.dto.DiaryResponseDTO;
import com.toy.project1.dto.DiarySaveRequestDTO;
import com.toy.project1.dto.HashtagSaveRequestDTO;
import com.toy.project1.handler.FileHandler;
import com.toy.project1.repository.DiaryFilesRepository;
import com.toy.project1.repository.DiaryHashtagRepository;
import com.toy.project1.repository.DiaryRepository;
import com.toy.project1.repository.HashtagRepository;
import com.toy.project1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {
	
	private final UserRepository userRepository;
	private final DiaryRepository diaryRepository;
	
	private final HashtagRepository hashtagRepository;
	private final DiaryHashtagRepository diaryHashtagRepository;
	
	private final DiaryFilesRepository diaryFilesRepository;
	private final FileHandler fileHandler; 
	
	private final String regTag = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";
	
	@Transactional
	public Long save(List<MultipartFile> files, DiarySaveRequestDTO diaryDTO, SessionUser sessionUser, HashtagSaveRequestDTO hashtagDTO) throws Exception {
		//로그인되어 있는 사람이 작성된 게시글의 작성자이기 때문에 로그인된 user의 정보를 가져온다.
		User user = userRepository.findById(sessionUser.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
		//diaryDTO에 가져온 user의 정보를 넣어준다.
		diaryDTO.setUser(user);
		//diary save
		Diary diary = diaryRepository.save(diaryDTO.toEntity());
		
		/* hashtagDTO의 name을 #를 기준으로 자른 후 하나씩 조회 -> 없으면 hashtag table에 save, 그 후 diaryhashtag table에 diary 정보와 hashtag정보 저장 */
		StringTokenizer st = new StringTokenizer(hashtagDTO.getHashtag(), "#| ");
		while(st.hasMoreTokens()) {
			hashtagDTO.setHashtag(st.nextToken());
			System.out.println(hashtagDTO.getHashtag());
			Hashtag hashtag = hashtagRepository.findByHashtag(hashtagDTO.getHashtag()).orElseGet(()->hashtagRepository.save(hashtagDTO.toEntity()));
			diaryHashtagRepository.save(DiaryHashtag.builder()
												.diary(diary)
												.hashtag(hashtag)
												.build());
		}
		
		/* 다중 파일 업로드 */
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
	
	public List<DiaryResponseDTO> popularDiaryList() {
		List<DiaryResponseDTO> diaryDTOs = diaryRepository.findAll(Sort.by(Sort.Direction.DESC, "hits")).stream()
											.map(diary -> new DiaryResponseDTO(diary, regTag))
											.limit(4)
											.collect(Collectors.toList());
		
		return diaryDTOs;
	}
	
	public Page<DiaryResponseDTO> newDiaryList(int page) {
		List<DiaryResponseDTO> diaryDTOs = new ArrayList<>();
		List<Diary> diaries = diaryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		List<DiaryHashtag> diaryHashtags = new ArrayList<>();
		for(Diary diary : diaries) {
			diaryHashtags = diaryHashtagRepository.findByDiaryId(diary.getId());
			diaryDTOs.add(new DiaryResponseDTO(diary, regTag, diaryHashtags));
		}
		
		Pageable pageable = PageRequest.of(page, 5);
		int start = (int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), diaryDTOs.size());
		
		Page<DiaryResponseDTO> diaryPage = new PageImpl<>(diaryDTOs.subList(start, end), pageable, diaryDTOs.size());
		
		return diaryPage;
	}
	
	public DiaryResponseDTO openDiary(Long id) {
		Diary diary = diaryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
		List<DiaryHashtag> diaryHashtags = diaryHashtagRepository.findByDiaryId(id);
		DiaryResponseDTO diaryResponseDTO = new DiaryResponseDTO(diary, diaryHashtags);
		
		return diaryResponseDTO;
	}
	
	public void deleteDiary(Long id) throws Exception {
		Diary diary = diaryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
		List<DiaryHashtag> diaryHashtags = diaryHashtagRepository.findByDiaryId(id);
		List<DiaryFiles> diaryFiles = diaryFilesRepository.findByDiaryId(id);
		for(DiaryHashtag diaryHashtag : diaryHashtags) {
			diaryHashtagRepository.delete(diaryHashtag);
		}
		for(DiaryFiles file : diaryFiles) {
			fileHandler.fileDelete("images/upload/diary", file.getFileName());
			diaryFilesRepository.delete(file);
		}
		
		diaryRepository.delete(diary);
	}

}
