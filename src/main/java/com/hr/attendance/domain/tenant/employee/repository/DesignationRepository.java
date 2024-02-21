package com.hr.attendance.domain.tenant.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.hr.attendance.domain.tenant.employee.model.Designation;


public interface DesignationRepository extends JpaRepository<Designation, Long>,QuerydslPredicateExecutor<Designation> {

}
