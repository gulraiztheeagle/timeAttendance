package com.hr.attendance.domain.tenant.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.hr.attendance.domain.tenant.user.model.User;

public interface UserRepository extends JpaRepository<User, Long>,QuerydslPredicateExecutor<User> {
	Optional<User> findByUsername(String username);
}