package com.jobportal.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchEmployerDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 75643L;

	private String name;
   
    private String industry;
  
    private Integer companySize;
    
    private Boolean isActive;
	
	

}
