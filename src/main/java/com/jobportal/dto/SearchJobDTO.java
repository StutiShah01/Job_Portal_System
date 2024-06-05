package com.jobportal.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SearchJobDTO implements Serializable 
{
	/**
	 * 
	 */
	  private static final long serialVersionUID = 4772130443467956868L;
	  private String title;
	  private String category;
	  private String employmentType;
	  private Integer minSalary;
	  private Integer maxSalary;
	  private String employerName;
	  private String tagName;
}