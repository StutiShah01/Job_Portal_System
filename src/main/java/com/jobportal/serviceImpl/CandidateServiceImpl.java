package com.jobportal.serviceImpl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jobportal.Constants.StringConstants;
import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.CandidateDTO;
import com.jobportal.dto.CandidateEducationDTO;
import com.jobportal.dto.CandidateExperienceDTO;
import com.jobportal.dto.GeneralSpecification;
import com.jobportal.dto.SearchCandidateDTO;
import com.jobportal.entity.Application;
import com.jobportal.entity.Candidate;
import com.jobportal.entity.CandidateEducation;
import com.jobportal.entity.CandidateExperience;
import com.jobportal.entity.CandidateSkill;
import com.jobportal.entity.CandidateTags;
import com.jobportal.entity.Interview;
import com.jobportal.entity.Tags;
import com.jobportal.mappers.CandidateMapper;
import com.jobportal.mappers.InterviewMapper;
import com.jobportal.pagination.Pagination;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.CandidateEducationRepository;
import com.jobportal.repository.CandidateExperienceRepository;
import com.jobportal.repository.CandidateRepository;
import com.jobportal.repository.CandidateSkillRepository;
import com.jobportal.repository.CandidateTagsRepository;
import com.jobportal.repository.InterviewRepository;
import com.jobportal.repository.TagsRepository;
import com.jobportal.service.CandidateService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CandidateServiceImpl implements CandidateService {

	@Autowired
	CandidateRepository candidateRepository;
	
	@Autowired
	CandidateMapper candidateMapper;
	
	@Autowired
	ApplicationRepository applicationRepository;
	
	@Autowired
	InterviewRepository interviewRepository;
	
	@Autowired
	InterviewMapper interviewMapper;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	CandidateSkillRepository candidateSkillRepository;
	
	@Autowired
	TagsRepository tagsRepository;
	
	@Autowired
	CandidateEducationRepository educationRepository;
	
	@Autowired
	CandidateTagsRepository candidateTagsRepository;
	
	@Autowired
	CandidateExperienceRepository experienceRepository;
	
	@Autowired
	MessageSource messageSource;
	
	// Adds a new candidate or updates an existing one
	@Override
	@Transactional
	public CandidateDTO addOrUpdateCandidate(CandidateDTO candidateDto) throws NotFoundException, CustomException {
		log.info("Starting to add or update a candidate");
		CandidateDTO dtoReponse=new CandidateDTO();
		if(candidateDto.getId()==null || candidateDto.getId() == 0)
		{
			// Adding new candidate
			log.info("Adding new candidate");
			Candidate candidate=candidateMapper.toCandidate(candidateDto);
//			
			candidateRepository.save(candidate);
			
			//Adding Candidate skills into candidate_skill table
			List<CandidateSkill> candidateSkills = candidateDto.getSkills().stream()
			            .map(skill -> {
			                CandidateSkill candidateSkill = new CandidateSkill();
			                candidateSkill.setCandidate(candidate);
			                candidateSkill.setSkillName(skill.toLowerCase());
			                return candidateSkill;
			            })
			            .collect(Collectors.toList());
			 candidateSkillRepository.saveAll(candidateSkills);
			 
			 //Adding Candidate tags into candidate_tags table
			 if (candidateDto.getTagIds() != null && !candidateDto.getTagIds().isEmpty()) {
			        List<CandidateTags> candidateTags = candidateDto.getTagIds().stream()
			            .map(tagId -> createCandidateTag(candidate, tagId))
			            .collect(Collectors.toList());
			        candidate.setTags(candidateTags); // 
			        candidateRepository.save(candidate); // Save the candidate again with tags
			    }
			// Add Candidate Education into candidate_education table
	        if (candidateDto.getEducation() != null && !candidateDto.getEducation().isEmpty()) {
	            List<CandidateEducation> candidateEducations = candidateDto.getEducation().stream()
	                .map(educationDto -> createCandidateEducation(candidate, educationDto))
	                .collect(Collectors.toList());
	            educationRepository.saveAll(candidateEducations);
	            candidate.setEducationList(candidateEducations);
	            candidateRepository.save(candidate);
	           }
	        
	        // Add Candidate Experience into candidate_experience
	        if (candidateDto.getExperience() != null && !candidateDto.getExperience().isEmpty()) {
	            List<CandidateExperience> candidateExperiences = candidateDto.getExperience().stream()
	                .map(experienceDto -> createCandidateExperience(candidate, experienceDto))
	                .collect(Collectors.toList());
	            experienceRepository.saveAll(candidateExperiences);

	          
	            candidate.setExperienceList(candidateExperiences);
	            candidateRepository.save(candidate);
	        }
	        if (candidateDto.getExperience() == null && candidateDto.getExperience().isEmpty()) {
	        	candidate.setTotalYearsOfExperience(0.0);
	        }
	        
	        //taking candidateDto to get the year of experince for company
	        CandidateDTO tempCandidateDTO=candidateMapper.toCandidateDTO(candidate);
	        Double totalyearsOfExperience = tempCandidateDTO.getExperience().stream()
                    .filter(experience -> experience.getEndDate() != null)
                    .mapToDouble(experience -> experience.getYearsOfExperience())
                    .sum();
           	candidate.setTotalYearsOfExperience(totalyearsOfExperience);
           	candidateRepository.save(candidate);
			dtoReponse=candidateMapper.toCandidateDTO(candidate);
		}
		else
		{    // Updating existing candidate
            log.info("Updating existing candidate");
			Optional<Candidate> candidate=candidateRepository.findById(candidateDto.getId());
			if(candidate.isEmpty())
			{
				throw new NotFoundException("candidate not found ");
			}
			Candidate updateCandidate=candidate.get();
			updateCandidate.setName(candidateDto.getName());
			updateCandidate.setPhone(candidateDto.getPhone());
			updateCandidate.setEmail(candidateDto.getEmail());
			updateCandidate.setResumeUrl(candidateDto.getResumeUrl());
		    
			//Deleting Existing skill set of the candidate of the candidate and adding updated candidate skills
			List<CandidateSkill> skillsTODelete=candidateSkillRepository.findByCandidate(updateCandidate);
			candidateSkillRepository.deleteAll(skillsTODelete);
			List<CandidateSkill> candidateSkills = candidateDto.getSkills().stream()
		            .map(skill -> {
		                CandidateSkill candidateSkill = new CandidateSkill();
		                candidateSkill.setCandidate(updateCandidate);
		                candidateSkill.setSkillName(skill.toLowerCase());
		                return candidateSkill;
		            })
		            .collect(Collectors.toList());
		    candidateSkillRepository.saveAll(candidateSkills);
		    
		    //Deleting Existing candidate tags and adding updated candidate tag list
		    List<CandidateTags> candidateTags=candidateTagsRepository.findByCandidate(updateCandidate);
		    candidateTagsRepository.deleteAll(candidateTags);
		    if (candidateDto.getTagIds() != null && !candidateDto.getTagIds().isEmpty())
		    {
		    	List<CandidateTags> newCandidateTagsList=candidateDto.getTagIds().stream()
		    						.map(tagId-> createCandidateTag(updateCandidate, tagId))
		    						.collect(Collectors.toList());
		        
		        updateCandidate.setTags(newCandidateTagsList);
		    };
		    updateCandidate.setSkills(candidateSkills);
		    
		    //Deleting Exsisting candidate Education Details and adding updated Candiate List of Education Details
		    List<CandidateEducation> candidateEducations =educationRepository.findByCandidate(updateCandidate);
		    educationRepository.deleteAll(candidateEducations);
		    if(candidateDto.getEducation() !=null || !candidateDto.getEducation().isEmpty() )
		    {
		    	List<CandidateEducation> newCandidateEducations = candidateDto.getEducation().stream()
		                .map(educationDto -> createCandidateEducation(updateCandidate, educationDto))
		                .collect(Collectors.toList());
		            educationRepository.saveAll(newCandidateEducations);
		            updateCandidate.setEducationList(newCandidateEducations);
		           
		    }
		    List<CandidateExperience> candidateExperiences=experienceRepository.findByCandidate(updateCandidate);
		    experienceRepository.deleteAll(candidateExperiences);
		    if (candidateDto.getExperience() != null && !candidateDto.getExperience().isEmpty()) {
	            List<CandidateExperience> newCandidateExperiences = candidateDto.getExperience().stream()
	                .map(experienceDto -> createCandidateExperience(updateCandidate, experienceDto))
	                .collect(Collectors.toList());
	            experienceRepository.saveAll(newCandidateExperiences);

	          
	            updateCandidate.setExperienceList(newCandidateExperiences);
	            candidateRepository.save(updateCandidate);
	        }
		    if (candidateDto.getExperience() == null && candidateDto.getExperience().isEmpty()) {
	        	updateCandidate.setTotalYearsOfExperience(0.0);
	        }
		    CandidateDTO tempUpdateDto=candidateMapper.toCandidateDTO(updateCandidate);
		    Double totalyearsOfExperience = tempUpdateDto.getExperience().stream()
                    .filter(experience -> experience.getEndDate() != null)
                    .mapToDouble(experience -> experience.getYearsOfExperience())
                    .sum();
           	updateCandidate.setTotalYearsOfExperience(totalyearsOfExperience);
		    
			candidateRepository.save(updateCandidate);
			dtoReponse =candidateMapper.toCandidateDTO(updateCandidate);
		}
		
		return dtoReponse;
	}
	
	//method for creation of candidate tag and adding to database
	private CandidateTags createCandidateTag(Candidate candidate, Long tagId) {
	    Tags tag = tagsRepository.findById(tagId)
	        .orElseThrow(() -> new NotFoundException("Tag not found with ID: " + tagId+" ,Choose appropriate Tags")); // Consider using a specific checked exception

	    CandidateTags candidateTag = new CandidateTags();
	    candidateTag.setCandidate(candidate);
	    candidateTag.setTags(tag); // Set the found Tag object
	    candidateTagsRepository.save(candidateTag);
	    return candidateTag;
	}
	private CandidateEducation createCandidateEducation(Candidate candidate, CandidateEducationDTO educationDto) {
	    CandidateEducation candidateEducation = new CandidateEducation();
	    BeanUtils.copyProperties(educationDto, candidateEducation);
	    candidateEducation.setCandidate(candidate);
	    return candidateEducation;
	}
	
	private CandidateExperience createCandidateExperience(Candidate candidate, CandidateExperienceDTO experienceDto) {
	    CandidateExperience candidateExperience = new CandidateExperience();
	    BeanUtils.copyProperties(experienceDto, candidateExperience);
	    if (experienceDto.getStartDate()== null) {
	    	experienceDto.setYearsOfExperience(null);
	    }

        LocalDate startDateLocalDate = experienceDto.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDateLocalDate =experienceDto.getEndDate() != null ? experienceDto.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : LocalDate.now();
        Double yearsOfExperience = ChronoUnit.DAYS.between(startDateLocalDate, endDateLocalDate) / 365.0;
        candidateExperience.setYearsOfExperience(yearsOfExperience);
//        experienceDto.setYearsOfExperience( ChronoUnit.YEARS.between(startDateLocalDate, endDateLocalDate));
    	
	    candidateExperience.setCandidate(candidate);
	    return candidateExperience;
	}

	
	// Deletes a candidate by setting their status to inactive
	@Override
	@Transactional
	public void deleteCandidateById(Long id) throws NotFoundException {
		log.info("Deleting candidate with ID: {}", id);
		Optional<Candidate> candidate=candidateRepository.findById(id);
		if(candidate.isEmpty())
		{
			throw new NotFoundException("Candidate Not Found");
			
		}
		Candidate deleteCandidate=candidate.get();
		deleteCandidate.setIsActive(false);
		candidateRepository.save(deleteCandidate);
		log.info("Candidate deleted successfully");
		
	}

	
	// Paginates the list of candidates
	@Override
	public Pagination getCandidates(Integer pageNo, Integer pageSize) {
		Pagination pagination = new Pagination();
		Pageable pageable=PageRequest.of(pageNo, pageSize);
		Page<Candidate> pageSource = candidateRepository.findAll(pageable);
		List<Candidate> allCandidates = pageSource.getContent();
		List<CandidateDTO > responselist =new ArrayList<>();
		for(Candidate candidate :allCandidates)
		{
			responselist.add(candidateMapper.toCandidateDTO(candidate));
		}
		pagination.setList(responselist);
		pagination.setCurrentPage(pageable.getPageNumber()+1);
		pagination.setHasNext(pageSource.hasNext());
		pagination.setHasprevious(pageSource.hasPrevious());
		pagination.setPageSize(pageable.getPageSize());
		pagination.setTotalCount(responselist.size());
		log.info("fetching Candidate's data");
		return pagination;
	}
	// Searches candidates by name or skills
    public  List<CandidateDTO> searchByNameSkills(String name) {
    	log.info("Searching candidates by name and/or skills");
    	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    	    CriteriaQuery<Candidate> criteriaQuery = criteriaBuilder.createQuery(Candidate.class);
    	    Root<Candidate> candidateRoot = criteriaQuery.from(Candidate.class);

    	    List<Predicate> predicates = new ArrayList<>();

    	    // Filter by name (optional)
    	    if (name != null) {
    	        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(candidateRoot.get("name")), "%" + name.toLowerCase() + "%"));
    	    }
//    	    Set<String> skillSet = null;
//            if (skills != null && !skills.isEmpty()) {
//                skillSet = Stream.of(skills.split(","))
//                                 .map(String::trim)
//                                 .collect(Collectors.toSet());
//            }
//            
//    	    // Filter by skills (optional)
//    	    if (skills != null && !skills.isEmpty()) {
//    	        // Assuming 'skills' is a collection type in the Candidate entity
//    	        Join<Candidate,Candidate > skillJoin = candidateRoot.join("skills", JoinType.INNER);
//    	        Predicate skillsPredicate = skillJoin.get("skills").in(skillSet);
//    	        predicates.add(skillsPredicate);
//    	    }

    	    criteriaQuery.select(candidateRoot).where(predicates.toArray(new Predicate[0]));

    	    List<Candidate> candidates = entityManager.createQuery(criteriaQuery).getResultList();
    	    
    	    // Convert Candidate entities to CandidateDTOs and return
    	    List<CandidateDTO> candidateDTOs = new ArrayList<>();
    	    for (Candidate candidate : candidates) {
    	        
    	        candidateDTOs.add(candidateMapper.toCandidateDTO(candidate));
    	    }
    	    log.info("Found {} candidates", candidateDTOs.size());
    	    return candidateDTOs;
    	}
 // Searches candidates by name or skills
//    @Override
//    public List<CandidateDTO> searchCandidates(String name, String skills) {
//    		List<Candidate> candidates=new ArrayList<>();
//    		if (name != null && skills != null) {
//    			Set<String> skillSet = new HashSet<>(Arrays.asList(skills.split(",")));
//    			candidates= candidateRepository.findByNameContainingIgnoreCaseOrSkillsIn(name, skillSet);
//	        } else if (name != null) {
//	        	candidates= candidateRepository.findByNameContainingIgnoreCase(name);
//	        } else if (skills != null) {
//	        	Set<String> skillSet = new HashSet<>(Arrays.asList(skills.split(",")));
//	            candidates= candidateRepository.findBySkillsIn(skillSet);
//	        } 
//	        List<CandidateDTO> candidatedtolist=new ArrayList<>();
//	        for(Candidate candidate : candidates)
//	        {
//	        	candidatedtolist.add(candidateMapper.toCandidateDTO(candidate));
//	        }
//        return candidatedtolist;
//    }

    
	@Override
	public Candidate findById(Long id) {
		Optional<Candidate> candidate=candidateRepository.findById(id);
		if(candidate.isPresent())
		{
			return candidate.get();
		}
		return null;
	}

	//fetching scheduled interview list of a particular candidate by id
	@Override
	public Page<Interview> scheduleInterviewListOfCandidate(Long candidateid,Integer pagenumber,Integer pageSize) {
		log.info("Searching for shcedule interview list");
		List<Application> cand_app_list=applicationRepository.findByCandidate_Id(candidateid);
		List<Interview> scheduleInterviewList=new ArrayList<>();
		for(Application application:cand_app_list)
		{
			System.out.println(application.getId());
			Interview interview=interviewRepository.findByApplication_Id(application.getId());
			if(interview !=null  && interview.getInterviewStatus().equals(StringConstants.INTERVIEW_STATUS_SCHEDULE))
			{
				scheduleInterviewList.add(interview);
			}
		}
		Pageable pageable=PageRequest.of(pagenumber, pageSize);
		Page<Interview> interviewPages=new PageImpl<>(scheduleInterviewList,pageable,scheduleInterviewList.size());
//		List<InterviewDTO> responselist=new ArrayList<>();
//		for(Interview interview:scheduleInterviewList) {
//			responselist.add(interviewMapper.toInterviewDTO(interview));
//		}
		//
		return interviewPages;
	}
	// paginated candidate's filtered list
	@Override
	public Page<Candidate> searchCandidate(SearchCandidateDTO searchCandidateDTO, Integer PageNumber, Integer PageSize) {
		
		Pageable pageable = PageRequest.of(PageNumber, PageSize);
		Page<Candidate> pages=candidateRepository.findAll(GeneralSpecification.searchCandidate(searchCandidateDTO), pageable);
		
		return pages;
	}
	@Override
	public CandidateDTO getSingleCandidate(Long cnadidateId) throws NotFoundException {
		Candidate candidate=candidateRepository.findById(cnadidateId).orElseThrow(()->new NotFoundException("Candidate Not Found"));
		CandidateDTO candidatedto=candidateMapper.toCandidateDTO(candidate);
		return candidatedto;
	}
//	@Override
//	public String changeCandidateStatus(long id, Boolean active) throws NotFoundException {
//		// TODO Auto-generated method stub
//		return null;
//	}
	@Override
	public void changeCandidateStatus(Long id, Boolean active) throws NotFoundException ,CustomException{
		Candidate candidate=candidateRepository.findById(id).orElseThrow(()->new NotFoundException("Candidate Not Found"));
		Boolean currentStatus=candidate.getIsActive();
		if(currentStatus.equals(active))
		{
			String messageKey=active? "candidate.already.active":"candidate.already.inactive";
			String message=messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
			throw new CustomException(message);
					
		}
		candidate.setIsActive(active);
		candidateRepository.save(candidate);
	}

	@Override
	public boolean validateEmail(Long candidateId, String candidateEmail) {
		 if (candidateId == null) {
			    return candidateRepository.findByEmail(candidateEmail).isPresent();
		}
		else {
			    return candidateRepository.findByIdIsNotAndEmailIgnoreCase(candidateId, candidateEmail).isPresent();
			  }
	}

	@Override
	public boolean validatePhoneno(Long candidateId, Long candidatePhoneno) {
		if (candidateId == null) {
		    return candidateRepository.findByPhone(candidatePhoneno).isPresent();
	}
	else {
		    return candidateRepository.findByIdIsNotAndPhone(candidateId, candidatePhoneno).isPresent();
		  }
	}


	@Override
	public Boolean validateCandidate(CandidateDTO candidateDTO) {
//		if (candidateDTO.getId() == null) {
//		    return candidateRepository.existsByEmailAndPhoneAndTags_id(candidateDTO.getEmail(), candidateDTO.getPhone(), candidateDTO.getTagId());
//	}
//	else {
//		    return candidateRepository.existsByIdIsNotAndEmailAndPhoneAndTags_id(candidateDTO.getId(),candidateDTO.getEmail(), candidateDTO.getPhone(), candidateDTO.getTagId());
//		  }
		return null;
		
	}

	@Override
	public Boolean validateExperinces(Set<CandidateExperienceDTO> experiences) {
		Boolean flag=false;
		if (experiences != null) {
            for (CandidateExperienceDTO experience : experiences) {
                if (experience.getStartDate() != null && experience.getEndDate() != null) {
                    if (experience.getStartDate().after(experience.getEndDate())) {
                    	flag=true;
                    	break;
                    }
                } else if (experience.getStartDate() != null && experience.getEndDate() == null) {
                    continue;
                }
            }
		}
		return flag;
	}


	

	
}
