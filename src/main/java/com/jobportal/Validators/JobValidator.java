package com.jobportal.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jobportal.dto.JobDTO;
import com.jobportal.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobValidator implements Validator{

	
	@Autowired
	JobService jobService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		log.info("Checking if class is JobDTO for validation");
		return JobDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		JobDTO jobDTO=(JobDTO) target;
		log.info("Validating job details");
		if(jobDTO.getMinSalary() !=null && jobDTO.getMaxSalary() !=null ) {
		if(jobDTO.getMinSalary()>jobDTO.getMaxSalary())
		{
            log.info("Invalid salary range: minimum salary ({}) is greater than maximum salary ({})", jobDTO.getMinSalary(), jobDTO.getMaxSalary());
			errors.rejectValue("minSalary", "404", "Please give valid Salary Range");
		}
		}
		if(jobDTO.getRequired_skills()==null)
		{
	        log.info("Required skills are missing for the job");
			errors.rejectValue("required_skills", "404", "Please give required skills for this job");
		}
		if(jobDTO.getPostedDate()!=null && jobDTO.getExpiryDate() !=null)
		{
			if(jobDTO.getExpiryDate().before(jobDTO.getPostedDate()))
			{
	            log.info("Invalid date range: expiry date ({}) is before posted date ({})", jobDTO.getExpiryDate(), jobDTO.getPostedDate());
				errors.rejectValue("expiryDate", "404", "Please give proper postdate and expiry date");
			}
		}
	
			
			
		
		
	}

}
