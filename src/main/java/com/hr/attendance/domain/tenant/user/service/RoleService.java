package com.hr.attendance.domain.tenant.user.service;

import java.util.List;
import java.util.Optional;

import com.hr.attendance.domain.tenant.user.model.Permission;
import com.hr.attendance.domain.tenant.user.model.Role;

public interface RoleService {
	List<Role> findAllRoles();
	boolean updateRole(Role auth);
	boolean isRoleDuplicate(String roleName);
	Optional<Role> findRoleById(Long id);
	void createRole(Role role);
	boolean isPermissionValid(String permissionId);
	Optional<Permission> findPermissionById(String permissionId);
	List<Permission> findAllPermissions();
	Optional<Role> getRoleProxy(long id);
}
