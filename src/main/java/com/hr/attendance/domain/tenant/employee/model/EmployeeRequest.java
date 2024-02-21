package com.hr.attendance.domain.tenant.employee.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.hr.attendance.domain.tenant.audit.Auditable;

@Entity
@Audited
@Table(name = "user_request")
@EntityListeners(AuditingEntityListener.class)
public class EmployeeRequest extends Auditable<String> {

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
	
	private Date approvalDate;
	private Employee employee;
	private Employee approvedBy;
	
	public EmployeeRequest() {
	};

	public EmployeeRequest(Long id, String name, String email, Date submissionDate, int requestType, String reason,
			Date startDate, Date endDate, String remarks, int approvalStatus, String approvalRemarks,
			Employee approvedBy, Date approvalDate) {
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
		this.approvedBy = approvedBy;
		this.approvalDate = approvalDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	@Temporal(TemporalType.DATE)
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

	@ManyToOne
	@JoinColumn(name="approved_by", nullable = true)
	public Employee getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Employee approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Temporal(TemporalType.DATE)
	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	@ManyToOne
	@JoinColumn(name="employee_id", nullable = false)
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
