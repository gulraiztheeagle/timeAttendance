package com.hr.attendance.domain.tenant.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.attendance.domain.tenant.exception.dto.ExceptionJsonResponse;
import com.hr.attendance.domain.tenant.security.dto.AccessTokenInfo;
import com.hr.attendance.domain.tenant.security.service.TokenStore;


public class LoginProcessingFilter extends UsernamePasswordAuthenticationFilter {
	private final TokenStore tokenStore;
	private final ObjectMapper mapper;
	
	private final Logger logger = LoggerFactory.getLogger(LoginProcessingFilter.class);
	
	public LoginProcessingFilter(TokenStore tokenStore, ObjectMapper mapper) {
		this.tokenStore = tokenStore;
		this.mapper = mapper;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		AccessTokenInfo accessTokenInfo = tokenStore.getAccessToken(authResult);
		
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), accessTokenInfo);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ex) throws IOException, ServletException {
		
		logger.debug("Unsuccessfull Authentication, returning 401");
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        ExceptionJsonResponse resDto = new ExceptionJsonResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex.getMessage());
        
        mapper.writeValue(response.getWriter(), resDto);
	}
}