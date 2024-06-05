package com.jobportal.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.jobportal.entity.Candidate;
import com.jobportal.entity.CandidateEducation;
import com.jobportal.entity.CandidateSkill;
import com.jobportal.entity.CandidateTags;
import com.jobportal.entity.Employer;
import com.jobportal.entity.Job;
import com.jobportal.entity.Tags;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class GeneralSpecification {

	public static Specification<Employer> searchEmployer(SearchEmployerDTO searchEmployerDTO) {
	    return (root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        // Name search 
	        if (searchEmployerDTO.getName() != null && !searchEmployerDTO.getName().isEmpty()) {
	            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchEmployerDTO.getName().toLowerCase() + "%"));
	        }

	        // Industry search 
	        if (searchEmployerDTO.getIndustry() != null && !searchEmployerDTO.getIndustry().isEmpty()) {
	            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("industry")), "%" + searchEmployerDTO.getIndustry().toLowerCase() + "%"));
	        }

	        // Company size search 
	        if (searchEmployerDTO.getCompanySize() != null) {
	            predicates.add(criteriaBuilder.equal(root.get("companySize"), searchEmployerDTO.getCompanySize()));
	        }

	        // Active employer search 
	        if (searchEmployerDTO.getIsActive() != null) {
	            predicates.add(criteriaBuilder.equal(root.get("isActive"), searchEmployerDTO.getIsActive()));
	        } else {
	            // Include all employers by default (assuming an is_Active field)
	            predicates.add(criteriaBuilder.equal(root.get("isActive"), true));
	        }

	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    };
	}

	public static Specification<Candidate> searchCandidate(SearchCandidateDTO searchCandidateDTO) {
	    return (root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (searchCandidateDTO.getName() != null && !searchCandidateDTO.getName().isEmpty()) {
	            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchCandidateDTO.getName().toLowerCase() + "%"));
	        }

	        
	        if (searchCandidateDTO.getSkills() != null && !searchCandidateDTO.getSkills().isEmpty()) {
	            List<Predicate> skillPredicates = new ArrayList<>();
	            Join<Candidate, CandidateSkill> skillJoin = root.join("skills");

	            for (String skill : searchCandidateDTO.getSkills()) {
	                Predicate skillNamePredicate = criteriaBuilder.equal(criteriaBuilder.lower(skillJoin.get("skillName")), skill.toLowerCase());
	                skillPredicates.add(skillNamePredicate);
	            }

	            // Combine all skill predicates with OR 
	            Predicate orPredicate = criteriaBuilder.or(skillPredicates.toArray(new Predicate[0]));
	            predicates.add(orPredicate);
	        }
	        if (searchCandidateDTO.getTagName() != null && !searchCandidateDTO.getTagName().isEmpty()) {
                Join<Candidate, CandidateTags> tagJoin = root.join("tags");
                Join<CandidateTags, Tags> tagsJoin = tagJoin.join("tags"); 
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(tagsJoin.get("tagName")), "%" + searchCandidateDTO.getTagName().toLowerCase() + "%"));
            }
	        if(searchCandidateDTO.getTotalYearsOfExperince()!=null )
	        {
	        	predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalYearsOfExperience"), searchCandidateDTO.getTotalYearsOfExperince()));
	        }
	        if (searchCandidateDTO.getInstituteName() != null && !searchCandidateDTO.getInstituteName().isEmpty()) {
	            Join<Candidate, CandidateEducation> educationJoin = root.join("educationList");
	            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(educationJoin.get("institutionName")),"%" + searchCandidateDTO.getInstituteName().toLowerCase() + "%"
	            ));
	        }
        	
	        predicates.add(criteriaBuilder.equal(root.get("isActive"), true));

	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    };
	}
     	public static Specification<Job> searchJobs(SearchJobDTO searchJobDTO) {
		    return (root, query, criteriaBuilder) -> {
		        List<Predicate> predicates = new ArrayList<>();

		        // Title search 
		        if (searchJobDTO.getTitle() != null) {
		            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + searchJobDTO.getTitle().toLowerCase() + "%"));
		        }

		        // Category search
		        if (searchJobDTO.getCategory() != null) {
		            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("category")), "%" + searchJobDTO.getCategory().toLowerCase() + "%"));
		        }

		        // Employment type search
		        if (searchJobDTO.getEmploymentType() != null) {
		            predicates.add(criteriaBuilder.like(root.get("employmentType"),"%" + searchJobDTO.getEmploymentType().toLowerCase() + "%"));
		        }

		        // Salary range search
		        if (searchJobDTO.getMinSalary() != null && searchJobDTO.getMaxSalary() != null) {
		            predicates.add(criteriaBuilder.and(
		                    criteriaBuilder.greaterThanOrEqualTo(root.get("minSalary"), searchJobDTO.getMinSalary()),
		                    criteriaBuilder.lessThanOrEqualTo(root.get("maxSalary"), searchJobDTO.getMaxSalary())
		            ));
		        }

		        // Employer name search 
		        if (searchJobDTO.getEmployerName() != null) {
		            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("employer").get("name")), "%" + searchJobDTO.getEmployerName().toLowerCase() + "%"));
		        }
		        
		        if(searchJobDTO.getTagName()!=null)
		        {
		        	predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("tags").get("tagName")), "%" +searchJobDTO.getTagName().toLowerCase()+"%"));
		        }
		        predicates.add(criteriaBuilder.equal(root.get("isActive"), true));

		        //fetch all the jobs if no criteria is provided
		        if (predicates.isEmpty()) {
		            return criteriaBuilder.and(criteriaBuilder.equal(root.get("isActive"), true));
		        }

		        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		    };
		}

		
}