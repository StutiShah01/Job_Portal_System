package com.jobportal.dto;

import java.io.Serializable;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CandidateDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 7645321L;

	private Long id;

	@NotBlank(message = "{candidate.name.required}")
	private String name;

	@NotBlank(message = "{candidate.email.required}")
	private String email;

	@NotNull(message = "{candidate.phone.required}")
	@Min(value = 10, message = "{candidate.phone.min}")
	private Long phone;

	@NotBlank(message = "{candidate.resumeUrl.required}")
	private String resumeUrl;

	private Set<String> skills;

	@NotBlank(message = "{candidate.experienceSummary.required}")
	private String experienceSummary;
	
	private Set<Long> tagIds;
	
	private Set<@Valid CandidateEducationDTO> education;
	
	private Set<@Valid CandidateExperienceDTO> experience;
	
	private Double totalYearsOfExperience;
}
