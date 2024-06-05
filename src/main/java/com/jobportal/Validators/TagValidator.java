package com.jobportal.Validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jobportal.dto.TagDTO;
import com.jobportal.service.TagService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@Slf4j
@RequiredArgsConstructor
public class TagValidator implements Validator {

	private final TagService tagService;
	@Override
	public boolean supports(Class<?> clazz) {
		return TagDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(target instanceof TagDTO)
		{
			log.info("Validating Tag");
			TagDTO tagDTO=(TagDTO) target;
			if(tagService.existsTag(tagDTO.getTagName()))
			{
				errors.rejectValue("tagName","404",tagDTO.getTagName()+" already exists" );
			}
		}
		
	}

}
