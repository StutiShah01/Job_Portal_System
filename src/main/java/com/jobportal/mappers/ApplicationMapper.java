package com.jobportal.mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jobportal.dto.ApplicationWithCandidateDTO;
import com.jobportal.dto.ApplicationWithJobCandidateDTO;
import com.jobportal.dto.ApplicationWithJobDTO;
import com.jobportal.dto.CandidateDTO;
import com.jobportal.dto.EmployerDTO;
import com.jobportal.dto.JobDTO;
import com.jobportal.dto.JobWithEmployerDTO;
import com.jobportal.entity.Application;
import com.jobportal.entity.Candidate;
import com.jobportal.entity.Employer;
import com.jobportal.entity.Job;


@Component
public class ApplicationMapper {


	    public ApplicationWithJobDTO mapToApplicationWithJobDTO(Application application) {
	        ApplicationWithJobDTO dto = new ApplicationWithJobDTO();
	        dto.setID(application.getId());

	        // Nested object mapping for JobWithEmployerDTO
	        Job job = application.getJob();
	        if (job != null) {
	            JobWithEmployerDTO jobDto = new JobWithEmployerDTO();
	            jobDto.setId(job.getId());
	            jobDto.setTitle(job.getTitle());
	            jobDto.setDescription(job.getDescription());
	            jobDto.setCategory(job.getCategory());
	            jobDto.setEmploymentType(job.getEmploymentType());
	            jobDto.setMinSalary(job.getMinSalary());  
	            jobDto.setMaxSalary(job.getMaxSalary()); 
	            jobDto.setRequired_skills(job.getRequired_skills() != null ? new HashSet<>(job.getRequired_skills()) : null); // Handle potential null skills (convert to Set)
	            jobDto.setPostedDate(job.getPostedDate());
	            jobDto.setExpiryDate(job.getExpiryDate());

	            // Further nested mapping for EmployerDTO (if applicable)
	            Employer employer = job.getEmployer();
	            if (employer != null) {
	                EmployerDTO employerDto = new EmployerDTO();
	                employerDto.setId(employer.getId());
	                employerDto.setName(employer.getName());
	                employerDto.setEmail(employer.getEmail());
	                employerDto.setWebsite(employer.getWebsite());
	                employerDto.setDescription(employer.getDescription());
	                employerDto.setIndustry(employer.getIndustry());
	                employerDto.setLocation(employer.getLocation());
	                employerDto.setCompanySize(employer.getCompanySize());
	                jobDto.setEmployer(employerDto);
	            }
	            dto.setJob(jobDto);
	        }

	        dto.setSubmittedDate(application.getSubmittedDate());
	        dto.setApplicationStatus(application.getApplicationStatus());
	        return dto;
	    }

	   
	    public ApplicationWithCandidateDTO mapToApplicationWithCandidateDTO(Application application) 
	    {
	        ApplicationWithCandidateDTO dto = new ApplicationWithCandidateDTO();
	        dto.setId(application.getId());
	        dto.setJobId(application.getJob().getId()); // Set jobId based on job existence
	        dto.setSubmittedDate(application.getSubmittedDate());
	        dto.setApplicationStatus(application.getApplicationStatus());

	        // Nested object mapping for CandidateDTO
	        Candidate candidate = application.getCandidate();
	        if (candidate != null) {
	            CandidateDTO candidateDto = new CandidateDTO();
	            candidateDto.setId(candidate.getId());
	            candidateDto.setName(candidate.getName());
	            candidateDto.setEmail(candidate.getEmail());
	            candidateDto.setPhone(candidate.getPhone());
	            candidateDto.setResumeUrl(candidate.getResumeUrl());
//	            candidateDto.setSkills(candidate.getSkills());
	            candidateDto.setExperienceSummary(candidate.getExperienceSummary()); // Assuming bio maps to experienceSummary in DTO
	            dto.setCandidate(candidateDto);
	           
	        }
	        return dto;
	    }
        public ApplicationWithJobCandidateDTO mapToApplicationWithJobCandidateDTO(Application application) {
            ApplicationWithJobCandidateDTO dto = new ApplicationWithJobCandidateDTO();
            dto.setId(application.getId());

            // Nested object mapping for JobDTO
            Job job = application.getJob();
            if (job != null) {
                JobDTO jobDto = new JobDTO();
                jobDto.setId(job.getId());
                jobDto.setTitle(job.getTitle());
                jobDto.setDescription(job.getDescription());
                jobDto.setCategory(job.getCategory());
                jobDto.setEmploymentType(job.getEmploymentType());
                jobDto.setMinSalary(job.getMinSalary());
                jobDto.setMaxSalary(job.getMaxSalary());
                jobDto.setRequired_skills(job.getRequired_skills() != null ? new HashSet<>(job.getRequired_skills()) : null); // Handle potential null skills (convert to Set)
                jobDto.setPostedDate(job.getPostedDate());
                jobDto.setExpiryDate(job.getExpiryDate());

                // Employer details are not included in the DTO as per  class structure
                // dto.setEmployerId(job.getEmployer().getId()); // Uncomment if needed

                dto.setJob(jobDto);
            }

            // Nested object mapping for CandidateDTO
            Candidate candidate = application.getCandidate();
            if (candidate != null) {
                CandidateDTO candidateDto = new CandidateDTO();
                candidateDto.setId(candidate.getId());
                candidateDto.setName(candidate.getName());
                candidateDto.setEmail(candidate.getEmail());
                candidateDto.setPhone(candidate.getPhone());
                candidateDto.setResumeUrl(candidate.getResumeUrl());
//                candidateDto.setSkills(candidate.getSkills());
                candidateDto.setExperienceSummary(candidate.getExperienceSummary()); // Assuming bio maps to experienceSummary in DTO
                dto.setCandidate(candidateDto);
            }

            dto.setSubmittedDate(application.getSubmittedDate());
            dto.setApplicationStatus(application.getApplicationStatus());
            return dto;
        }
        
        public List<ApplicationWithJobDTO> applicationWithJobDTOsList(List<Application> applist)
        {
        	List<ApplicationWithJobDTO> responseList=new ArrayList<>();
    		for(Application application:applist)
    		{
    			ApplicationWithJobDTO app=new ApplicationWithJobDTO();
    			app=mapToApplicationWithJobDTO(application);
    			responseList.add(app);
    		}
    		return responseList;
        }
	    

}
