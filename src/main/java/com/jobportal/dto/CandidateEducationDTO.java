package com.jobportal.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CandidateEducationDTO implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 4514481398998019092L;

//	private Long id;
	
	@NotBlank(message = "{education.institutionName.required}")
    private String institutionName;
    
	@NotBlank(message = "{education.degree.required}")
    private String degree;
    
	@NotBlank(message = "{education.fieldOfStudy.required}")
    private String fieldOfStudy;

	@NotNull(message = "{education.graduationYear.required}")
    private Integer graduationYear;

    

}
