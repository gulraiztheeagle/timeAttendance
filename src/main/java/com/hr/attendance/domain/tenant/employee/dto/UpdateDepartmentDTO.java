package com.hr.attendance.domain.tenant.employee.dto;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateDepartmentDTO {

	private long id;
	private String name;
	private boolean enabled;
	
	public UpdateDepartmentDTO(){}
	public UpdateDepartmentDTO(long id, String name, boolean enabled) {
		this.id = id;
		this.name = name;
		this.enabled = enabled;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@NotBlank(message="generic.name.blank")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
