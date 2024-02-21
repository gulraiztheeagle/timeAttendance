package com.hr.attendance.domain.tenant.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.hr.attendance.domain.tenant.employee.model.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long>,QuerydslPredicateExecutor<Employee> {

	Optional<Employee> findByEmail(String email);
	List<Employee> findByManager(long managerId);
	
}
