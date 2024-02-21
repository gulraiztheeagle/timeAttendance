package com.hr.attendance.domain.tenant.employee.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.hr.attendance.domain.tenant.audit.Auditable;

@Entity
@Audited
@Table(name = "employee")
@EntityListeners(AuditingEntityListener.class)
public class Employee extends Auditable<String> {



	private long id;
	private String firstName;
	private String lastName;
	private String fatherName;
	private String qualification;
	private String email;
	private String cnic;
	private String contactNo;
	private Boolean enabled;
	private Integer totalLeaves;
	private String employeeType;
	private String personalNo;

	private Employee manager;
	private Designation designation;
	private Department department;

	public Employee() {
	}

	public Employee(long id, String firstName, String lastName, String fatherName, String qualification, String email,
			String cnic, String contactNo, Boolean enabled, Integer totalLeaves, Employee manager, Designation designation,
			Department department,String employeeType,String personalNo) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fatherName = fatherName;
		this.qualification = qualification;
		this.email = email;
		this.cnic = cnic;
		this.contactNo = contactNo;
		this.enabled = enabled;
		this.totalLeaves = totalLeaves;
		this.manager = manager;
		this.designation = designation;
		this.department = department;
		this.employeeType = employeeType;
		this.personalNo = personalNo;
	}
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	@Transient
	public String getFullName(){
		return this.firstName + " "+this.lastName;
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

	public Integer getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(Integer totalLeaves) {
		this.totalLeaves = totalLeaves;
	}

	@Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@JoinColumn(name="manager_id", nullable = true)
	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	@Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@JoinColumn(name="designation_id", nullable = false)
	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	@Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@JoinColumn(name="department_id", nullable = false)
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
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
