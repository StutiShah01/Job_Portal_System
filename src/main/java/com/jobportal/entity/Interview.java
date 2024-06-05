package com.jobportal.entity;

import java.util.Date;

import com.jobportal.enumconstants.InterviewStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@EqualsAndHashCode(callSuper = true)
public class Interview extends CommonModel{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7939819281908860848L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date interviewDate;

    @Column
    private String interviewType; // Online OR offline

    @Column
    private String interviewStatus=InterviewStatus.SCHEDULED.name(); // e.g: Scheduled, Completed, No-Show,Cancelled

}
