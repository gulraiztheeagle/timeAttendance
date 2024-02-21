package com.hr.attendance.domain.tenant.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.hr.attendance.domain.tenant.employee.model.EmailLog;


public interface EmailLogRepository extends JpaRepository<EmailLog, Long>, QuerydslPredicateExecutor<EmailLog> {

	List<EmailLog> findByDelivered(boolean status);
}
