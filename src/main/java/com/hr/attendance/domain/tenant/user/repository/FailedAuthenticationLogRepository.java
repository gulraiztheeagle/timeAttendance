package com.hr.attendance.domain.tenant.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.hr.attendance.domain.tenant.user.model.FailedAuthenticationLog;

public interface FailedAuthenticationLogRepository 
			extends JpaRepository<FailedAuthenticationLog, Long>,QuerydslPredicateExecutor<FailedAuthenticationLog>{

}
