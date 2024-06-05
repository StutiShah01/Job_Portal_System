package com.jobportal.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 98732L;

	private Long id;

	@NotBlank(message="{job.title.notblank}")
    private String title;
	
    private String description;

    @NotBlank(message="{job.category.notblank}")
    private String category;

    private String employmentType;

    private Integer minSalary;
    
    private Integer maxSalary;
   
    private Set<String> required_skills;

    @NotNull(message="{job.postedDate.notnull}")
    private Date postedDate;
    
    @NotNull(message="{job.expiryDate.notnull}")
    private Date expiryDate;

//    @NotNull(message="Please Give the Employers Id")
    private Long employerId;
    
    @NotNull(message = "{tagid.required}")
    private Long tagId;
     
}
