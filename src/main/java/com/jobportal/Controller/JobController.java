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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.CustomValidationException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.GenericResponse.GenericResponseHandlers;
import com.jobportal.Validators.JobValidator;
import com.jobportal.dto.JobDTO;
import com.jobportal.dto.SearchJobDTO;
import com.jobportal.entity.Job;
import com.jobportal.mappers.JobMapper;
import com.jobportal.pagination.Pagination;
import com.jobportal.service.JobService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/Job")
@Slf4j
public class JobController {
	
	@Autowired
	JobService jobservice;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	JobValidator jobValidator;
	
	@Autowired
	JobMapper jobMapper;
	
	@InitBinder
	public void initbinder(WebDataBinder databinder)
	{
		if(databinder.getTarget()!=null && JobDTO.class.equals(databinder.getTarget().getClass())) 
		{
			databinder.addValidators(jobValidator);
			
		}
	}

	@PostMapping("/add-jobs")
	 @Operation(summary = "Add a new job", description = "Adds a new job posting and returns the job details")
    @ApiResponse(responseCode = "201", description = "Job added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobDTO.class)))
	public ResponseEntity<?> addJobs(@Valid @RequestBody JobDTO jobdtoo,BindingResult bindingResult) throws NotFoundException, CustomException, CustomValidationException
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
		log.info("adding job for particular Employer");
		JobDTO jobdtoResponse=jobservice.addOrUpdateJob(jobdtoo);
//		return new ResponseEntity<>(jobdtoResponse,HttpStatus.OK);
		return new GenericResponseHandlers.Builder()
				  .setData(jobdtoResponse)
	              .setStatus(HttpStatus.CREATED)
	              .create();

	}
	@PutMapping("/update-job-details")
	 @Operation(summary = "Update job details", description = "Updates the details of an existing job posting")
    @ApiResponse(responseCode = "200", description = "Job details updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JobDTO.class)))
	public ResponseEntity<?> update(@Valid @RequestBody JobDTO jobdto) throws NotFoundException, CustomException
	{
		if (jobdto.getId() != null || jobdto.getId() != 0) {

		JobDTO jobdtoResponse=jobservice.addOrUpdateJob(jobdto);
//		jobservice.findByTitleAndPostedDate(jobdto.getTitle(), jobdto.getPostedDate());
//		return new ResponseEntity<>(jobdtoResponse,HttpStatus.OK);
		return new GenericResponseHandlers.Builder()
				  .setData(jobdtoResponse)
	              .setStatus(HttpStatus.OK)
	              .create();
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}
	@DeleteMapping("/delete-job/{id}")
	@Operation(summary = "Delete a job", description = "Deletes a job posting by ID")
    @ApiResponse(responseCode = "200", description = "Job deleted successfully")
	public ResponseEntity<?> deleteJob(@PathVariable("id") Long id) throws NotFoundException
	{
		String Status=jobservice.deleteJob(id);
//		return new ResponseEntity<>(Status,HttpStatus.OK);
		return new GenericResponseHandlers.Builder()
				  .setData(Status)
	              .setStatus(HttpStatus.OK)
	              .create();
	}
	@PostMapping("/search-job")
	 @Operation(summary = "Search jobs", description = "Searches for jobs based on various criteria and paginates the results")
    @ApiResponse(responseCode = "200", description = "Jobs retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pagination.class)))
	public ResponseEntity<?> searchJobs(@RequestBody SearchJobDTO searchJobDTO,
			@RequestParam(required = false,value = "page",defaultValue = "1")Integer page,
			@RequestParam(required = false,value = "size",defaultValue = "2")Integer size) throws CustomException
	{
		Page<Job> pages= jobservice.searchJob(searchJobDTO, page-1, size);
		List<Job> jobfilteredlist=pages.getContent();
		List<JobDTO> responseList=jobMapper.jobDTOs(jobfilteredlist);
		return new GenericResponseHandlers.Builder()
				  .setData(responseList)
				  .setPageNumber(page).setPageSize(size.longValue()).setTotalCount(pages.getTotalElements())
				  .setTotalPages(pages.getTotalPages()).setHasNextPage(pages.hasNext()).setHasPreviousPage(pages.hasPrevious())
	              .setStatus(HttpStatus.OK)
	              .create();
	}
	@GetMapping("/status")
	@Operation(summary = "Activate or Deactivate job", description = "Activates or deactivates an job based on their ID and the provided active flag.")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "job status updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
	        @ApiResponse(responseCode = "404", description = "Not Found - Employer with the provided ID does not exist", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<?> activeDeactiveJob(@RequestParam(required = true) Long id,
			@RequestParam(required = true) Boolean active) throws NotFoundException, CustomException {

		jobservice.changeJobStatus(id, active);
		return new GenericResponseHandlers.Builder()
				.setStatus(HttpStatus.OK)
				.setMessage(messageSource.getMessage("status.updated", null, LocaleContextHolder.getLocale()))
				.create();
	}
}
