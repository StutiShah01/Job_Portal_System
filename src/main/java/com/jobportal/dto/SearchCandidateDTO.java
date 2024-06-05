package com.jobportal.dto;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCandidateDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 87654L;
	private String name;
	private Set<String> skills;
	private String tagName;
	private String instituteName;
	private Integer totalYearsOfExperince;
	

}
