package com.jobportal.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.entity.Interview;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
	/**
	 * 
	 * @param id
	 * @return
	 */
	boolean existsByApplication_Id(long id);
	/**
	 * 
	 * @param id
	 * @return
	 */
	Interview findByApplication_Id(long id);
}
