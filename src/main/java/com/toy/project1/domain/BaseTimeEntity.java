package com.toy.project1.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
	
	@CreatedDate
	private String createdDate;
	@LastModifiedDate
	private String modifiedDate;
	
	@PrePersist
	public void onPrePersist() {
		this.createdDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
		this.modifiedDate = this.createdDate;
	}
	
	@PreUpdate
	public void preUpdate() {
		this.modifiedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
	}

}
