package com.jobportal.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 143337010235092651L;

	private Long id;
	
	@NotBlank(message = "{tag.required}")
	private String tagName;
	

}
