package com.hr.attendance.domain.tenant.exception.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
//@Table(name="app_log")
public class AppLog {
	private Date timestamp;
	private String username;
	private String tenantName;
	private String requestUrl;
	private String requestBody;
	private String requestHeaders;
	private String requestMethod;
	private String exceptionType;
	private String exceptionMsg;
	private String stackTrace;
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	public String getRequestHeaders() {
		return requestHeaders;
	}
	public void setRequestHeaders(String requestHeaders) {
		this.requestHeaders = requestHeaders;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public String getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public String getExceptionMsg() {
		return exceptionMsg;
	}
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	
	
	
}
