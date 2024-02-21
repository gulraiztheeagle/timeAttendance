package com.hr.attendance.domain.tenant.employee.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.hr.attendance.domain.tenant.employee.model.EmployeeAttendance;


public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance, Long>,QuerydslPredicateExecutor<EmployeeAttendance>{

	List<EmployeeAttendance> findByUserEmailOrderByCreatedDateDesc(String email);
}
