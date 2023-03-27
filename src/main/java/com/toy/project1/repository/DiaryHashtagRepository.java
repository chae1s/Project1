package com.toy.project1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toy.project1.domain.DiaryHashtag;

@Repository
public interface DiaryHashtagRepository extends JpaRepository<DiaryHashtag, Long>{
	
	@EntityGraph(attributePaths = {"hashtag"})
	List<DiaryHashtag> findByDiaryId(Long diary_id);

}
