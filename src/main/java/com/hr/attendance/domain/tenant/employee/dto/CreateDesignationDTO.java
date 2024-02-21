package com.hr.attendance.domain.tenant.employee.dto;


public class CreateDesignationDTO {
	private String name;
	private boolean enabled;
	
	public CreateDesignationDTO(){}
  
	public CreateDesignationDTO(String name, boolean enabled){
   	 	this.name = name;
   	 	this.enabled = enabled;
	}
	
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


