package com.hr.attendance.domain.tenant.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.hr.attendance.domain.tenant.security.BearerAuthenticationToken;
import com.hr.attendance.domain.tenant.security.exception.InvalidTokenException;
import com.hr.attendance.domain.tenant.security.model.AccessToken;
import com.hr.attendance.domain.tenant.security.service.TokenStore;



@Component
public class BearerTokenAuthenticationProvider implements AuthenticationProvider{
	@Autowired
	private TokenStore tokenStore;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String accessTokenKey = (String) authentication.getCredentials();
		AccessToken accessToken = tokenStore.readAccessToken(accessTokenKey);
		
		if(accessToken == null)
			throw new InvalidTokenException("Invalid Bearer Token");
		
		Authentication auth = tokenStore.readAuthentication(accessToken);
		String tenantSchema = accessToken.getTenantSchemaName();
		
		if(auth == null)
			throw new InvalidTokenException("Persisted Access Token is invalid or has been corrupted.");
		
		String username = (String) auth.getPrincipal();
		
		return new BearerAuthenticationToken(username, auth.getAuthorities(), tenantSchema);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (BearerAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
