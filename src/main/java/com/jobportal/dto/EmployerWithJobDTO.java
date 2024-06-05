package com.jobportal.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;


@Data
public class EmployerWithJobDTO implements Serializable{

	 	/**
	 * 
	 */
	private static final long serialVersionUID = 867643L;

		private Long id;
 
	    private String name;

	    private String email;

	    private String website;

	    private String description;
	    
	    private String industry;

	    private String location;

	    private Integer companySize;

	    private List<JobWithApplicationDTO> jobs;
}
