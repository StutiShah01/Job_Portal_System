package com.jobportal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class CandidateTags extends CommonModel {
	
	
	  private static final long serialVersionUID = 2515529185684679725L;

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "tag_id", nullable = false,unique = false)
	  private Tags tags;

	  @ManyToOne(fetch = FetchType.LAZY)
	  private Candidate candidate;

}
