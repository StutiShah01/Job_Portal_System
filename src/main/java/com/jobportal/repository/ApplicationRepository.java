package com.jobportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.jobportal.entity.Application;
import com.jobportal.entity.Candidate;
import com.jobportal.entity.Job;


@Repository
public interface ApplicationRepository extends JpaRepository<Application , Long> ,JpaSpecificationExecutor<Application>{
		
		/**
		 * 
		 * @param job
		 * @param candidate
		 * @return boolean value
		 */
		Boolean existsByJobAndCandidate(Job job, Candidate candidate);
		
		/**
		 * 
		 * @param candidate
		 * @param pageable
		 * @return Page<Application>
		 */
		Page<Application> findByCandidate(Candidate candidate,Pageable pageable);
		/**
		 * 
		 * @param job
		 * @return List<Application>
		 */
		List<Application> findByJob(Job job);
		/**
		 * 
		 * @param employerId
		 * @return List<Application>
		 */
		List<Application> findByJob_Employer_Id(Long employerId);
		/**
		 * 
		 * @param candidateId
		 * @return List<Application>
		 */
		List<Application> findByCandidate_Id(Long candidateId);
		/**
		 * @param Long application id
		 * @return boolean
		 */
		boolean existsById(Long id);
		
//		Page<Application> findByCandidateAndIs_Applied(Candidate candidate ,Boolean isApplied,Pageable pageable);
}
