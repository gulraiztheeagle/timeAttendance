package com.hr.attendance.domain.tenant.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="access_token")
public class AccessToken {
	private String tokenId;
	private byte[] token;
	private byte[] authentication;
	private byte[] tenant;
	private String username;
	private String requesterIpAddress;
	private String requesterDeviceType;
	private String tenantSchemaName;
	
	public AccessToken() {
	}

	public AccessToken(String tokenId, byte[] token, byte[] authentication, String username, String requesterIpAddress,
			String requesterDeviceType, byte[] tenant, String tenantSchemaName) {
		this.tokenId = tokenId;
		this.token = token;
		this.authentication = authentication;
		this.username = username;
		this.requesterIpAddress = requesterIpAddress;
		this.requesterDeviceType = requesterDeviceType;
		this.tenant = tenant;
		this.tenantSchemaName = tenantSchemaName;
	}

	@Id
	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	@Column(length=100000)
	public byte[] getToken() {
		return token;
	}

	public void setToken(byte[] token) {
		this.token = token;
	}

	@Column(length=100000)
	public byte[] getAuthentication() {
		return authentication;
	}

	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRequesterIpAddress() {
		return requesterIpAddress;
	}

	public void setRequesterIpAddress(String requesterIpAddress) {
		this.requesterIpAddress = requesterIpAddress;
	}

	public String getRequesterDeviceType() {
		return requesterDeviceType;
	}

	public void setRequesterDeviceType(String requesterDeviceType) {
		this.requesterDeviceType = requesterDeviceType;
	}

	@Column(length=100000)
	public byte[] getTenant() {
		return tenant;
	}

	public void setTenant(byte[] tenant) {
		this.tenant = tenant;
	}

	public String getTenantSchemaName() {
		return tenantSchemaName;
	}

	public void setTenantSchemaName(String tenantSchemaName) {
		this.tenantSchemaName = tenantSchemaName;
	}
}
