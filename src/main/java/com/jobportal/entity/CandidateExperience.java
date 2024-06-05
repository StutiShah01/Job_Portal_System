package com.jobportal.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class CandidateExperience extends CommonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column
    private String jobTitle;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column
    @Temporal(TemporalType.DATE)
    private Date endDate; // Can be null for current positions

    @ManyToOne
    private Candidate candidate; 

    @Column 
    private Double yearsOfExperience;


//    public Double calculateYearsOfExperience() {
//        if (startDate == null) {
//            return null; // Handle cases without start date
//        }
//
//        LocalDate startDateLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate endDateLocalDate = endDate != null ? endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : LocalDate.now();
//        return ChronoUnit.YEARS.between(startDateLocalDate, endDateLocalDate).doubleValue();
//    }
}

