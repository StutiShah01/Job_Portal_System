package com.jobportal.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.jobportal.dto.CandidateDTO;
import com.jobportal.service.CandidateService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CandidateValidator implements Validator {

	@Autowired
	CandidateService candidateService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		log.info("Checking if class is CandidateDTO for validation");
		return CandidateDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors)
	{
		if(target instanceof CandidateDTO)
		{
		CandidateDTO candidatedto=(CandidateDTO)target;
		
		log.info("Validating candidate details");
		Long candidateId=candidatedto.getId();
		String candidateEmail=candidatedto.getEmail();
		Long candidatePhoneno=candidatedto.getPhone();
		if(candidateService.validateEmail(candidateId, candidateEmail))
		{
			errors.rejectValue("email", "email.already.exists", new Object[] {candidatedto.getEmail()},candidatedto.getEmail() + " already exsist ");

		}
		if(candidateService.validatePhoneno(candidateId, candidatePhoneno))
		{
			errors.rejectValue("phone", "404", candidatePhoneno+ " already exsist ");

		}
		if(candidatedto.getSkills().isEmpty() || candidatedto.getSkills() == null)
		{
			errors.rejectValue("skills", "404", "atleast one skills is required");
		}
		if(candidatedto.getEducation().isEmpty() || candidatedto.getEducation() == null)
		{
			errors.rejectValue("education", "404", "Please give candidate education details");
		}
		if(candidateService.validateExperinces(candidatedto.getExperience()))
		{
			errors.rejectValue("experience", "404", "Start date must be before end date for ");
		}
		
		}
}
		
		
}

	
