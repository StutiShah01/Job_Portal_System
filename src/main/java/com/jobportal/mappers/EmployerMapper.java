package com.jobportal.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.jobportal.dto.EmployerDTO;
import com.jobportal.dto.EmployerWithJobDTO;
import com.jobportal.entity.Employer;

@Component
public class EmployerMapper {

	
	public Employer toEmployer(EmployerDTO  employerDto)
	{
		Employer employer=new Employer();
		BeanUtils.copyProperties(employerDto, employer);
		return employer;

	}
	public EmployerDTO toEmployerDTO(Employer employer)
	{
		EmployerDTO dto=new EmployerDTO();
		BeanUtils.copyProperties(employer, dto);
		return dto;
	}
	public EmployerWithJobDTO toEmployerWithJObDto(Employer employer)
	{
		EmployerWithJobDTO dto=new EmployerWithJobDTO();
		BeanUtils.copyProperties(employer, dto);
//		List<Job> jobs=new ArrayList<>();
		
		return dto;
		
	}
	public List<EmployerDTO> employerDTos(List<Employer> employers)
	{
		List<EmployerDTO> employerResponseList = new ArrayList<>();
		for (Employer employer : employers) {
			employerResponseList.add(toEmployerDTO(employer));
		}
		return employerResponseList;
	}
}
