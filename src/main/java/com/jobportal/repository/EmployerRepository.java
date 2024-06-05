package com.jobportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.jobportal.entity.Employer;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long>,JpaSpecificationExecutor<Employer>{
		/**
		 * 
		 * @param email
		 * @return
		 */
		public Optional<Employer> findByEmail(String email);
		/**
		 * 
		 * @param website
		 * @return
		 */
		public Optional<Employer> findByWebsite(String website);
		/**
		 * 
		 * @param email
		 * @return
		 */
		public Boolean existsByEmail(String email);
		/**
		 * 
		 * @param id
		 * @param email
		 * @return
		 */
		public Optional<Employer> findByIdIsNotAndEmailIgnoringCase(Long id,String email);
		/**
		 * 
		 * @param id
		 * @param website
		 * @return
		 */
		public Optional<Employer> findByIdIsNotAndWebsite(long id,String website);
}
