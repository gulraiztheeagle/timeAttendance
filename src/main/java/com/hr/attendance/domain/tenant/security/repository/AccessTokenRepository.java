package com.hr.attendance.domain.tenant.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.hr.attendance.domain.tenant.security.model.AccessToken;


public interface AccessTokenRepository extends JpaRepository<AccessToken, String>, QuerydslPredicateExecutor<AccessToken> {

}
