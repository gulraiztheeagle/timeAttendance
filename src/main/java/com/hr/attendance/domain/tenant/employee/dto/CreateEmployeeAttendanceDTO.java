package com.hr.attendance.domain.tenant.employee.dto;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Temporal;

public class CreateEmployeeAttendanceDTO {
	private String type;
	
	public CreateEmployeeAttendanceDTO(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Temporal(TIMESTAMP)
	public Date getTimeStamp() {
		return new Date();
	}
}
