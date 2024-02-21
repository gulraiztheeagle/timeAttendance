package com.hr.attendance.domain.tenant.employee.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.hr.attendance.domain.tenant.employee.model.Department;
import com.hr.attendance.domain.tenant.employee.model.Designation;
import com.hr.attendance.domain.tenant.employee.model.EmailLog;
import com.hr.attendance.domain.tenant.employee.model.Employee;
import com.hr.attendance.domain.tenant.employee.model.EmployeeAttendance;
import com.hr.attendance.domain.tenant.employee.model.EmployeeRequest;


public interface EmployeeService {

	// Employee
    public List<Employee> findAllEmployee();
	public Optional<Employee>  findEmployeeByEmail(String email);
	public Optional<Employee> findEmployeeById(Long id);
    public void addEmployee(Employee employee);
    public void updateEmployee(Employee employee);
    public void deleteEmployeeById(Long id);
    public boolean IsEmployeeNameUnique(Employee employee);
    public void disableEmployee(long employeeId);
	
	// Employee attendance
	public List<Map<String, Object>> findEmployeeAttendanceByEmail(String email);
	public void addEmployeeAttendance(EmployeeAttendance employeeAttendance);
	public void deleteEmployeeAttendanceById(long id);
	public boolean isEmployeeAttendanceUnique(String email,Date date,int transactionType);
	
	// Employee request
	public List<EmployeeRequest> findAllEmployeeRequest();
	public List<EmployeeRequest> findEmployeeRequestByEmail(String email);
	public List<Map<String,Object>> findEmployeeRequestToApprove(Long managerId);
	public Optional<EmployeeRequest> findEmployeeRequestById(Long id);
	public void addEmployeeRequest(EmployeeRequest employeeRequest);
	public void updateEmployeeRequest(EmployeeRequest employeeRequest);
	public void deleteEmployeeRequestById(long id);
	public boolean isEmployeeRequestDateRangeUnique(String email,Date startDate,Date endDate);  
	public boolean isEmployeeRequestInitiatedOrApproved(String email);
	
	
	
	//Failed Email logs
	public List<EmailLog> findAllEmailLogByStatus(boolean status);
	public Optional<EmailLog> findEmailLogById(long id);
	public void createEmailLog(EmailLog emailLog);
	public void editEmailLog(EmailLog emailLog);

	//Department
	public List<Department> findAllDepartment();
	public Optional<Department> findDepartmentById(long id);
	public void addDepartment(Department department);
	public void updateDepartment(Department department);
	public void deleteDepartmentById(long id);
	public boolean isDepartmentNameUnique(Department department);
	
	//Designation
	public List<Designation> findAllDesignation();
	public Optional<Designation> findDesignationById(long id);
	public void addDesignation(Designation designation);
	public void updateDesignation(Designation designation);
	public void deleteDesignationById(long id);
	public boolean isDesignationNameUnique(Designation designation);
	
	
	
}
