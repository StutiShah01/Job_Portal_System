package com.jobportal.Exceptions;

import java.util.List;

public class CustomValidationException extends RuntimeException {
	 
	private static final long serialVersionUID = -7927542277546668426L;
	private List<String> errors;

	  public CustomValidationException(List<String> errors) {
	    this.errors = errors;
	  }

	  public List<String> getErrors() {
	    return errors;
	  }
}
