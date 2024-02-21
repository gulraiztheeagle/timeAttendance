package com.hr.attendance.domain.tenant.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.attendance.domain.tenant.security.filter.BearerTokenAuthenticationProcessingFilter;
import com.hr.attendance.domain.tenant.security.filter.LoginProcessingFilter;
import com.hr.attendance.domain.tenant.security.provider.BearerTokenAuthenticationProvider;
import com.hr.attendance.domain.tenant.security.provider.LoginAuthenticationProvider;
import com.hr.attendance.domain.tenant.security.service.TokenStore;
import com.hr.attendance.domain.tenant.security.util.TokenExtractor;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	public static final String AUTHENTICATION_URL = "/login";
	public static final String REQUEST_PASS_URL = "/user/**";
	public static final String API_ROOT_URL = "/**";

	@Autowired
	private TokenStore tokenStore;
	@Autowired
	private TokenExtractor tokenExtractor;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private LoginAuthenticationProvider loginAuthProvider;
	@Autowired
	private BearerTokenAuthenticationProvider bearerAuthProvider;

	private LoginProcessingFilter loginFilter() {
		LoginProcessingFilter filter = new LoginProcessingFilter(tokenStore, objectMapper);
		filter.setAuthenticationManager(authManager);

		return filter;
	};

	private BearerTokenAuthenticationProcessingFilter bearerFilter() {
		BearerTokenAuthenticationProcessingFilter filter = new BearerTokenAuthenticationProcessingFilter(
				new AntPathRequestMatcher(API_ROOT_URL), tokenExtractor);

		filter.setAuthenticationManager(authManager);

		return filter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests().antMatchers(AUTHENTICATION_URL,REQUEST_PASS_URL).permitAll()
		.antMatchers("/notifications/**").permitAll().and().authorizeRequests().anyRequest().authenticated()
				.and().addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(bearerFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
		configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


	@Override
	public void configure(WebSecurity registry) {
		registry.ignoring()
				.antMatchers("/docs/**").antMatchers("/actuator/**").antMatchers("/v2/api-docs", "/configuration/ui",
						"/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**")
				.antMatchers("/notifications/**");
	}

	@Autowired
	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(loginAuthProvider);
		auth.authenticationProvider(bearerAuthProvider);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}