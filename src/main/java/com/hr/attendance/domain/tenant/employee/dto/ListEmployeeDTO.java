package com.hr.attendance.domain.tenant.employee.dto;

import java.util.Date;
import java.util.List;


public class ListEmployeeDTO {
	private Long id;
	private String firstName;
	private String lastName;
	private String fatherName;
	private String qualification;
	private Long bankAccount;
	private String accountType;
	private Date joiningDate;
	private Date leavingDate;
	private String email;
	private String cnic;
	private String contactNo;
	private Boolean enabled;
	private String departmentName;
	private List<String> distributionNames;
	private String designationName;;
	private Long managerId;
	private String managerName;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFatherName() {
		return fatherName;
	}
	public String getFullName() {
		return this.getFirstName()+" "+this.getLastName();
	}
	
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public Long getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(Long bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public Date getJoiningDate() {
		return joiningDate;
	}
	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}
	public Date getLeavingDate() {
		return leavingDate;
	}
	public void setLeavingDate(Date leavingDate) {
		this.leavingDate = leavingDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCnic() {
		return cnic;
	}
	public void setCnic(String cnic) {
		this.cnic = cnic;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public Boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public List<String> getDistributionNames() {
		return distributionNames;
	}
	public void setDistributionNames(List<String> distributionNames) {
		this.distributionNames = distributionNames;
	}
	public String getDesignationName() {
		return designationName;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	
}
