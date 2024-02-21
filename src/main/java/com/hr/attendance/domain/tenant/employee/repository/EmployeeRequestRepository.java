package com.hr.attendance.domain.tenant.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.hr.attendance.domain.tenant.employee.model.EmployeeRequest;



public interface EmployeeRequestRepository extends JpaRepository<EmployeeRequest, Long>,QuerydslPredicateExecutor<EmployeeRequest>{

	List<EmployeeRequest> findByEmailOrderByIdDesc(String email);
	Optional<EmployeeRequest> findById(Long id);

}
