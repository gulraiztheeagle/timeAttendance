package com.hr.attendance.domain.tenant.user.service;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.attendance.domain.tenant.user.dto.CreateUserDTO;
import com.hr.attendance.domain.tenant.user.dto.PrivilegeDTO;
import com.hr.attendance.domain.tenant.user.dto.RoleDTO;
import com.hr.attendance.domain.tenant.user.dto.UserDetailsDTO;
import com.hr.attendance.domain.tenant.user.dto.UserRoleDTO;
import com.hr.attendance.domain.tenant.user.model.Privilege;
import com.hr.attendance.domain.tenant.user.model.Role;
import com.hr.attendance.domain.tenant.user.model.User;

@Service
public class UserMappingHelper {
	@Autowired
	private DozerBeanMapper mapper;
	
	public void setMapper(DozerBeanMapper mapper){
		this.mapper = mapper;
	}
	
	public User convertUserDetailsDtoToDomainModel(UserDetailsDTO userDTO){
		return mapper.map(userDTO, User.class);
	}
	
	public User convertCreateUserDtoToDomainModel(CreateUserDTO userDTO){
		return mapper.map(userDTO, User.class);
	}
	
	public UserDetailsDTO convertUserToUserDetailsDto(User user){
		return mapper.map(user, UserDetailsDTO.class);
	}
	
	public RoleDTO convertRoleToDto(Role auth){
		return mapper.map(auth, RoleDTO.class);
	}
	
	public Role convertRoleDtoToDomainModel(RoleDTO authDto){
		return mapper.map(authDto, Role.class);
	}
	
	public PrivilegeDTO convertPrivilegeToDto(Privilege privilege){
		return mapper.map(privilege, PrivilegeDTO.class);
	}
	
	public UserRoleDTO convertRoleToUserRoleDto(Role role){
		return mapper.map(role, UserRoleDTO.class);
	}
}
