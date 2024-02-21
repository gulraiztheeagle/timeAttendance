package com.hr.attendance.domain.tenant.exception.type;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class TransactionException extends AuthenticationException{

	public TransactionException(String msg) {
		super(msg);
	}
}
