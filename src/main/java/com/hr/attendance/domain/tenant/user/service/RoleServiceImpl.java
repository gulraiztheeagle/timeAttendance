package com.hr.attendance.domain.tenant.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.attendance.domain.tenant.user.model.Permission;
import com.hr.attendance.domain.tenant.user.model.QRole;
import com.hr.attendance.domain.tenant.user.model.Role;
import com.hr.attendance.domain.tenant.user.repository.PermissionRepository;
import com.hr.attendance.domain.tenant.user.repository.RoleRepository;
import com.querydsl.core.types.Predicate;

@Service
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	
	@Override
	public List<Role> findAllRoles(){
		return roleRepository.findAll();
	}

	@Override
	public boolean updateRole(Role auth) {
		roleRepository.save(auth);
		return true;
	}

	@Override
	public boolean isRoleDuplicate(String roleName) {
		QRole role = QRole.role;
		Predicate where = role.authority.equalsIgnoreCase(roleName);
		
		return roleRepository.exists(where);
	}

	@Override
	public Optional<Role> findRoleById(Long id) {
		return roleRepository.findById(id);
	}

	@Override
	public void createRole(Role role) {
		roleRepository.save(role);
	}

	@Override
	public boolean isPermissionValid(String permissionId) {
		return permissionRepository.existsById(permissionId);
	}

	@Override
	public Optional<Permission> findPermissionById(String permissionId) {
		return permissionRepository.findById(permissionId);
	}

	@Override
	public List<Permission> findAllPermissions() {
		return permissionRepository.findAll();
	}

	@Override
	public Optional<Role> getRoleProxy(long id) {
		return roleRepository.findById(id);
	}
}
