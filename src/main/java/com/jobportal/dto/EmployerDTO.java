package com.jobportal.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployerDTO implements Serializable{
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 187368L;

	private Long id;

    @NotBlank(message="{employer.name.required}")
    private String name;
    
    @NotBlank(message="{email.required}")
    private String email;

    @NotBlank(message="{website.required}")
    private String website;

    @NotBlank(message="{description.required}")
    private String description;

    @NotBlank(message="{industry.required }")
    private String industry;

    @NotBlank(message="{location.required}")
    private String location;

    @NotNull(message="{companySize.required}")
    private Integer companySize;

}
