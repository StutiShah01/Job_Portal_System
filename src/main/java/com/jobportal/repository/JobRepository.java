package com.jobportal.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.jobportal.entity.Employer;
import com.jobportal.entity.Job;
import com.jobportal.entity.Tags;


@Repository
public interface JobRepository extends JpaRepository<Job, Long>,JpaSpecificationExecutor<Job>{
	
	/**
	 * 
	 * @param postedDate
	 * @param title
	 * @param employer
	 * @return
	 */
	boolean existsByPostedDateAndTitleAndEmployer( Date postedDate,String title,Employer employer);
	/**
	 * 
	 * @param postedDate
	 * @param tag
	 * @param employer
	 * @return
	 */
	boolean existsByPostedDateAndTagsAndEmployer(Date postedDate,Tags tag,Employer employer);
	/**
	 * 
	 * @param id
	 * @param postedDate
	 * @param title
	 * @param employer
	 * @return
	 */
	boolean existsByIdIsNotAndPostedDateAndTitleAndEmployer(Long id, Date postedDate, String title, Employer employer);
	/**
	 * 
	 * @param employer
	 * @param pageable
	 * @return
	 */
	public Page<Job> findByEmployer(Employer employer,Pageable pageable );
	/**
	 * 
	 * @param employer
	 * @return
	 */
	public List<Job> findByEmployer(Employer employer);
	/**
	 * 
	 * @param employerId
	 * @return
	 */
	List<Job> findByEmployer_Id(Long employerId);
}
