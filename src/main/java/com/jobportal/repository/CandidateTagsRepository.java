package com.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.entity.Candidate;
import com.jobportal.entity.CandidateTags;

@Repository
public interface CandidateTagsRepository extends JpaRepository<CandidateTags, Long> {
	/**
	 * 
	 * @param candidate
	 * @return List<CandidateTags>
	 */
	public List<CandidateTags> findByCandidate(Candidate candidate);

}
