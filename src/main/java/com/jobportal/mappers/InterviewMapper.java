package com.jobportal.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jobportal.dto.InterviewDTO;
import com.jobportal.entity.Interview;

@Component
public class InterviewMapper {

	public InterviewDTO toInterviewDTO(Interview interview)
	{
		InterviewDTO interviewdto=new InterviewDTO();
		interviewdto.setId(interview.getId());
		interviewdto.setApplicationId(interview.getApplication().getId());
		interviewdto.setInterviewDate(interview.getInterviewDate());
		interviewdto.setInterviewType(interview.getInterviewType());
		return interviewdto;
	}
	public List<InterviewDTO> interviewDTOList(List<Interview> interviews )
	{
		List<InterviewDTO> responselist=new ArrayList<>();
		for(Interview interview:interviews) {
			responselist.add(toInterviewDTO(interview));
		}
		return responselist;
	}
}
