package com.hr.attendance.domain.tenant.security.dto;

import java.io.Serializable;
import java.util.List;

public class AccessTokenInfo implements Serializable{
	private static final long serialVersionUID = 8353038823118139182L;
	
	private final String accessToken;
	private final String tokenType;
	private final String username;
	private final List<String> roles;
	private final List<String> permissions;
	private final String companyName;
	private final String name;
	private final String designation;
	private final String department;
	private final String email;
	private final String domain;
	private final boolean hasDistributions;
	private long refEmployeeId;
	
	public AccessTokenInfo(String accessToken, String tokenType, String username, List<String> roles,
			List<String> permissions, String companyName, String name, String designation, String department,
			String email, String domain, boolean hasDistributions,long refEmployeeId) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.username = username;
		this.roles = roles;
		this.permissions = permissions;
		this.companyName = companyName;
		this.name = name;
		this.designation = designation;
		this.department = department;
		this.email = email;
		this.domain = domain;
		this.hasDistributions = hasDistributions;
		this.refEmployeeId = refEmployeeId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public String getUsername() {
		return username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getName() {
		return name;
	}

	public String getDesignation() {
		return designation;
	}

	public String getDepartment() {
		return department;
	}

	public String getEmail() {
		return email;
	}

	public String getDomain() {
		return domain;
	}
	
	public boolean getHasDistributions(){
		return this.hasDistributions;
	}

	public long getRefEmployeeId() {
		return refEmployeeId;
	}

	public void setRefEmployeeId(long refEmployeeId) {
		this.refEmployeeId = refEmployeeId;
	}
	
}