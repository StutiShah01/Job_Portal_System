package com.jobportal.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ApplicationWithJobDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 543546L;
	private Long ID;
	private JobWithEmployerDTO job;
	private Date submittedDate;
	private String applicationStatus;
}
