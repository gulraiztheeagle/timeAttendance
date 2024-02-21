package com.hr.attendance.domain.tenant.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.attendance.domain.tenant.user.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	PasswordResetToken findByToken(String token);
}
