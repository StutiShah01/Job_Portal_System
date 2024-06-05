package com.jobportal.dto;

import java.io.Serializable;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InterviewDTO implements Serializable {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 67554L;

	private Long id;

 	 @NotNull(message = "{interview.applicationId.notnull}")
     private Long applicationId;

     @NotNull(message = "{interview.interviewDate.notnull}")
     private Date interviewDate;
     
     @NotBlank(message = "{interview.interviewType.notblank}")
     private String interviewType; // Online OR offline

}
