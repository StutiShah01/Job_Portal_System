package com.jobportal.Exceptions;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
	

	public NotFoundException(String msg)
	{
			super(msg);
	}
}
