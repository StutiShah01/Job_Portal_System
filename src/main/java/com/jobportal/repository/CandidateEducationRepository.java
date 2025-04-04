package com.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.entity.Candidate;
import com.jobportal.entity.CandidateEducation;

@Repository
public interface CandidateEducationRepository extends JpaRepository<CandidateEducation, Long>{
	List<CandidateEducation> findByCandidate(Candidate candidate);
}
