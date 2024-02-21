package com.hr.attendance.domain.tenant.employee.dto;

import org.hibernate.validator.constraints.NotBlank;

public class CreateDepartmentDTO {
	
	private String name;
	private boolean enabled;
	
	public CreateDepartmentDTO(){}
	public CreateDepartmentDTO( String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
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
