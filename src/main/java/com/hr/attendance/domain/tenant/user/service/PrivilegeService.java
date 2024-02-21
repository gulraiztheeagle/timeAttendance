package com.hr.attendance.domain.tenant.user.service;

import java.util.List;

import com.hr.attendance.domain.tenant.user.model.Privilege;

public interface PrivilegeService {
	Privilege findPrivilegeByName(String name);
	Privilege findPrivilegeByUrl(String url);
	List<Privilege> findAll();
}
