package com.jobportal.entity;

import java.io.Serializable;
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


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Employer extends CommonModel implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1061899001928885979L;



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String website;

    @Column
    private String description;

    @Column
    private String industry;

    @Column
    private String location;

    @Column
    private Integer companySize;

    @OneToMany(mappedBy = "employer",fetch = FetchType.LAZY)
    private List<Job> jobs;
    
    @Column
    private Boolean isActive=true;

    
}

