package com.jobportal.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import lombok.Data;

@Data
public class JobWithEmployerDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 85432L;

	private Long id;

    private String title;
	
    private String description;

    private String category;

    private String employmentType;

    private Integer minSalary;
    
    private Integer maxSalary;
   
    private Set<String> required_skills;

    private Date postedDate;
    
    private Date expiryDate;

    private EmployerDTO employer;
}
