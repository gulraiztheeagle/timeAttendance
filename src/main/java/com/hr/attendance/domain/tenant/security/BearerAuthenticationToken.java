package com.hr.attendance.domain.tenant.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class BearerAuthenticationToken extends AbstractAuthenticationToken{
	private static final long serialVersionUID = 9213416587082988980L;
	
	private String username;
	private String accessToken;
	private String tenantSchema;
	
	public BearerAuthenticationToken(String accessToken){
		super(null);
		this.accessToken = accessToken;
	}
	
	public BearerAuthenticationToken(String username, Collection<? extends GrantedAuthority> authorities, String tenantSchema) {
		super(authorities);
		this.username = username;
		this.tenantSchema = tenantSchema;
		this.eraseCredentials();
		
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return this.accessToken;
	}

	@Override
	public Object getPrincipal() {
		return this.username;
	}
	
	@Override
    public void eraseCredentials() {        
        super.eraseCredentials();
        this.accessToken = null;
    }
	
	@Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

	public String getTenantSchema() {
		return tenantSchema;
	}
}
