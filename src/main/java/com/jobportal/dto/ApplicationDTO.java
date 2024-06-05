package com.jobportal.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3966541009906634713L;


	private Long id;
	
	@NotNull(message = "{application.jobId.notnull}")
    private Long jobId;

    @NotNull(message = "{application.candidateId.notnull}")
    private Long candidateId;

    private Date submittedDate=Date.from(Instant.now());

//    private String applicationStatus="submitted";

}
