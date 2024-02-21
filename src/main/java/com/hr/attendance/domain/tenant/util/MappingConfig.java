package com.hr.attendance.domain.tenant.util;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hr.attendance.domain.tenant.user.dto.CreateUserDTO;
import com.hr.attendance.domain.tenant.user.dto.PermissionDTO;
import com.hr.attendance.domain.tenant.user.dto.PrivilegeDTO;
import com.hr.attendance.domain.tenant.user.dto.RoleDTO;
import com.hr.attendance.domain.tenant.user.dto.UserDetailsDTO;
import com.hr.attendance.domain.tenant.user.dto.UserRoleDTO;
import com.hr.attendance.domain.tenant.user.model.Permission;
import com.hr.attendance.domain.tenant.user.model.Privilege;
import com.hr.attendance.domain.tenant.user.model.Role;
import com.hr.attendance.domain.tenant.user.model.User;



@Configuration
public class MappingConfig {
	@Bean
	public BeanMappingBuilder beanMappingBuilder() {
	    return new BeanMappingBuilder() {
	        @Override
	        protected void configure() {
	            mapping(User.class, UserDetailsDTO.class)
	            .fields(field("id").accessible(), field("id").accessible())
	            .fields("authorities", "roles");
	            
	            mapping(Role.class, UserRoleDTO.class)
	            .fields(field("id").accessible(), field("id").accessible())
	            .fields("authority", "name");
	            
	            mapping(Role.class, RoleDTO.class)
	            .fields(field("id").accessible(), field("id").accessible())
	            .fields("authority", "name")
	            .fields(field("enabled"), field("enabled").getMethod("isEnabled()"));
	            
	            mapping(Permission.class, PermissionDTO.class)
	            .fields(field("id").accessible(), field("id").accessible());
	            
	            mapping(Privilege.class, PrivilegeDTO.class)
	            .fields(field("id").accessible(), field("id").accessible());
	            
	            mapping(User.class, CreateUserDTO.class)
	            .fields("authorities", "roles");
	            
//	            mapping(Bank.class, CreateBankDTO.class)
//	            .fields(field("enabled"), field("enabled").getMethod("isEnabled()"));
	        }
	    };
	}
	
	@Bean
	public DozerBeanMapper beanMapper() {
	    DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
	    dozerBeanMapper.addMapping(beanMappingBuilder());
	    return dozerBeanMapper;
	}
}
