package com.jobportal.serviceImpl;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.GeneralSpecification;
import com.jobportal.dto.JobDTO;
import com.jobportal.dto.SearchJobDTO;
import com.jobportal.entity.Employer;
import com.jobportal.entity.Job;
import com.jobportal.entity.Tags;
import com.jobportal.mappers.JobMapper;
import com.jobportal.repository.EmployerRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.TagsRepository;
import com.jobportal.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobServiceImpl implements JobService {
	
	
	@Autowired
	EmployerRepository employerRepository;
	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	JobMapper jobMapper;
	
	@Autowired
	TagsRepository tagsRepository;
	
	@Autowired
	MessageSource messageSource;
	
	/**
     * Adds or updates a job posting.
     *
     * @param jobdto The job details to be added or updated.
     * @return JobDTO containing details of the saved job.
     * @throws NotFoundException If the employer doesn't exist or the job already exists for the same date and title by the same employer (excluding update case).
	 * @throws CustomException 
     */
	@Override
	public JobDTO addOrUpdateJob(JobDTO jobdto) throws NotFoundException, CustomException {
		
		JobDTO dtoResponse=new JobDTO();
		if(jobdto.getId()==null || jobdto.getId()==0)
		{
			long employerId=jobdto.getEmployerId();
			Employer employer=employerRepository.findById(employerId).orElseThrow(()->new NotFoundException("No Employer Exsist"));
			if(employer.getIsActive()==false)
			{
				throw new NotFoundException(employer.getName()+"does not exist now");
			}
			if(!tagsRepository.findById(jobdto.getTagId()).isPresent())
			{
				throw new NotFoundException("Tag not found,Select Appropriate Job tag");
			}
			Tags tags=tagsRepository.findById(jobdto.getTagId()).get();
			Job jobtoSave=jobMapper.toJob(jobdto);
			jobtoSave.setTitle(jobdto.getTitle().toLowerCase());
			jobtoSave.setTags(tags);
			if(jobtoSave.getRequired_skills().isEmpty()|| jobtoSave.getRequired_skills()==null)
			{
				throw new CustomException("Atleast one skill is required");
			}
			jobtoSave.setRequired_skills(skillToLowercase(jobdto.getRequired_skills()));
			jobtoSave.setEmployer(employer);
			Boolean checkExist=jobRepository.existsByPostedDateAndTagsAndEmployer(jobdto.getPostedDate(), tags, employer);
//			Boolean checkExsist=jobRepository.existsByPostedDateAndTitleAndEmployer( jobdto.getPostedDate(),jobdto.getTitle().toLowerCase(), employer);
			if(checkExist==true)
			{
				throw new NotFoundException("Job already Posted for " + jobdto.getTagId()+ " on date " +jobdto.getPostedDate());
			}
			jobRepository.save(jobtoSave);
			dtoResponse=jobMapper.tojobDtO(jobtoSave);
			log.info("Job added successfully: {}", dtoResponse.getTitle());
		}
		else // Updating an existing job
		{
			Job job=jobRepository.findById(jobdto.getId()).orElseThrow(()->new NotFoundException("No Such Job Found"));
			
			long employerId=jobdto.getEmployerId();
			Employer employer=employerRepository.findById(employerId).orElseThrow(()->new NotFoundException("No Employer Exsist"));
			
			// Check employer status before proceeding
			if(employer.getIsActive()==false)
			{
				throw new NotFoundException(employer.getName()+"does not exist now");
			}
			job.setEmployer(employer);
			job.setTitle(jobdto.getTitle().toLowerCase());
			job.setCategory(jobdto.getCategory());
			job.setDescription(jobdto.getDescription());
			job.setEmploymentType(jobdto.getEmploymentType());
			job.setExpiryDate(jobdto.getPostedDate());
			job.setMaxSalary(jobdto.getMaxSalary());
			job.setMinSalary(jobdto.getMinSalary());
			job.setPostedDate(jobdto.getPostedDate());
			if(job.getRequired_skills().isEmpty() || job.getRequired_skills()==null)
			{
				throw new CustomException("Atleast One Skill is required");
			}
			job.setRequired_skills(skillToLowercase(jobdto.getRequired_skills()));
			
			// Check for existing job with same title and posted date for the employer (excluding the current update)
			Boolean checkExsist=jobRepository.existsByIdIsNotAndPostedDateAndTitleAndEmployer(jobdto.getId(), jobdto.getPostedDate(),jobdto.getTitle().toLowerCase(), employer);
			if(checkExsist==true)
			{
				throw new NotFoundException("Job already Posted for " + jobdto.getTitle());
			}
			jobRepository.save(job);
			dtoResponse=jobMapper.tojobDtO(job);
			log.info("Job updated successfully");
		}
		return dtoResponse;
	}
	
	
	public Set<String> skillToLowercase(Set<String> skills)
	{
		Set<String> skillToLowerCase=new HashSet<>();
		for(String skill:skills)
		{
			skillToLowerCase.add(skill.toLowerCase());
		}
		return skillToLowerCase;
	}
	
	@Override
	public String deleteJob(Long id) throws NotFoundException {
		Job job=jobRepository.findById(id).orElseThrow(()->new NotFoundException("Job Not Found"));
		job.setIsActive(false);
		jobRepository.save(job);
		log.info("Job deleted successfully: {}", job.getTitle());	
		return "Job Deleted Successfully";
	}

	@Override
	public Job findByTitleAndPostedDateAndEmployer(String title, Date date, Employer employer) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//find job by Id
	@Override
	public Job findByJob(Long id)  {
		Optional<Job> job=jobRepository.findById(id);
		if(job.isPresent())
		{
			return job.get();
		}
		return null ;
	}


	@Override
	public Page<Job> searchJob(SearchJobDTO searchJobDTO, Integer PageNumber, Integer PageSize) throws CustomException
	{	
	Pageable pageable = PageRequest.of(PageNumber, PageSize);
	Page<Job> pages=jobRepository.findAll(GeneralSpecification.searchJobs(searchJobDTO), pageable);
	return pages;
	}


	@Override
	public void changeJobStatus(Long id, Boolean active) throws NotFoundException,CustomException{
		Job job=jobRepository.findById(id).orElseThrow(()->new NotFoundException("Job Not Found"));
		Boolean currentStatus=job.getIsActive();
		if (currentStatus.equals(active)) {
			String messageKey = active ? "job.already.active" : "job.already.inactive";
            String message = messageSource.getMessage(messageKey,null, LocaleContextHolder.getLocale());
            throw new CustomException(message);	   
  
		}
		job.setIsActive(active);
		jobRepository.save(job);	
	}

}
