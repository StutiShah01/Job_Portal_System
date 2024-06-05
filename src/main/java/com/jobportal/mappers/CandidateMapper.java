package com.jobportal.mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.jobportal.dto.CandidateDTO;
import com.jobportal.dto.CandidateEducationDTO;
import com.jobportal.dto.CandidateExperienceDTO;
import com.jobportal.entity.Candidate;
import com.jobportal.entity.CandidateEducation;
import com.jobportal.entity.CandidateExperience;
import com.jobportal.entity.CandidateSkill;
import com.jobportal.entity.CandidateTags;
import com.jobportal.repository.CandidateSkillRepository;
import com.jobportal.repository.CandidateTagsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CandidateMapper {
	public final CandidateSkillRepository candidateSkillRepository;
	public final CandidateTagsRepository candidateTagsRepository;
	public Candidate toCandidate(CandidateDTO  candidateDTO)
	{
		Candidate candidate=new Candidate();
		
		BeanUtils.copyProperties(candidateDTO, candidate);
		return candidate;

	}
	public CandidateDTO toCandidateDTO(Candidate candidate)
	{
		CandidateDTO dto=new CandidateDTO();
		BeanUtils.copyProperties(candidate, dto);
		List<CandidateSkill> candidateSkill=candidateSkillRepository.findByCandidate(candidate);
		Set<String> skills=new HashSet<>();
		for(CandidateSkill cs:candidateSkill)
		{
			skills.add(cs.getSkillName());
		}
		dto.setSkills(skills);
		List<CandidateTags> candidateTags=candidateTagsRepository.findByCandidate(candidate);
		Set<Long> tags=new HashSet<>();
		for(CandidateTags ct:candidateTags)
		{
			tags.add(ct.getTags().getId());
		}
		dto.setTagIds(tags);
		Set<CandidateEducationDTO> educationSet=toSetOfCandidateEducationDTO(candidate.getEducationList());
		dto.setEducation(educationSet);
		Set<CandidateExperienceDTO> experinceSet=toSetOfCandidateExperienceDTO(candidate.getExperienceList());
		dto.setExperience(experinceSet);
		return dto;
	}
	public Set<CandidateEducationDTO> toSetOfCandidateEducationDTO(List<CandidateEducation> educationslist)
	{
		Set<CandidateEducationDTO> set=new HashSet<>();
		for( CandidateEducation education :educationslist)
		{
			CandidateEducationDTO dto =new CandidateEducationDTO();
 			BeanUtils.copyProperties(education, dto);
 			set.add(dto);
		}
		return set;
	}
	public Set<CandidateExperienceDTO> toSetOfCandidateExperienceDTO(List<CandidateExperience> experincelist )
	{
		Set<CandidateExperienceDTO> set=new HashSet<>();
		for(CandidateExperience experience : experincelist  )
		{
			CandidateExperienceDTO dto=new CandidateExperienceDTO();
			BeanUtils.copyProperties(experience, dto);
			set.add(dto);
			
		}
		return set;
	}
	
	public List<CandidateDTO> candidateDTOs(List<Candidate> candidates)
	{
		List<CandidateDTO> responseList= new ArrayList<>();
		for(Candidate candidate:candidates)
		{
			responseList.add(toCandidateDTO(candidate));
		}
		return responseList;
	}
}
