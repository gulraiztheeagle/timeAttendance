package com.hr.attendance.domain.tenant.security.exception;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class InvalidTokenException extends AuthenticationException {

	public InvalidTokenException(String msg) {
		super(msg);
	}
}
