package com.jobportal.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jobportal.entity.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate , Long>,JpaSpecificationExecutor<Candidate>{
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	public Optional<Candidate> findByEmail(String email);
	/**
	 * 
	 * @param phoneno
	 * @return
	 */
	public Optional<Candidate> findByPhone(Long phoneno);
	/**
	 * 
	 * @param id
	 * @param email
	 * @return
	 */
	public Optional<Candidate> findByIdIsNotAndEmailIgnoreCase(Long id,String email);
	/**
	 * 
	 * @param id
	 * @param phoneno
	 * @return
	 */
	public Optional<Candidate> findByIdIsNotAndPhone(Long id,Long phoneno );
	/**
	 * 
	 * @param name
	 * @return
	 */
	List<Candidate> findByNameContainingIgnoreCase(String name);
	/**
	 * 
	 * @param skills
	 * @return
	 */
	List<Candidate> findBySkillsIn(Set<String> skills);
	/**
	 * 
	 * @param email
	 * @param phone
	 * @param id
	 * @return
	 */
	public Boolean existsByEmailAndPhoneAndTags_id(String email, Long phone, Long id);
	/**
	 * 
	 * @param id
	 * @param email
	 * @param phone
	 * @param tagid
	 * @return
	 */
	public Boolean existsByIdIsNotAndEmailAndPhoneAndTags_id(Long id, String email, Long phone, Long tagid);

	/**
	 * 
	 * @param name
	 * @param skills
	 * @return
	 */
	@Query(value = "SELECT * FROM Candidate c " +
            "WHERE LOWER(c.name) LIKE CONCAT('%', :name, '%') " + 
            "AND c.skills @> :skills", nativeQuery = true)
	List<Candidate> findByNameContainingIgnoreCaseOrSkillsIn(String name, Set<String> skills);

	
}
