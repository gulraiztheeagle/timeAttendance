package com.hr.attendance.domain.tenant.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.attendance.domain.tenant.user.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {

}
