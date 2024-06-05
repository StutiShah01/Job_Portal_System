package com.jobportal.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jobportal.dto.ApplicationDTO;
import com.jobportal.entity.Candidate;
import com.jobportal.entity.Job;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.CandidateService;
import com.jobportal.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationValidator implements Validator {

	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	JobService jobservice;
	
	@Autowired
	CandidateService candidateService;

	
	@Override
	public boolean supports(Class<?> clazz) {
		// This method checks if the provided class is ApplicationDTO
	    log.info("Checking if class is ApplicationDTO for validation");
		return ApplicationDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(target instanceof ApplicationDTO)
		{
		ApplicationDTO applicationDTO=(ApplicationDTO) target;
		
		// Validation logic with comments and logging
	    log.info("Validating application details");
		if(applicationDTO.getCandidateId()==null || applicationDTO.getJobId()==null)
		{
			errors.reject("404", "jobId and candidate id both are required");
		}
		
		if(applicationDTO.getJobId()!=null && applicationDTO.getCandidateId()!=null)
		{
			if(jobservice.findByJob(applicationDTO.getJobId())==null)
			{
				log.info("Job with ID {} not found", applicationDTO.getJobId());
				errors.reject("404","Job doesn't Exsist" );
			}
			else {
				
			}
			if(candidateService.findById(applicationDTO.getCandidateId())==null)
			{
				log.info("Candidate with ID {} not found", applicationDTO.getCandidateId());
				errors.rejectValue("candidateId","404", "Candidate doesn't not exsist");
			}
			Job job=jobservice.findByJob(applicationDTO.getJobId());
			Candidate candidate=candidateService.findById(applicationDTO.getCandidateId());
			if(applicationService.ExsistJobAndCandidate(job, candidate))
			{
				log.info("Application already exists for job ID {} and candidate ID {}", applicationDTO.getJobId(), applicationDTO.getCandidateId());
				errors.rejectValue("jobId", "404", "Already applied for this job");
			}
		}
	
		}
		
		
	}

}
