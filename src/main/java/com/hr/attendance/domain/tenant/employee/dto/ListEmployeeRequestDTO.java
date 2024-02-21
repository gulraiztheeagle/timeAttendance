package com.hr.attendance.domain.tenant.employee.dto;

import java.util.Date;

public class ListEmployeeRequestDTO {

	private Long id;
	private String name;
	private String email;
	private Date submissionDate;
	private int requestType;
	private String reason;
	private Date startDate;
	private Date endDate;
	private String remarks;
	private int approvalStatus;
	private String approvalRemarks;
	
	public ListEmployeeRequestDTO() {}
	
	public ListEmployeeRequestDTO(Long id, String name, String email, Date submissionDate, int requestType, String reason,
			Date startDate, Date endDate, String remarks, int approvalStatus, String approvalRemarks) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.submissionDate = submissionDate;
		this.requestType = requestType;
		this.reason = reason;
		this.startDate = startDate;
		this.endDate = endDate;
		this.remarks = remarks;
		this.approvalStatus = approvalStatus;
		this.approvalRemarks = approvalRemarks;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Date getSubmissionDate() {
		return submissionDate;
	}
	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
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
	public int getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(int approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getApprovalRemarks() {
		return approvalRemarks;
	}
	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}
	
	
}
