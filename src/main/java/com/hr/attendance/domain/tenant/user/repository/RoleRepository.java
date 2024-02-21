package com.hr.attendance.domain.tenant.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.hr.attendance.domain.tenant.user.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>,QuerydslPredicateExecutor<Role>{
	Role findByAuthority(String authority);
}
