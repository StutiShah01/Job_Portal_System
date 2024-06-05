package com.jobportal.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")// The @SuppressWarnings("serial")annotation is retained to ignore serialization-related warnings, which is common for entity classes that implement Serializable.
@Entity
@Data
@EqualsAndHashCode(callSuper = true)//   - This ensures that the generated equals and hashCode methods include the fields from the superclass (CommenModel) 

public class Candidate extends CommonModel{


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    
    @Column
    private String email; // Consider adding email verification

    @Column
    private Long phone;  // Consider adding phone verification
  
    @Column
    private String resumeUrl;  

    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY) 
    private List<CandidateSkill> skills;
    
    @Column
    private String experienceSummary;

    @OneToMany(mappedBy = "candidate")
    private List<Application> applications;
     
    @OneToMany(mappedBy = "candidate",fetch =FetchType.LAZY)
    private List<CandidateTags> tags;
    
    @OneToMany(mappedBy = "candidate",fetch = FetchType.LAZY)
    private List<CandidateEducation> educationList; 

    @OneToMany(mappedBy = "candidate",fetch =  FetchType.LAZY)
    private List<CandidateExperience> experienceList;
    
    @Column
    private Double totalYearsOfExperience;
    
    @Column
    private Boolean isActive=true;

    
}