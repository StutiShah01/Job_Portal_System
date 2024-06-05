package com.jobportal.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ApplicationWithCandidateDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 23451L;
	private Long id;
    private long jobId;
    private CandidateDTO candidate;
    private Date submittedDate;
    private String applicationStatus;
}
