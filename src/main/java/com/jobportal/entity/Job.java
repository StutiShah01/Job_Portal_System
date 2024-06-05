package com.jobportal.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Job extends CommonModel {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String category;

    @Column
    private String employmentType;

    @Column
    private Integer minSalary;
    
    @Column
    private Integer maxSalary;

    @Column
    private Set<String> required_skills; // Comma-separated list of skills

    @Column
    @Temporal(TemporalType.DATE)
    private Date postedDate;

    @Column
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    @OneToMany(mappedBy = "job")
    private List<Application> applications;

    @Column
    private Boolean isActive=true;
    
    @OneToOne
    @JoinColumn(name = "tag_id") // Foreign key to Tag
    private Tags tags;
}
