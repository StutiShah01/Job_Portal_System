package com.jobportal.serviceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.ApplicationDTO;
import com.jobportal.dto.ApplicationWithCandidateDTO;
import com.jobportal.dto.EmployerWithJobDTO;
import com.jobportal.dto.JobWithApplicationDTO;
import com.jobportal.entity.Application;
import com.jobportal.entity.Candidate;
import com.jobportal.entity.Employer;
import com.jobportal.entity.Job;
import com.jobportal.enumconstants.ApplicationStatus;
import com.jobportal.mappers.ApplicationMapper;
import com.jobportal.mappers.EmployerMapper;
import com.jobportal.mappers.JobMapper;
import com.jobportal.pagination.Pagination;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.CandidateRepository;
import com.jobportal.repository.EmployerRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.EmployerService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	CandidateRepository candidateRepository;
	
	@Autowired
	ApplicationRepository applicationRepository;
	
	@Autowired
	EmployerRepository employerRepository;
	
	@Autowired
	ApplicationMapper applicationMapper;
	
	@Autowired
	EmployerService employerService;

	@Autowired
	JobMapper jobMapper;
	
	@Autowired
	EmployerMapper employerMapper;

	/**
	 * @param ApplicationDTO object which is to be added
	 * @return ApplicationDTO object which is saved to the database
	 * @throws throws NotFoundException 
	 */
	@Override
	@Transactional
	public ApplicationDTO addApplication(ApplicationDTO applicationDTO) throws Exception {
		log.info("Adding a new application");
		Application application =new Application();
		Optional<Candidate> temp=candidateRepository.findById(applicationDTO.getCandidateId());
		Candidate candidate=temp.get();
		if(!candidate.getIsActive().booleanValue())
		{
			throw new CustomException("Candidate is In-Active so can't apply");
		}
		Job job=jobRepository.findById(applicationDTO.getJobId()).orElseThrow(()->new NotFoundException("Job doesn't Exsist "));
		if(Date.from(Instant.now()).after(job.getExpiryDate()) || job.getIsActive()==false)
		{
			log.info("Application deadline passed or job is not active");
			throw new NotFoundException("You have missed the deadline");
		}
		application.setJob(job);
		application.setCandidate(candidate);
		application.setSubmittedDate(Date.from(Instant.now()));
		Application savedApplication =applicationRepository.save(application);
		ApplicationDTO applicationDTOResponse=new ApplicationDTO();
		applicationDTOResponse.setId(savedApplication.getId());
		applicationDTOResponse.setJobId(savedApplication.getJob().getId());
		applicationDTOResponse.setCandidateId(savedApplication.getCandidate().getId());
        log.info("Application added successfully");
		return applicationDTOResponse;
	}
    // Check if an application exists between a job and a candidate
	@Override
	public Boolean ExsistJobAndCandidate(Job job, Candidate candidate) {
		log.info("Checking if application exists for given job and candidate");
		return applicationRepository.existsByJobAndCandidate(job, candidate);
	}
	
    // Method to withdraw an application
	@Override
	public void withDrawApplication(Long id) throws NotFoundException {
		log.info("Withdrawing application");
		Application application=applicationRepository.findById(id).orElseThrow(()->new NotFoundException("Application Not Found"));
		application.setApplicationStatus(ApplicationStatus.WITHDRAW.name());
		application.setIsApplied(false);
		applicationRepository.save(application);
        log.info("Application withdrawn successfully");
//		return "Application Withdrawed Successfully";
	}
	
	
    // Get all applications for a candidate
	@Override
	public Page<Application> applicationlistforCandidate(Long candidateId,Integer pageNumber,Integer pageSize) throws NotFoundException, CustomException {
        log.info("Fetching applications for candidate");

        Pageable pageable = PageRequest.of(pageNumber,pageSize ); 
		Candidate candidate=candidateRepository.findById(candidateId).orElseThrow(()->new NotFoundException("Candidate Not Found"));
		Page<Application> pagesOfApplicationList=applicationRepository.findByCandidate(candidate,pageable);
		List<Application> applicationList=pagesOfApplicationList.getContent();
		List<Application> appliedApplicationList=new ArrayList<>();
		for(Application application:applicationList)
		{
			if(application.getIsApplied()==true)
			{
				appliedApplicationList.add(application);
			}
		}
		
		Page<Application> responsePages= new PageImpl<>(appliedApplicationList, pageable, appliedApplicationList.size());
		return responsePages;
		//		return pagesOfApplicationList;

	}
	// Get all applications for a job
	@Override
	public JobWithApplicationDTO applicationlistforJob(Long jobId) throws NotFoundException {
		log.info("Fetching applications for job");
		Job job=jobRepository.findById(jobId).orElseThrow(()->new NotFoundException("Job Not Found"));
		JobWithApplicationDTO jobWithApplicationDTO=jobMapper.toJobWithApplicationDTO(job);
		List<Application> applicationList=applicationRepository.findByJob(job);
		
		List<ApplicationWithCandidateDTO> applicationDTOList =new ArrayList<>();
		for(Application application:applicationList)
		{
			if(application.getIsApplied()==true)
			{
			ApplicationWithCandidateDTO app=new ApplicationWithCandidateDTO();
			app=applicationMapper.mapToApplicationWithCandidateDTO(application);
			applicationDTOList.add(app);

			}
		}
		jobWithApplicationDTO.setApplicationlist(applicationDTOList);
		return jobWithApplicationDTO;
	}

	@Override
	public EmployerWithJobDTO employersApplicationList(Long employerId) throws NotFoundException
	{

		Employer employer=employerRepository.findById(employerId).orElseThrow(()-> new NotFoundException("Employer Not Found"));
		EmployerWithJobDTO employerWithJobDTO=employerMapper.toEmployerWithJObDto(employer);
		List<JobWithApplicationDTO> joblist=new ArrayList<>();
		List<Job> jobs=jobRepository.findByEmployer_Id(employerId);
		for(Job job:jobs)
		{
			joblist.add(applicationlistforJob(job.getId()));
		}
		employerWithJobDTO.setJobs(joblist);
		return employerWithJobDTO;
		
		
	}

	// check for the existing application
	@Override
	public Boolean checkApplicationExists(Long applicationId) {
		log.info("checking for the application exists in database");
		return applicationRepository.existsById(applicationId);
	}
	
	public <T> Pagination getPaginationData(List<T> data, Pageable pageable) {
	    Pagination pagination = new Pagination();
	    pagination.setList(data);
	    pagination.setHasprevious(pageable.hasPrevious());
	    pagination.setHasNext(pageable.getPageNumber() + 1 < pagination.getTotalCount());
	    pagination.setTotalCount((int) data.stream().count()); // Get total count from the data list
	    pagination.setCurrentPage(pageable.getPageNumber() + 1); // Adjust for 1-based indexing
	    pagination.setPageSize(pageable.getPageSize());
	    return pagination;
	  }
	
}
