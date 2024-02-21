package com.hr.attendance.domain.tenant.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.hr.attendance.domain.tenant.security.BearerAuthenticationToken;
import com.hr.attendance.domain.tenant.security.RequestContext;
import com.hr.attendance.domain.tenant.security.util.TokenExtractor;


public class BearerTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
	private final String AUTHENTICATION_HEADER_NAME = "Authorization";
	private final TokenExtractor tokenExtractor;
	
	public BearerTokenAuthenticationProcessingFilter(RequestMatcher requestMatcher, 
			TokenExtractor tokenExtractor) {
		super(requestMatcher);
		this.tokenExtractor = tokenExtractor;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String tokenPayload = request.getHeader(AUTHENTICATION_HEADER_NAME);
        String token = tokenExtractor.extract(tokenPayload);
        
        return getAuthenticationManager().authenticate(new BearerAuthenticationToken(token));
	}
	
	@Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
		BearerAuthenticationToken bearerToken = (BearerAuthenticationToken)authResult;
		SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        
        
//        RequestContext.setCurrentTenantSchema(bearerToken.getTenantSchema());
        
        chain.doFilter(request, response);
    }
}
