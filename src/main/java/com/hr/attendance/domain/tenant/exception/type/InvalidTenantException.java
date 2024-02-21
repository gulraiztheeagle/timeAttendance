package com.hr.attendance.domain.tenant.exception.type;

import org.springframework.security.core.AuthenticationException;

public class InvalidTenantException extends AuthenticationException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTenantException(String message){
		super(message);
	}
}
