package com.jobportal.serviceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.dto.EmployerDTO;
import com.jobportal.dto.GeneralSpecification;
import com.jobportal.dto.SearchEmployerDTO;
import com.jobportal.entity.Employer;
import com.jobportal.entity.Job;
import com.jobportal.mappers.EmployerMapper;
import com.jobportal.pagination.Pagination;
import com.jobportal.repository.EmployerRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.service.EmployerService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {
	
	private final EmployerMapper employerMapper;

	private final EmployerRepository employerRepository;

	private final JobRepository jobRepository;

	private final EntityManager entityManager;
	
	private final MessageSource messageSource;
	
	@Override
	public EmployerDTO addOrUpdateEmployer(EmployerDTO employerDto) throws NotFoundException, CustomException {
		EmployerDTO dtoToReturn = new EmployerDTO();

		if (employerDto.getId() == null || employerDto.getId() == 0) {
			Employer employerToSave = employerMapper.toEmployer(employerDto);
//			employerToSave.setName(employerDto.getName();
			employerRepository.save(employerToSave);
			dtoToReturn = employerMapper.toEmployerDTO(employerToSave);

		} else {
			Optional<Employer> employer = employerRepository.findById(employerDto.getId());
			if (employer.isEmpty()) {
				throw new NotFoundException("Employer Does Not Exsist");
			}

			Employer updateEmployer = employer.get();
			if (!updateEmployer.getIsActive().booleanValue()) {
				throw new CustomException("Employer is In-Active right now");
			}
			updateEmployer.setName(employerDto.getName());
			updateEmployer.setEmail(employerDto.getEmail());
			updateEmployer.setIndustry(employerDto.getIndustry());
			updateEmployer.setDescription(employerDto.getDescription());
			updateEmployer.setCompanySize(employerDto.getCompanySize());
			updateEmployer.setWebsite(employerDto.getWebsite());
			updateEmployer.setLocation(employerDto.getLocation());
			employerRepository.save(updateEmployer);
			dtoToReturn = employerMapper.toEmployerDTO(updateEmployer);
		}
		return dtoToReturn;
	}


	@Override
	public void deleteEmployerById(Long id) throws NotFoundException {
		Optional<Employer> employer = employerRepository.findById(id);
		if (employer.isEmpty()) {
			throw new NotFoundException("Employer Does Not Exsist");
		}
		Employer employerToDelete = employer.get();
		employerToDelete.setIsActive(false);
		employerRepository.save(employerToDelete);
		
	}

	public List<EmployerDTO> findEmployersByFilters(String name, String industry, String companySize) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employer> criteriaQuery = criteriaBuilder.createQuery(Employer.class);
		Root<Employer> employerRoot = criteriaQuery.from(Employer.class);

		Predicate predicate = null;

		// Build predicates based on provided filter parameters
		if (name != null) {
			predicate = criteriaBuilder.like(criteriaBuilder.lower(employerRoot.get("name")),
					"%" + name.toLowerCase() + "%");
		}
		if (industry != null) {
			predicate = criteriaBuilder.and(predicate,
					criteriaBuilder.like(employerRoot.get("industry"), "%" + industry + "%"));
		}
		if (companySize != null) {
			predicate = criteriaBuilder.and(predicate,
					criteriaBuilder.equal(employerRoot.get("companySize"), companySize));
		}

		// Apply predicate if any filters are provided
		if (predicate != null) {
			criteriaQuery.select(employerRoot).where(predicate);
		} else {
			// Handle case where no filters are provided (return all employers)
			criteriaQuery.select(employerRoot);
		}

		List<Employer> employers = entityManager.createQuery(criteriaQuery).getResultList();
		List<EmployerDTO> employersdto = new ArrayList<>();
		for (Employer emp : employers) {
			employersdto.add(employerMapper.toEmployerDTO(emp));
		}
		return employersdto;
	}

	@Override
	public Pagination getEmployers(Integer pageNo, Integer pageSize) {
		Pagination pagination = new Pagination();
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Employer> pageSource = employerRepository.findAll(pageable);
//		Page<Employer> pageSource =employerRepository.findByNameContaining(pageable, name);
		List<Employer> allEmployer = pageSource.getContent();
		List<EmployerDTO> responselist = new ArrayList<>();
		for (Employer employer : allEmployer) {
			responselist.add(employerMapper.toEmployerDTO(employer));
		}
		pagination.setList(responselist);
		pagination.setCurrentPage(pageable.getPageNumber() + 1);
		pagination.setHasNext(pageSource.hasNext());
		pagination.setHasprevious(pageSource.hasPrevious());
		pagination.setPageSize(pageable.getPageSize());
		pagination.setTotalCount(responselist.size());
		log.info("fetching Employer's data");
		return pagination;
	}

	@Override
	public Page<Job> getEmployersJobList(Long id, Integer PageNumber, Integer PageSize) throws NotFoundException {
		Employer employers = employerRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Employer doesn't exsist"));
		Pageable pageable = PageRequest.of(PageNumber, PageSize);
		Page<Job> temp= jobRepository.findByEmployer(employers,pageable);
		List<Job> jobs=temp.getContent();
		List<Job> finalJobList=new ArrayList<>();
		for (Job job : jobs) {
			if (job.getIsActive() == true && job.getExpiryDate().after(Date.from(Instant.now()))) {
				finalJobList.add(job);
			}

		}
		Page<Job> jobPages=new PageImpl<>(finalJobList, pageable, finalJobList.size());

		return jobPages;
	}

	public Page<EmployerDTO> findEmployer(String name, String industryType, Integer companySize, Pageable pageable) {
//		Page<Employer> employers= employerRepository.findAll(Specification.where(EmployerSpecification.hasName(name).and(EmployerSpecification.hasIndustryType(industryType)).and(EmployerSpecification.hasCompanySize(companySize))), pageable);
//		return employers.map(employerMapper::toEmployerDTO);
		return null;
	}

	@Override
	public Page<Employer> searchEmployers(SearchEmployerDTO searchEmployerDTO, Integer PageNumber, Integer PageSize)
			throws CustomException {

		
		Pageable pageable = PageRequest.of(PageNumber, PageSize);
		Page<Employer> EmployerPages = employerRepository.findAll(GeneralSpecification.searchEmployer(searchEmployerDTO),
				pageable);
		
		return EmployerPages;
	}

	@Override
	public EmployerDTO getSingleEmployer(Long employerId) throws NotFoundException {
		Employer employer = getEmployerDetails(employerId);
		EmployerDTO employerdto = employerMapper.toEmployerDTO(employer);
		return employerdto;
	}

	private Employer getEmployerDetails(long employerId) throws NotFoundException {
		return employerRepository.findById(employerId)
				.orElseThrow(() -> new NotFoundException("Employer Not Found"));
	}

	@Override
	public void changeEmployerStatus(Long id, Boolean active) throws NotFoundException, CustomException {
		
		Employer employer = getEmployerDetails(id);
		Boolean currentStatus=employer.getIsActive();		
		if (currentStatus.equals(active)) {
			String messageKey = active ? "employer.already.active" : "employer.already.inactive";
            String message = messageSource.getMessage(messageKey,null, LocaleContextHolder.getLocale());
            throw new CustomException(message);	   
            
		}
		// Update the employer's status
	    employer.setIsActive(active);
	    employerRepository.save(employer);

	    // If deactivating employer, deactivate all associated jobs
	    if (!active.booleanValue()) {
	        List<Job> jobList = jobRepository.findByEmployer(employer);
	        jobList.forEach(job -> job.setIsActive(false));
	        jobRepository.saveAll(jobList);
	    }
	}

	
	@Override
	public Boolean validateEmail(Long id,String email) 
	{
		 if (id == null) {
			    return employerRepository.findByEmail(email).isPresent();
		}
		else {
			    return employerRepository.findByIdIsNotAndEmailIgnoringCase(id, email).isPresent();
			  }
	}

	@Override
	public Boolean validateWebsite(Long id, String website)
	{
		if (id == null) {
		    return employerRepository.findByWebsite(website).isPresent();
		}
		else {
			    return employerRepository.findByIdIsNotAndWebsite(id, website).isPresent();
			  }
	}
}
