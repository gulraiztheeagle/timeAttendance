package com.hr.attendance.domain.tenant.common;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hr.attendance.domain.tenant.security.RequestContext;


public class ServletRequestThreadLocalDestroyer implements ServletRequestListener {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void requestDestroyed(ServletRequestEvent arg0) {
		RequestContext.clear();
		//TenantSchemaContext.clear();
	}

	@Override
	public void requestInitialized(ServletRequestEvent arg0) {
	}
}