package com.toy.project1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toy.project1.domain.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>{

	List <Diary> findByUserId(Long user_id);
	
	@EntityGraph(attributePaths = {"user"})
	List<Diary> findAll();
	
	@EntityGraph(attributePaths = {"user"})
	List<Diary> findAll(Sort sort);
	
	@EntityGraph(attributePaths = {"user"})
	Optional<Diary> findById(Long id);
}
