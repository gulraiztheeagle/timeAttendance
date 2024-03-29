package com.hr.attendance.domain.tenant.audit;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
//(auditorAwareRef = "auditorAware")
class AuditConfig {
	@Bean
	public AuditorAware<String> createAuditorProvider() {
		return new SecurityAuditor();
	}

	@Bean
	public AuditingEntityListener createAuditingListener() {
		return new AuditingEntityListener();
	}

	/*
	 * public static class SecurityAuditor implements AuditorAware<String> {
	 * 
	 * @Override public Optional<String> getCurrentAuditor() { return
	 * Optional.of("BACKGROUND"); } }
	 */
    public static class SecurityAuditor implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth == null){
            	Optional.of("BACKGROUND"); 
            }
            
            return Optional.of(auth.getName()); 
        }
    }
	
}