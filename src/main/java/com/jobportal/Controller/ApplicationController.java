package com.jobportal.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.CustomValidationException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.GenericResponse.GenericResponseHandlers;
import com.jobportal.Validators.ApplicationValidator;
import com.jobportal.dto.ApplicationDTO;
import com.jobportal.dto.ApplicationWithJobDTO;
import com.jobportal.dto.EmployerWithJobDTO;
import com.jobportal.dto.JobWithApplicationDTO;
import com.jobportal.entity.Application;
import com.jobportal.mappers.ApplicationMapper;
import com.jobportal.pagination.Pagination;
import com.jobportal.service.ApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/application")
public class ApplicationController {
	
	@Autowired
	ApplicationService applicationservice;
	
	@Autowired
	ApplicationValidator applicationValidator;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	ApplicationMapper applicationMapper;
	
	@InitBinder
	public void initbinder(WebDataBinder databinder)
	{
		databinder.addValidators(applicationValidator);
	}
	
	@PostMapping("/add")
	@Operation(summary = "Add a new application", description = "Submits a new job application and returns the application details")
    @ApiResponse(responseCode = "200", description = "Application added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationDTO.class)))
	public ResponseEntity<?> addApplication(@Valid @RequestBody ApplicationDTO applicationdto,BindingResult bindingResult) throws NotFoundException, Exception
	{
		if (bindingResult.hasErrors()) {
//			log.info("An error occured");
//			return new ResponseEntity<>(bindingResult.getFieldErrors().stream().map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
//					.collect(Collectors.joining("\n")), HttpStatus.OK);
		List<String> errors = bindingResult.getFieldErrors().stream()
		        .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
		        .collect(Collectors.toList());
		    throw new CustomValidationException(errors); 
		}
		
		ApplicationDTO response=applicationservice.addApplication(applicationdto);
		log.info("Application Saved Successfully");
		return new GenericResponseHandlers.Builder()
				  .setData(response)
		          .setStatus(HttpStatus.OK)
		          .create();
	}
	@DeleteMapping("/withdraw-application/{id}")
	@Operation(summary = "Withdraw an application", description = "Withdraws a previously submitted job application")
    @ApiResponse(responseCode = "200", description = "Application withdrawn successfully")
	public ResponseEntity<?> withDrawApplication(@PathVariable("id") Long id) throws NotFoundException
	{
		
		applicationservice.withDrawApplication(id);
		log.info("Application withdrawed successfully");
		return new GenericResponseHandlers.Builder()
				  .setMessage(messageSource.getMessage("application.withdraw.successfully", null, LocaleContextHolder.getLocale()))
	              .setStatus(HttpStatus.OK)
	              .create();
	}
	
	@GetMapping("/get-app-of-candidates/{candidateId}")
	@Operation(summary = "Get applications of a candidate", description = "Retrieves all applications submitted by a specific candidate with pagination")
    @ApiResponse(responseCode = "200", description = "List of applications retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pagination.class)))
	public ResponseEntity<?> getCandidateApplicationList(@PathVariable("candidateId") long candidateId,
			@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "2", required = false) Integer pageSize
			) throws NotFoundException, CustomException
	{
		
		log.info("Fetching list of application for particular candidate");
		Page<Application> pages=applicationservice.applicationlistforCandidate(candidateId,pageNumber-1,pageSize);
		List<Application> applicationList=pages.getContent();
		List<ApplicationWithJobDTO> responseList=applicationMapper.applicationWithJobDTOsList(applicationList);
		return new GenericResponseHandlers.Builder()
				  .setData(responseList)
				  .setPageNumber(pageNumber).setPageSize(pageSize.longValue()).setTotalCount(pages.getTotalElements())
				  .setTotalPages(pages.getTotalPages()).setHasNextPage(pages.hasNext()).setHasPreviousPage(pages.hasPrevious())
	              .setStatus(HttpStatus.OK)
	              .create();
	}
	
	@GetMapping("/get-app-of-job/{jobId}")
	@Operation(summary = "Get applications for a job", description = "Retrieves all applications submitted for a specific job")
    @ApiResponse(responseCode = "200", description = "List of applications retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobWithApplicationDTO.class)))
	public ResponseEntity<?> getJobApplicationList(@PathVariable("jobId") long jobId
			) throws NotFoundException
	{
		log.info("Fetching list of application for particular job");
		JobWithApplicationDTO applicationdto=applicationservice.applicationlistforJob(jobId);
		return new GenericResponseHandlers.Builder()
				  .setData(applicationdto)
	              .setStatus(HttpStatus.OK)
	              .create();
	}
	
		
	@GetMapping("/get-app-of-emp/{id}")
	@Operation(summary = "Get applications of an employer", description = "Retrieves all applications submitted to jobs posted by a specific employer")
    @ApiResponse(responseCode = "200", description = "List of applications retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployerWithJobDTO.class)))
	public ResponseEntity<?> getlist(@PathVariable("id") long id) throws NotFoundException
	{
		log.info("Fetching list of application for particular employer");
		EmployerWithJobDTO employerWithJobDTO=applicationservice.employersApplicationList(id);
// 		return new ResponseEntity<>(employerWithJobDTO,HttpStatus.OK); 
		return new GenericResponseHandlers.Builder()
				  .setData(employerWithJobDTO)
	              .setStatus(HttpStatus.OK)
	              .create();
	}
	
	

}
