package com.hr.attendance.domain.tenant.employee.dto;

import java.util.Date;

public class UpdateEmployeeRequestDTO {
	
	private Long id;
	private int approvalStatus;
	private String approvalRemarks;
	private Date approvalDate;
	private int approvedBy;
	
	public UpdateEmployeeRequestDTO(){}

	public UpdateEmployeeRequestDTO(Long id, int approvalStatus, String approvalRemarks, Date approvalDate,
			int approvedBy) {
		super();
		this.id = id;
		this.approvalStatus = approvalStatus;
		this.approvalRemarks = approvalRemarks;
		this.approvalDate = approvalDate;
		this.approvedBy = approvedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public int getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(int approvedBy) {
		this.approvedBy = approvedBy;
	}
	
	

}
