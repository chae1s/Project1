package com.toy.project1.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toy.project1.domain.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>{

	List <Diary> findByUserId(Long user_id);
	
	@EntityGraph(attributePaths = {"user", "diaryFiles"})
	Page<Diary> findAll(Pageable pageable);
	
	@EntityGraph(attributePaths = {"user", "diaryFiles"})
	List<Diary> findAll(Sort sort);
}
