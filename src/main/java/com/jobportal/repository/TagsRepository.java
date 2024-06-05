package com.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.jobportal.entity.Tags;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Long>,JpaSpecificationExecutor<Tags> {
	/**
	 * 
	 * @param tagname
	 * @return
	 */
	public Boolean existsByTagNameIgnoreCase(String tagname);

}
