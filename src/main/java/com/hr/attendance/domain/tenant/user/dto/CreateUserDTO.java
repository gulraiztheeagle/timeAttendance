package com.hr.attendance.domain.tenant.user.dto;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

public class CreateUserDTO {
	private String username;
	private long employeeId;
	
	private List<Long> roleIds;

	public CreateUserDTO() {

	}
	
	public CreateUserDTO(String username, long employeeId) {
		this.username = username;
		this.setEmployeeId(employeeId);
	}
	
	@NotBlank(message="user.createUser.emptyUsername")
	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}
}
