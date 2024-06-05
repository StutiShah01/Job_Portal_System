package com.jobportal.entity;

import java.util.Date;

import com.jobportal.Constants.StringConstants;
import com.jobportal.enumconstants.ApplicationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)//   - This ensures that the generated equals and hashCode methods include the fields from the superclass (CommenModel) 
public class Application extends CommonModel{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7822520966787707921L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column
    @Temporal(TemporalType.DATE)
    private Date submittedDate;

    @Column
    private String applicationStatus=ApplicationStatus.SUBMITTED.name(); 
    
    @Column
    private Boolean isApplied=true;

 
}

