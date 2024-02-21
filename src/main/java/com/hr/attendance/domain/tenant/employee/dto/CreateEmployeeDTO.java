package com.hr.attendance.domain.tenant.employee.dto;

public class CreateEmployeeDTO {

	private String firstName;
	private String lastName;
	private String fatherName;
	private String qualification;
	private String email;
	private String cnic;
	private String contactNo;
	private Boolean enabled;
	private int totalLeaves;
	private Long managerId;
	private Long departmentId;
	private Long designationId;
	private String employeeType;
	private String personalNo;
		
	public CreateEmployeeDTO(long id, String firstName, String lastName, String fatherName, String qualification,
			String email, String cnic, String contactNo, Boolean enabled, int totalLeaves, Long managerId,
			Long departmentId, Long designationId,String employeeType,String personalNo) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.fatherName = fatherName;
		this.qualification = qualification;
		this.email = email;
		this.cnic = cnic;
		this.contactNo = contactNo;
		this.enabled = enabled;
		this.totalLeaves = totalLeaves;
		this.managerId = managerId;
		this.departmentId = departmentId;
		this.designationId = designationId;
		this.employeeType = employeeType;
		this.personalNo = personalNo;
		
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
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
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
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public int getTotalLeaves() {
		return totalLeaves;
	}
	public void setTotalLeaves(int totalLeaves) {
		this.totalLeaves = totalLeaves;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public Long getDesignationId() {
		return designationId;
	}
	public void setDesignationId(Long designationId) {
		this.designationId = designationId;
	}
	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getPersonalNo() {
		return personalNo;
	}

	public void setPersonalNo(String personalNo) {
		this.personalNo = personalNo;
	}
	
	
	
	
}
