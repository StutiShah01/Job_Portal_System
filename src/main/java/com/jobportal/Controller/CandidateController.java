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
import com.jobportal.Validators.CandidateValidator;
import com.jobportal.dto.CandidateDTO;
import com.jobportal.dto.InterviewDTO;
import com.jobportal.dto.SearchCandidateDTO;
import com.jobportal.entity.Candidate;
import com.jobportal.entity.Interview;
import com.jobportal.mappers.CandidateMapper;
import com.jobportal.mappers.InterviewMapper;
import com.jobportal.pagination.Pagination;
import com.jobportal.service.CandidateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class CandidateController {
	
	@Autowired
	CandidateService candidateService;
	
	@Autowired
	CandidateValidator candidateValidator;
	
	@Autowired 
	MessageSource messageSource;
	
	@Autowired
    EntityManager entityManager;
	
	@Autowired
	InterviewMapper interviewMapper;
	
	@Autowired
	CandidateMapper candidateMapper;
	
	@InitBinder
	public void initbinder(WebDataBinder databinder)
	{
		if(databinder.getTarget()!=null && CandidateDTO.class.equals(databinder.getTarget().getClass())) 
		{
			databinder.addValidators(candidateValidator);
			
		}
	}
	
	@PostMapping("/add")
	@Operation(summary = "Add a new candidate", description = "Add a new candidate and return the saved candidate details")
	@ApiResponse(responseCode = "201", description = "Candidate created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDTO.class)))
	public ResponseEntity<?> addCandidate(@Valid @RequestBody CandidateDTO candidatedto,BindingResult bindingResult) throws NotFoundException, CustomException, CustomValidationException
	{
		if (bindingResult.hasErrors()) {

		List<String> errors = bindingResult.getFieldErrors().stream()
		        .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
		        .collect(Collectors.toList());
		    throw new CustomValidationException(errors); 
		}
		
		CandidateDTO candidatedtoResponse=candidateService.addOrUpdateCandidate(candidatedto);
		log.info("Candidate Saved successfully");
//		return new ResponseEntity<>(candidatedtoResponse,HttpStatus.OK);
		return new GenericResponseHandlers.Builder()
				  .setData(candidatedtoResponse)
              .setStatus(HttpStatus.CREATED)
              .create();
	}
	
	@PutMapping("/update")
	@Operation(summary = "Update an existing candidate", description = "Update details of an existing candidate by ID")
	@ApiResponse(responseCode = "200", description = "Candidate updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDTO.class)))
	public ResponseEntity<?> updateCandidate(@Valid @RequestBody CandidateDTO candidateDTO,BindingResult bindingResult) throws NotFoundException, CustomException, CustomValidationException
	{
		if (candidateDTO.getId() == null) {
	        String errorMessage = "Candidate ID is required to update an existing candidate.";
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
			CandidateDTO response=candidateService.addOrUpdateCandidate(candidateDTO);
			log.info("Candidate's Details updated");
//			return new ResponseEntity<>(candidateDTOResponse,HttpStatus.OK);
			return new GenericResponseHandlers.Builder()
					  .setData(response)
					  .setMessage("Candidate Updated Successfully")
					  .setStatus(HttpStatus.OK)
					  .create();
		
	}
	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Delete a candidate", description = "Delete a candidate using their ID")
	@ApiResponse(responseCode = "200", description = "Candidate deleted successfully")
	public ResponseEntity<?> deleteCandidate(@PathVariable("id") Long id) throws NotFoundException
	{
		candidateService.deleteCandidateById(id);
		log.info("Candidate Deleted");
//		return new ResponseEntity<>(status,HttpStatus.OK);
		return new GenericResponseHandlers.Builder()
				  .setMessage(messageSource.getMessage("candidate.deleted", null, LocaleContextHolder.getLocale()))
              .setStatus(HttpStatus.OK)
              .create();
		
	}
	@GetMapping("/getAll")
	@Operation(summary = "List all candidates", description = "Get a list of all candidates with pagination options")
	@ApiResponse(responseCode = "200", description = "List of candidates retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pagination.class)))
	public ResponseEntity<?> getAllCandiates(
			@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "2", required = false) Integer pageSize
			)
	{
		Pagination candidateList=candidateService.getCandidates(pageNumber-1,pageSize);
		log.info("Fetch Employers list");
		return new GenericResponseHandlers.Builder()
				  .setData(candidateList)
	              .setStatus(HttpStatus.OK)
	              .create();
	} 
	
//
//	@GetMapping("/search")
//	@Operation(summary = "Search candidates", description = "Search candidates based on name or skills")
//	@ApiResponse(responseCode = "200", description = "Search results retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
//    public ResponseEntity<?> searchCandidates(@RequestParam(value = "name", required = false) String name,
//                                                           @RequestParam(value = "skills", required = false) String skills) {
//        List<CandidateDTO> candidates = candidateService.searchCandidates(name, skills);
//        log.info("Searching candidate based on name or skills ");
////        return ResponseEntity.ok(candidates);
//		    return new GenericResponseHandlers.Builder()
//					  .setData(candidates)
//			          .setStatus(HttpStatus.OK)
//			          .create();
//		}
	
	@GetMapping("/Scheduled-interview-list/{candidateId}")
	@Operation(summary = "List scheduled interviews for a candidate", description = "Retrieve a list of scheduled interviews for a given candidate with pagination")
	@ApiResponse(responseCode = "200", description = "Scheduled interviews list retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
	public ResponseEntity<?> listofScheduledInterview(@PathVariable("candidateId") Long candidateId,
			@RequestParam(required = false, value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(required = false, value = "size", defaultValue = "2") Integer pageSize)
	{
		 Page<Interview> pages=candidateService.scheduleInterviewListOfCandidate(candidateId,pageNumber-1,pageSize);
		 List<Interview> listofScheduledInterview=pages.getContent();
		 List<InterviewDTO> responselist=interviewMapper.interviewDTOList(listofScheduledInterview);

		return new GenericResponseHandlers.Builder()
				  .setData(responselist)
				  .setPageNumber(pageNumber).setPageSize(pageSize.longValue()).setTotalCount(pages.getTotalElements())
				  .setTotalPages(pages.getTotalPages()).setHasNextPage(pages.hasNext()).setHasPreviousPage(pages.hasPrevious())
				  .setStatus(HttpStatus.OK)
				  .create();
		
	}
	@PostMapping("/search-candidate")
	@Operation(summary = "Search for candidates with detailed options", description = "Search for candidates based on detailed search criteria with pagination")
	@ApiResponse(responseCode = "200", description = "Search results retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pagination.class)))
	public ResponseEntity<?> searchCandidate(@RequestBody SearchCandidateDTO searchCandidateDTO,
			@RequestParam(required = false,value = "page",defaultValue = "1")Integer page,
			@RequestParam(required = false,value = "size",defaultValue = "2")Integer size)
	{
		Page<Candidate> pages= candidateService.searchCandidate(searchCandidateDTO, page-1, size);
		List<Candidate> filteredCandidateList=pages.getContent();
		List<CandidateDTO> responseList= candidateMapper.candidateDTOs(filteredCandidateList);
		return new GenericResponseHandlers.Builder()
				  .setPageNumber(page).setPageSize(size.longValue()).setTotalCount(pages.getTotalElements())
				  .setTotalPages(pages.getTotalPages()).setHasNextPage(pages.hasNext()).setHasPreviousPage(pages.hasPrevious())
				  .setData(responseList)
	              .setStatus(HttpStatus.OK)
	              .create();
	}
	
	
	@GetMapping("/get-single-candidate/{candidateId}")
	@Operation(summary = "Get details of a single candidate", description = "Retrieve details of a single candidate using their ID")
	@ApiResponse(responseCode = "200", description = "Candidate details retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDTO.class)))
	public ResponseEntity<?> getSingleCandidateDetails(@PathVariable("candidateId") Long candidateId) throws NotFoundException
	{
		CandidateDTO response=candidateService.getSingleCandidate(candidateId);
		return new GenericResponseHandlers.Builder()
				  .setData(response)
	              .setStatus(HttpStatus.OK)
	              .create();
	}
	@GetMapping("/candidate-status")
	@Operation(summary = "Activate or deactivate a candidate", description = "Change the activation status of a candidate")
	@ApiResponse(responseCode = "200", description = "Candidate status updated successfully")
	public ResponseEntity<?> activeDeactiveCandidate(@RequestParam(required=true) Long id ,
													@RequestParam(required=true) Boolean active) throws NotFoundException, CustomException
	{
		
		candidateService.changeCandidateStatus(id, active);
		return new GenericResponseHandlers.Builder()
				  .setMessage(messageSource.getMessage("status.updated", null, LocaleContextHolder.getLocale()))
		          .setStatus(HttpStatus.OK)
		          .create();
	}
}
	