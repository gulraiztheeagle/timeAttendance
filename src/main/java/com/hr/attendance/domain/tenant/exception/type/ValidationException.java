package com.hr.attendance.domain.tenant.exception.type;

@SuppressWarnings("serial")
public class ValidationException extends Exception{
	public ValidationException(){}
	
	public ValidationException(String message){
		super(message);
	}
}