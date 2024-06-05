package com.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.jobportal.entity.CandidateSkill;
import java.util.List;
import com.jobportal.entity.Candidate;


@Repository
public interface CandidateSkillRepository extends JpaRepository<CandidateSkill,Long>,JpaSpecificationExecutor<CandidateSkill> {
	/**
	 * 
	 * @param candidate
	 * @return List<CandidateSkill>
	 */
	public List<CandidateSkill> findByCandidate(Candidate candidate);
}
