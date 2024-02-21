package com.hr.attendance.domain.tenant.security.provider;

import java.time.LocalDate;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.hr.attendance.domain.tenant.user.model.User;
import com.hr.attendance.domain.tenant.user.service.CalendarService;
import com.hr.attendance.domain.tenant.user.service.UserDetailsServiceImpl;

@Component
public class LoginAuthenticationProvider implements AuthenticationProvider  {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private CalendarService calendarService;
	
	@Value("${backendApp.user.maxAllowedFailedAttempts}")
	private int maxAllowedAttempts;
	@Value("${backendApp.user.passwordExpiresInDays}")
	private int passwordExpiresInDays;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
//	@Autowired
//	private TenantContextUtil tenantContextUtil;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		//=========== Set the current Tenant Context for Authentication of user=======//
//				tenantContextUtil.setCurrentTenant(authentication.getName());
				
				//========== Start Authentication ===========================================//
				BCryptPasswordEncoder passwordEnocder = new BCryptPasswordEncoder();
				String username = authentication.getName();
				String password = (String) authentication.getCredentials();
				
				if (!userDetailsService.userExists(username))
				{
					log.info("username not found");
					throw new BadCredentialsException("Username not found!");
					
				}
				
				User user = userDetailsService.findByUsername(username);
				
				if (!user.getUsername().equalsIgnoreCase(username)) {
					log.info("username not found");
					throw new BadCredentialsException("Invalid username");
				}

				else if (StringUtils.isBlank(password)) {
					throw new BadCredentialsException("Provide passsword.");
				}
				
				else if (!passwordEnocder.matches(password, user.getPassword())) {
					log.info("Wrong password provided for username: "+username);
			/*
			 * int failedAttempts =
			 * userDetailsService.updateFailedLoginAttempts(user.getId());
			 * 
			 * if (failedAttempts % maxAllowedAttempts == 0) {
			 * userDetailsService.lockUserAccount(user.getId()); }
			 */
					log.info("EXCEPTION THROWN");
					throw new BadCredentialsException("Wrong password provided for username: "+username);
				}

				else if (!user.isAccountNonExpired()) {
					log.info("Account has been expired for username: "+username);
					throw new AccountExpiredException("Account has been expired.");
				}

				else if (!user.isEnabled()) {
					log.info("Account has been disabled for username: "+username);
					throw new DisabledException("User has been disabled.");
				}

				else if (!user.isAccountNonLocked()) {
					log.info("Account has been locked for username: "+username);
					throw new LockedException("Account has been locked.");
				}

				else if (!user.isCredentialsNonExpired()) {
					log.info("Account has been locked for username: "+username);
					throw new CredentialsExpiredException("Password has Expired.");
				}
		/*
		 * else{ LocalDate fromDate; LocalDate toDate = LocalDate.now();
		 * 
		 * if (user.getPasswordChangedDate() == null) { fromDate =
		 * calendarService.convertFromDateToLocalDate(user.getCreatedDate()); } else {
		 * fromDate =
		 * calendarService.convertFromDateToLocalDate(user.getPasswordChangedDate()); }
		 * 
		 * long diff = java.time.temporal.ChronoUnit.DAYS.between(fromDate, toDate); if
		 * (diff > passwordExpiresInDays) {
		 * userDetailsService.expireCredentials(user.getId()); throw new
		 * CredentialsExpiredException("Password has Expired."); } }
		 */				
//				userDetailsService.resetFailedLoginAttempts(user.getId());
				log.info("User with username: "+username+" has been authenticated");
				
				return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getPermissions());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
