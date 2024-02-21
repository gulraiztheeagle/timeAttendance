package com.hr.attendance.domain.tenant.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PermissionDTO {
	private String id;
	private String description;
	
	public PermissionDTO(){
		
	}
	
	public PermissionDTO(String id, String desc){
		this.id = id;
		this.description = desc;
	}
	
	@JsonProperty("name")
	public String getAuthority() {
		return id;
	}
	
	public void setAuthority(String authority){
		this.id = authority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
