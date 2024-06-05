package com.jobportal.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jobportal.dto.InterviewDTO;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.InterviewService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InterviewValidator implements Validator {

	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	InterviewService interviewService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		log.info("Checking if class is InterviewDTO for validation");
		return InterviewDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		InterviewDTO interviewdto=(InterviewDTO) target;
		log.info("Validating interview details");
		if(interviewdto.getId()==null || interviewdto.getId() ==0)
		{
		if(interviewdto.getApplicationId()==null)
		{
			log.info("Application ID is missing for new interview");
			errors.rejectValue("applicationId", "404", "Application Id is required to schedule interview");
			
		}
		if(interviewdto.getApplicationId()!=null || interviewdto.getApplicationId()!=0)
		{
			boolean checkApplication=applicationService.checkApplicationExists(interviewdto.getApplicationId());
			if(checkApplication ==false)
			{
				log.info("Application with ID {} not found", interviewdto.getApplicationId());
				errors.rejectValue("applicationId","404", "No Application Found");
			}
			boolean checkInterview=interviewService.checkInterviewSchedule(interviewdto.getApplicationId());
			if(checkInterview==true)
			{
				 log.info("Interview already scheduled for application ID {}", interviewdto.getApplicationId());
				errors.rejectValue("applicationId","404", "Interview Already Schedule for this application");
			}
		}
		}
		
	}

}
