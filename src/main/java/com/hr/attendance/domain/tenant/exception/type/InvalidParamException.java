package com.hr.attendance.domain.tenant.exception.type;

@SuppressWarnings("serial")
public class InvalidParamException extends Exception{
	public InvalidParamException(){
		
	}
	
	public InvalidParamException(String message){
		super(message);
	}
}