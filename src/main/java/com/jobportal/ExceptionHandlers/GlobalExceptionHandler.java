package com.jobportal.ExceptionHandlers;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.jobportal.Exceptions.CustomException;
import com.jobportal.Exceptions.CustomValidationException;
import com.jobportal.Exceptions.NotFoundException;
import com.jobportal.GenericResponse.GenericResponseHandlers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
	
	
	
//	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
//	public ResponseEntity<?> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception){
//		ErrorMessage errorMessage= new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
//		log.info("MethodArgumentTypeMismatchException has occurred ");
//		return new ResponseEntity<>(errorMessage,HttpStatus.OK);
//	}
	
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	ResponseEntity<Object> handleControllerException(final HttpServletRequest request, final Throwable exception, final Locale locale) {
		HttpStatus status = null;
		String message = exception.getMessage();
		 if (exception instanceof MethodArgumentTypeMismatchException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			message = exception.getMessage();
		}  else if(exception instanceof NotFoundException){
			status = HttpStatus.NOT_FOUND;
			message = exception.getMessage();
		} else if(exception instanceof CustomException) {
			status = HttpStatus.NOT_FOUND;
			message = exception.getMessage();
		}else if(exception instanceof CustomValidationException) {
			status = HttpStatus.BAD_REQUEST;
		    CustomValidationException customValidationException = (CustomValidationException) exception;
		    List<String> errors = customValidationException.getErrors();
		    String errorMessage = String.join(" , ", errors);
		    message = errorMessage;
//			message=exception.getMessage();
		}else if(exception instanceof IllegalArgumentException ) {
			status = HttpStatus.BAD_REQUEST;
			message = exception.getMessage();
		}else if(exception instanceof RuntimeException)
		{
			status=HttpStatus.BAD_REQUEST;
			message=exception.getMessage();
		}
		else {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			message = "Something Went Wrong";
			log.error("exception : {}", exception);
		}
		 
		return new GenericResponseHandlers.Builder().setStatus(status).setMessage(message).create();
	}
//	@ExceptionHandler
//	public Map<String,String> handleException(MethodArgumentNotValidException ex)
//	{
//		Map<String,String> errormap = new HashMap<>();
//		ex.getBindingResult().getFieldErrors().forEach(error->{
//			errormap.put(error.getField(), error.getDefaultMessage());
//		});
////		errormap.put("status", "400");
//		return errormap;
//
//	}
//	
//	@ExceptionHandler(NotFoundException.class)
//	public Map<String,String> handleNotfoundException(NotFoundException ex)
//	{
//		Map<String,String> errormap = new HashMap<>();
//		errormap.put("Error Occured", ex.getMessage());
////		serrormap.put("status", "400");
//		return errormap;
//
//	}
//	@ExceptionHandler(CustomException.class)
//	public Map<String,String> handleCustomException(CustomException ex)
//	{
//		Map<String,String> errormap = new HashMap<>();
//		errormap.put("Error Occured", ex.getMessage());
////		serrormap.put("status", "400");
//		return errormap;
//
//	}
//	
//	@ExceptionHandler(CustomValidationException.class)
//	public ResponseEntity<ErrorMessage> handleCustomValidationException(CustomValidationException ex) {
//    List<String> errors = ex.getErrors();
//    String errorMessage = String.join(" , ", errors); // Join all errors into a single string
//    return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.OK);
//  }
//	@ExceptionHandler(IllegalArgumentException.class)
//	public Map<String,String> handleIllegalArgumentExceptionException(IllegalArgumentException ex)
//	{
//		Map<String,String> errormap = new HashMap<>();
//		errormap.put("Error Occured", ex.getMessage());
////		serrormap.put("status", "400");
//		return errormap;
//
//	}
	

}
