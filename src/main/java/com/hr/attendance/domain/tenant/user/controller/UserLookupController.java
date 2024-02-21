package com.hr.attendance.domain.tenant.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hr.attendance.domain.tenant.user.model.Permission;
import com.hr.attendance.domain.tenant.user.model.Role;
import com.hr.attendance.domain.tenant.user.service.RoleService;

@RestController
@RequestMapping(value="/user/lookup")
@CrossOrigin(origins = "*")
public class UserLookupController {
	@Autowired
	private RoleService roleService;
	
//	@PreAuthorize("hasAnyAuthority('CREATE_A_USER', 'UPDATE_A_USER')")
	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public ResponseEntity<?> getRoles(){
		List<Role> roles = roleService.findAllRoles();
		List<Map<String, Object>> lookupRoles = new ArrayList<>();
		
		for(Role role : roles){
			Map<String, Object> roleMap = new HashMap<>();
			roleMap.put("id", role.getId());
			roleMap.put("name", role.getAuthority());
			
			lookupRoles.add(roleMap);
		}
		
		return new ResponseEntity<List<Map<String, Object>>>(lookupRoles, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAnyAuthority('CREATE_A_ROLE', 'UPDATE_A_ROLE')")
	@RequestMapping(value = "/permissions", method = RequestMethod.GET)
	public ResponseEntity<?> getPermissions(){
		List<Permission> permissions = roleService.findAllPermissions();
		List<Map<String, Object>> lookupPermissions = new ArrayList<>();
		
		for(Permission permission: permissions){
			Map<String, Object> permissionMap = new HashMap<>();
			permissionMap.put("name", permission.getAuthority());
			permissionMap.put("description", permission.getDescription());
			
			lookupPermissions.add(permissionMap);
		}
		
		return new ResponseEntity<List<Map<String, Object>>>(lookupPermissions, HttpStatus.OK);
	}
}
