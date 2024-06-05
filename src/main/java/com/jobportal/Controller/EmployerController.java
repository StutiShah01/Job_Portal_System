package com.jobportal.Controller;

import java.util.List;
import java.util.stream.Collectors;

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
import com.jobportal.Validators.EmployerValidator;
import com.jobportal.dto.EmployerDTO;
import com.jobportal.dto.JobDTO;
import com.jobportal.dto.SearchEmployerDTO;
import com.jobportal.entity.Employer;
import com.jobportal.entity.Job;
import com.jobportal.mappers.EmployerMapper;
import com.jobportal.mappers.JobMapper;
import com.jobportal.service.EmployerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/employer")
@Slf4j
@RequiredArgsConstructor
public class EmployerController {

	
	private final EmployerService employerService;
	private final EmployerValidator employerValidator;
	private final MessageSource messageSource;
	private final EmployerMapper employerMapper;
	private final JobMapper jobMapper;
	@InitBinder
	public void initbinder(WebDataBinder databinder){
		
			databinder.addValidators(employerValidator);
	}

	@PostMapping("/add")
	@Operation(summary = "Create a new employer", description = "Creates a new employer based on the provided details in the request body.")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Employer created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployerDTO.class))),
	        @ApiResponse(responseCode = "400", description = "Bad Request - Validation errors", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> addEmployer(@Valid @RequestBody EmployerDTO employerdto, BindingResult bindingResult)
	        throws CustomValidationException, NotFoundException, CustomException {

	    // Validate employer details using @Valid annotation and BindingResult
	    if (bindingResult.hasErrors()) {
	        log.info("Validation errors occurred during employer creation");
	        List<String> errors = bindingResult.getFieldErrors().stream()
	                .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
	                .collect(Collectors.toList());
	        throw new CustomValidationException(errors); // Throw custom exception with detailed error messages
	    }

	    // Call service to add 
	    EmployerDTO response = employerService.addOrUpdateEmployer(employerdto);
	    log.info("Employer Added Successfully");

	    // Create a successful response using GenericResponseHandlers
	    return new GenericResponseHandlers.Builder()
	            .setStatus(HttpStatus.CREATED) // Use CREATED for new resource
	            .setMessage("Employer Added Successfully")
	            .setData(response)
	            .create();
	}

	@PutMapping("/update")
	@Operation(summary = "Update an existing employer", description = "Updates the details of an existing employer based on the provided details in the request body.")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employer updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployerDTO.class))),
	        @ApiResponse(responseCode = "400", description = "Bad Request - Validation errors or missing ID", content = @Content(mediaType = "application/json")),
	        @ApiResponse(responseCode = "404", description = "Not Found - Employer with the provided ID does not exist", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> updateEmployer(@Valid @RequestBody EmployerDTO employerdto,BindingResult bindingResult)
	        throws NotFoundException, CustomException, CustomValidationException {

	    if (employerdto.getId() == null) {
	        String errorMessage = "Employer ID is required to update an existing employer.";
	        return new GenericResponseHandlers.Builder()
	                .setStatus(HttpStatus.BAD_REQUEST)
	                .setMessage(errorMessage)
	                .create();
	    }
	    if (bindingResult.hasErrors()) {
			 log.info("An error occured");

			List<String> errors = bindingResult.getFieldErrors().stream()
					.map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
					.collect(Collectors.toList());
			throw new CustomValidationException(errors); // Throw custom exception with error messages
			
			}

	    EmployerDTO updatedEmployer = employerService.addOrUpdateEmployer(employerdto);
	    log.info("Employer's Details Updated");
	    return new GenericResponseHandlers.Builder()
	            .setStatus(HttpStatus.OK)
	            .setMessage("Employer Updated Successfully")
	            .setData(updatedEmployer)
	            .create();
	}
	
	
	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Delete an employer", description = "Deletes an employer by their ID.")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employer deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
	        @ApiResponse(responseCode = "404", description = "Not Found - Employer with the provided ID does not exist", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> deleteEmployer(@PathVariable("id") Long id)
			throws NotFoundException {
		employerService.deleteEmployerById(id);
		log.info("Employer Deleted Successfully");
		return new GenericResponseHandlers.Builder()
								.setStatus(HttpStatus.OK)
								.setMessage(messageSource.getMessage("employer.deleted", null, LocaleContextHolder.getLocale()))
								.create();
	}

	@GetMapping("/employer-status")
	@Operation(summary = "Activate or Deactivate an Employer", description = "Activates or deactivates an employer based on their ID and the provided active flag.")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employer status updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
	        @ApiResponse(responseCode = "404", description = "Not Found - Employer with the provided ID does not exist", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<?> activeDeactiveEmployer(@RequestParam(required = true) Long id,
			@RequestParam(required = true) Boolean active) throws NotFoundException, CustomException {

		 employerService.changeEmployerStatus(id, active);
		return new GenericResponseHandlers.Builder().setMessage(messageSource.getMessage("status.updated", null, LocaleContextHolder.getLocale())).setStatus(HttpStatus.OK)
				.create();
	}

//	@GetMapping("/employer-filters")
//	public ResponseEntity<Object> getFilteredEmployers(@RequestParam(required = false) String name,
//			@RequestParam(required = false) String industry, @RequestParam(required = false) String companySize,
//			@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
//			@RequestParam(value = "pageSize", defaultValue = "2", required = false) Integer pageSize) {
//		List<EmployerDTO> employers = employerService.findEmployersByFilters(name, industry, companySize);
//		log.info("Searching employer based on name,industry or company size");
////    return ResponseEntity.ok(employers);
//		return new GenericResponseHandlers.Builder().setData(employers).setStatus(HttpStatus.OK).create();
//	}

//	@GetMapping("/getAll")
//	public ResponseEntity<Object> getAllEmployers(
//			@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
//			@RequestParam(value = "pageSize", defaultValue = "2", required = false) Integer pageSize) {
//		Pagination employerList = employerService.getEmployers(pageNumber - 1, pageSize);
//		log.info("Fetch Employers list");
//		return new GenericResponseHandlers.Builder().setData(employerList).setStatus(HttpStatus.OK).create();
//
//	}

	@GetMapping("getJobList/{emloyerId}")
	@Operation(summary = "Get Employer's Job List", responses = {
	        @ApiResponse(responseCode = "200", description = "Employer's job list found"),
	        @ApiResponse(responseCode = "404", description = "Employer not found or has no jobs")
	})
	public ResponseEntity<Object> getEmployersJobList(@PathVariable("emloyerId") Long emloyerId,
			@RequestParam(required = false, value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(required = false, value = "size", defaultValue = "2") Integer pageSize) throws NotFoundException {
		Page<Job> jobPages= employerService.getEmployersJobList(emloyerId,pageNumber-1,pageSize);
		List<Job> joblist=jobPages.getContent();
		List<JobDTO> jobdtolist = jobMapper.jobDTOs(joblist);
		
		log.info("Fetching joblist of particular Employer based on id");
		return new GenericResponseHandlers.Builder().setData(jobdtolist).setStatus(HttpStatus.OK)
				.setPageNumber(pageNumber).setPageSize(pageSize.longValue()).setTotalCount(jobPages.getTotalElements())
				.setTotalPages(jobPages.getTotalPages()).setHasNextPage(jobPages.hasNext()).setHasPreviousPage(jobPages.hasPrevious())
				.create();
	}


	@PostMapping("/search-employers")
	@Operation(summary =  "Search Employers")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employers found"),
	        @ApiResponse(responseCode = "400", description = "Bad request"),
	        @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<Object> searchEmployer(@RequestBody SearchEmployerDTO searchEmployerDTO,
			@RequestParam(required = false, value = "page", defaultValue = "1") Integer page,
			@RequestParam(required = false, value = "size", defaultValue = "2") Integer size) throws CustomException {
		Page<Employer> employerPages=employerService.searchEmployers(searchEmployerDTO, page - 1, size);
		List<Employer> employerList=employerPages.getContent();
		List<EmployerDTO> employerResponseList=employerMapper.employerDTos(employerList);
		return new GenericResponseHandlers.Builder().setData(employerResponseList).setStatus(HttpStatus.OK)
				.setPageNumber(page).setPageSize(size.longValue()).setTotalCount(employerPages.getTotalElements())
				.setTotalPages(employerPages.getTotalPages()).setHasNextPage(employerPages.hasNext()).setHasPreviousPage(employerPages.hasPrevious())
				.setMessage("List of Employers").create();
	}

	@GetMapping("/get-single-employer/{employerId}")
	@Operation( summary = "Get Single Employer Details")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Employer details found"),
	        @ApiResponse(responseCode = "404", description = "Employer not found")
	})
	public ResponseEntity<Object> getSingleEmployerDetails(@PathVariable("employerId") Long employerId)
			throws NotFoundException {
		EmployerDTO employerDTO = employerService.getSingleEmployer(employerId);
//		return new ResponseEntity<>(employerDTO,HttpStatus.OK);
		return new GenericResponseHandlers.Builder().setData(employerDTO).setStatus(HttpStatus.OK)
				.setMessage("Employer Status Updated Successfully").create();
	}

}
