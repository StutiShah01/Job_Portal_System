package com.jobportal.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobWithApplicationDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 34588L;
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
    private long employerId;
    private List<ApplicationWithCandidateDTO> applicationlist; 
}
