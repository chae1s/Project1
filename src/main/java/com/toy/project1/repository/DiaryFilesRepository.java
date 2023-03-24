package com.toy.project1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toy.project1.domain.DiaryFiles;

@Repository
public interface DiaryFilesRepository extends JpaRepository<DiaryFiles, Long> {
	
	List<DiaryFiles> findByDiaryId(Long diary_id);
	

}
