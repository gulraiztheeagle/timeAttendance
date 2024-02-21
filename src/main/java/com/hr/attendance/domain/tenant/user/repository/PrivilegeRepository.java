package com.hr.attendance.domain.tenant.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.hr.attendance.domain.tenant.user.model.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long>,QuerydslPredicateExecutor<Privilege> {
	Privilege findByName(String name);
	Privilege findByUrl(String url);
	List<Privilege> findOneByName(String name);
}
