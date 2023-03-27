package com.toy.project1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toy.project1.domain.Hashtag;
import java.lang.String;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long>{
	
	Optional<Hashtag> findByHashtag(String hashtag);
	
	Optional<Hashtag> findById(Long id);
	

}
