package com.hr.attendance.domain.tenant.employee.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class CreateEmployeeRequestDTO {

	private String name;
	private String email;
	private int requestType;	
	private String reason;
	private Date startDate;
	private Date endDate;
	private String remarks;
	
	public CreateEmployeeRequestDTO() {}
	
	public CreateEmployeeRequestDTO(String name, String email, int requestType, String reason, Date startDate, Date endDate,
			String remarks) {
		super();
		this.name = name;
		this.email = email;
		this.requestType = requestType;
		this.reason = reason;
		this.startDate = startDate;
		this.endDate = endDate;
		this.remarks = remarks;
	}
	


	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getRequestType() {
		return requestType;
	}
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
