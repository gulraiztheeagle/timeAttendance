package com.hr.attendance.domain.tenant.employee.dto;

public class ViewDepartmentDTO {

	private long id;
	private String name;
	private boolean enabled;
	
	public ViewDepartmentDTO(){}
	public ViewDepartmentDTO(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
