package com.hr.attendance.domain.tenant.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.attendance.domain.tenant.user.model.Privilege;
import com.hr.attendance.domain.tenant.user.repository.PrivilegeRepository;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {
	
	@Autowired
	private PrivilegeRepository privilegeRepo;
	
	@Override
	public Privilege findPrivilegeByName(String name) {
		return privilegeRepo.findByName(name);
	}

	@Override
	public Privilege findPrivilegeByUrl(String url) {
		return privilegeRepo.findByUrl(url);
	}

	@Override
	public List<Privilege> findAll() {
		return privilegeRepo.findAll();
	}
	
}
