package com.hr.attendance.domain.tenant.security.service;

import org.springframework.security.core.Authentication;

import com.hr.attendance.domain.tenant.security.dto.AccessTokenInfo;
import com.hr.attendance.domain.tenant.security.model.AccessToken;


public interface TokenStore {
	public final String USERNAME = "username";
	public final String UUID_KEY = "uuid";
	AccessTokenInfo getAccessToken(Authentication authentication);
	AccessToken readAccessToken(String accessTokenKey);
	Authentication readAuthentication(AccessToken accessToken);
//	Tenant readTenant(AccessToken accessToken);
}
