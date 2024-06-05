package com.jobportal.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jobportal.dto.EmployerDTO;
import com.jobportal.dto.SearchEmployerDTO;
import com.jobportal.service.EmployerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmployerValidator implements Validator {

	@Autowired
	EmployerService employerService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		log.info("Checking if class is EmployerDTO for validation");
		return EmployerDTO.class.equals(clazz) || SearchEmployerDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(target instanceof EmployerDTO)
		{
			EmployerDTO employerdto = (EmployerDTO) target;
			log.info("Validating employer details");
		
			Long employerId=employerdto.getId();
			String employerEmail=employerdto.getEmail();
			String employerWebsite=employerdto.getWebsite();
		
			System.out.println(employerId + "" +employerEmail);
	
			if(employerService.validateEmail(employerId, employerEmail))
			{
				errors.rejectValue("email", "email.already.exists", new Object[] {employerdto.getEmail()},employerdto.getEmail() + " already exsist by default msg");

			}
			if(employerService.validateWebsite(employerId, employerWebsite))
			{
				errors.rejectValue("website", "404", employerWebsite+ " already exsist ");

			}
			
		}	
	}
		
}


