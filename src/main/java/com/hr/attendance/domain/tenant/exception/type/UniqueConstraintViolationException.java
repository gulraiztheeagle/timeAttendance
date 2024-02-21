package com.hr.attendance.domain.tenant.exception.type;

@SuppressWarnings("serial")
public class UniqueConstraintViolationException extends Exception{
	public UniqueConstraintViolationException(String msg){
		super(msg);
	}
}