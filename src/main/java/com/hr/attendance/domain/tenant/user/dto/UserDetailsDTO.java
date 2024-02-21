package com.hr.attendance.domain.tenant.user.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

public class UserDetailsDTO {
	private Long id;
	private String name;
	private String username;
	private String mobileNo;
	private String email;
	private String skypeId;
	private Date createdOn;
	private String createdBy;
	
	private boolean enabled;
	private boolean accountLocked;
	private boolean accountExpired;
	private boolean credentialsExpired;

	private String designation;
	private String reportingTo;
	private String role;
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = UserRoleDTO.class)
	private List<UserRoleDTO> roles;
	
	public UserDetailsDTO(){
		
	}
	public UserDetailsDTO(Long id, String name, String username, String mobileNo, String email, String skypeId,
			Date createdOn, String createdBy, boolean enabled, boolean accountLocked, boolean accountExpired,
			boolean credentialsExpired,String role) {

		this.id = id;
		this.name = name;
		this.username = username;
		this.mobileNo = mobileNo;
		this.email = email;
		this.skypeId = skypeId;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.enabled = enabled;
		this.accountLocked = accountLocked;
		this.accountExpired = accountExpired;
		this.credentialsExpired = credentialsExpired;
		this.role=role;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date timeStamp) {
		this.createdOn = timeStamp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<UserRoleDTO> getRoles() {
		if (this.roles == null)
			this.roles = new ArrayList<UserRoleDTO>();
		return this.roles;
	}

	public void setRoles(List<UserRoleDTO> rolesDtos) {
		this.roles = rolesDtos;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getReportingTo() {
		return reportingTo;
	}

	public void setReportingTo(String reportingTo) {
		this.reportingTo = reportingTo;
	}

	public boolean isAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
