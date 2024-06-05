package com.jobportal.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ApplicationWithJobCandidateDTO implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 17654L;
	private Long id;
	private JobDTO job;
	private CandidateDTO candidate;
	private Date submittedDate;
	private String applicationStatus;
}
