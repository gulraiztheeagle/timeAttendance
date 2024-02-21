package com.hr.attendance.domain.tenant.employee.dto;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateDesignationDTO {
	private long id;
	private String name;
	private boolean enabled;
	
	public UpdateDesignationDTO(){}
	public UpdateDesignationDTO(Long id,String name, boolean enabled){
		
		this.id = id;
		this.name = name;
		this.enabled = enabled;
		
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
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
