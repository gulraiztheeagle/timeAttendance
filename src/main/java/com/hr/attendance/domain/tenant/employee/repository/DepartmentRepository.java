package com.hr.attendance.domain.tenant.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.hr.attendance.domain.tenant.employee.model.Department;


public interface DepartmentRepository extends JpaRepository<Department, Long>,QuerydslPredicateExecutor<Department> {

}
