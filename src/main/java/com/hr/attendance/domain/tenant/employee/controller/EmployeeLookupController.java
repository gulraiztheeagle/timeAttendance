package com.hr.attendance.domain.tenant.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hr.attendance.domain.tenant.employee.model.Department;
import com.hr.attendance.domain.tenant.employee.model.Designation;
import com.hr.attendance.domain.tenant.employee.service.EmployeeService;
import com.hr.attendance.domain.tenant.util.ApiMessageLookup;

@RestController
@RequestMapping(value = "/employee/lookup")
@CrossOrigin(origins = "*")
public class EmployeeLookupController {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ApiMessageLookup messageLookup;

	@RequestMapping(value = "/getAllDepartment", method = RequestMethod.GET)
	public List<Department> findAllDepartment() {
		return employeeService.findAllDepartment();

	}

	@RequestMapping(value = "/getAllDesignation", method = RequestMethod.GET)
	public List<Designation> findAllDesignation() {
		return employeeService.findAllDesignation();
	}

}
