package com.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.entity.Candidate;
import com.jobportal.entity.CandidateExperience;

@Repository
public interface CandidateExperienceRepository extends JpaRepository<CandidateExperience, Long>{
	List<CandidateExperience> findByCandidate(Candidate candidate);
}
