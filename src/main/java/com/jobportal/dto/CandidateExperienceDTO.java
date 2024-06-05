package com.jobportal.dto;

import java.io.Serializable;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CandidateExperienceDTO implements Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 6943253986726336539L;

//		private Long id;

		@NotBlank(message="{company.name.required}")
	    private String companyName;

	    @NotBlank(message = "{jobtitle.required}")
	    private String jobTitle;

	    @NotNull(message = "{startdate.required}")
	    private Date startDate;
  
	    private Date endDate; // Can be null for current positions

	    private Double yearsOfExperience;

}
