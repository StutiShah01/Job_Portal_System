package com.jobportal.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import com.jobportal.Validators.InterviewValidator;
import com.jobportal.dto.InterviewDTO;
import com.jobportal.service.InterviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/interview")
@Slf4j
public class InterviewController {
	
	@Autowired
	InterviewService interviewService;
	
	@Autowired
	InterviewValidator interviewValidator;
	
	@Autowired
	MessageSource messageSource;
	
	@InitBinder
	public void initbinder(WebDataBinder databinder)
	{
		if(databinder.getTarget()!=null && InterviewDTO.class.equals(databinder.getTarget().getClass())) 
		{
			databinder.addValidators(interviewValidator);
			
		}
	}
	
	@PostMapping("/schedule")
	@Operation(summary = "Schedule a new interview", description = "Schedule a new interview and return the interview details")
    @ApiResponse(responseCode = "200", description = "Interview scheduled successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InterviewDTO.class)))
	public ResponseEntity<?> scheduleInterview(@Valid @RequestBody InterviewDTO interviewdto,BindingResult bindingResult) throws CustomException, CustomValidationException
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
		InterviewDTO response=interviewService.scheduleInterview(interviewdto);
		log.info("Interview Scheduled successfully");
//		return new ResponseEntity<>(response,HttpStatus.OK);
		return new GenericResponseHandlers.Builder()
				  .setData(response)
	              .setStatus(HttpStatus.OK)
	              .create();
	}
	@PutMapping("/update-scheduled-interview")
	 @Operation(summary = "Update a scheduled interview", description = "Updates the details of an already scheduled interview")
    @ApiResponse(responseCode = "200", description = "Interview updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InterviewDTO.class)))
	public ResponseEntity<?> updateInterview(@Valid @RequestBody InterviewDTO interviewdto,BindingResult bindingResult) throws CustomException, CustomValidationException
	{
		if (bindingResult.hasErrors()) {
		List<String> errors = bindingResult.getFieldErrors().stream()
		        .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
		        .collect(Collectors.toList());
		    throw new CustomValidationException(errors); 
		}
		InterviewDTO response=interviewService.updateInterview(interviewdto);
		log.info("Interview updated successfully");
//		return new ResponseEntity<>(response,HttpStatus.OK);
		return new GenericResponseHandlers.Builder()
				  .setData(response)
	              .setStatus(HttpStatus.OK)
	              .create();
	}
	@DeleteMapping("/cancel/{interviewId}")
	@Operation(summary = "Cancel an interview", description = "Cancels a previously scheduled interview")
    @ApiResponse(responseCode = "200", description = "Interview cancelled successfully")
	public ResponseEntity<?> cancelInterview(@PathVariable("interviewId") long interviewId ) throws NotFoundException, CustomException
	{
		interviewService.cancelInterview(interviewId);
		log.info("Interview Cancelled Successfully");
//		return new ResponseEntity<>(status,HttpStatus.OK);
		return new GenericResponseHandlers.Builder()
				  .setMessage(messageSource.getMessage("interview.cancelled.successfully", null, LocaleContextHolder.getLocale()))
	              .setStatus(HttpStatus.OK)
	              .create();
	}
	@GetMapping("/interview-status")
	@Operation(summary = "Change interview status", description = "Changes the status of an existing interview")
    @ApiResponse(responseCode = "200", description = "Interview status changed successfully")
	public ResponseEntity<?> changeInterviewStatus(@RequestParam(required=true) long id,
													@RequestParam(required=true) String status) throws NotFoundException
	{
		
		String responseStatus=interviewService.changeInterviewStatus(id, status);
//		return new ResponseEntity<>(responseStatus,HttpStatus.OK);
		return new GenericResponseHandlers.Builder()
				  .setData(responseStatus)
	              .setStatus(HttpStatus.OK)
	              .create();
	}
}
