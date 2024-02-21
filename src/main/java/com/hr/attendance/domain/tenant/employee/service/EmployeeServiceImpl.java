package com.hr.attendance.domain.tenant.employee.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.attendance.domain.tenant.employee.model.Department;
import com.hr.attendance.domain.tenant.employee.model.Designation;
import com.hr.attendance.domain.tenant.employee.model.EmailLog;
import com.hr.attendance.domain.tenant.employee.model.Employee;
import com.hr.attendance.domain.tenant.employee.model.EmployeeAttendance;
import com.hr.attendance.domain.tenant.employee.model.EmployeeRequest;
import com.hr.attendance.domain.tenant.employee.model.QDepartment;
import com.hr.attendance.domain.tenant.employee.model.QDesignation;
import com.hr.attendance.domain.tenant.employee.model.QEmployee;
import com.hr.attendance.domain.tenant.employee.model.QEmployeeRequest;
import com.hr.attendance.domain.tenant.employee.repository.DepartmentRepository;
import com.hr.attendance.domain.tenant.employee.repository.DesignationRepository;
import com.hr.attendance.domain.tenant.employee.repository.EmailLogRepository;
import com.hr.attendance.domain.tenant.employee.repository.EmployeeAttendanceRepository;
import com.hr.attendance.domain.tenant.employee.repository.EmployeeRepository;
import com.hr.attendance.domain.tenant.employee.repository.EmployeeRequestRepository;
import com.hr.attendance.domain.tenant.enums.RequestApprovalStatusEnum;
import com.hr.attendance.domain.tenant.security.RequestContext;
import com.hr.attendance.domain.tenant.user.model.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmployeeAttendanceRepository employeeAttendanceRepository;
	@Autowired
	private EmployeeRequestRepository employeeRequestRepository;
	@Autowired
	private EmailLogRepository emailLogRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private DesignationRepository designationRepository;

	@Override
	public List<Employee> findAllEmployee() {
		return employeeRepository.findAll();
	}

	@Override
	public Optional<Employee> findEmployeeByEmail(String email) {
		return employeeRepository.findByEmail(email);
	}

	@Override
	public Optional<Employee> findEmployeeById(Long id) {
		return employeeRepository.findById(id);
	}

	@Override
	public void addEmployee(Employee employee) {
		employeeRepository.save(employee);
	}

	@Override
	public void updateEmployee(Employee employee) {
		employeeRepository.save(employee);
	}

	@Override
	public void deleteEmployeeById(Long id) {
		employeeRepository.deleteById(id);
	}

	@Override
	public boolean IsEmployeeNameUnique(Employee employee) {
		BooleanBuilder where = new BooleanBuilder();
		QEmployee qEmployee = QEmployee.employee;
		where.and(qEmployee.email.eq(employee.getEmail()));
		where.and(qEmployee.id.ne(employee.getId()));
		return !employeeRepository.exists(where);
	}

	public List<EmployeeAttendance> findAllEmployeeAttendance() {
		return employeeAttendanceRepository.findAll();
	}

//	public List<EmployeeAttendance> findEmployeeAttendanceByEmail(String email) {
//		return employeeAttendanceRepository.findByEmailOrderByCreatedDateDesc(email);		
//	}

	public void addEmployeeAttendance(EmployeeAttendance userAttendance) {
		employeeAttendanceRepository.save(userAttendance);
	}

	@Override
	public void deleteEmployeeAttendanceById(long id) {
		employeeAttendanceRepository.deleteById(id);

	}

	@Override
	public boolean isEmployeeAttendanceUnique(String email, Date date, int transactionType) {
		JPAQuery<String> query = new JPAQuery<>(em);
		List tupleList = em.createNativeQuery(
				"SELECT * FROM hr_time_attendance where date(app_time_stamp) = date(:appTime) and user_email = :email and type = :type")
				.setParameter("appTime", date, TemporalType.DATE).setParameter("type", transactionType)
				.setParameter("email", email).getResultList();
		if (tupleList.isEmpty()) {
			return true;
		}
		return false;

	}

	@Override
	public List<EmployeeRequest> findAllEmployeeRequest() {
		return employeeRequestRepository.findAll();
	}

	@Override
	public Optional<EmployeeRequest> findEmployeeRequestById(Long id) {
		return employeeRequestRepository.findById(id);
	}

	@Override
	public void addEmployeeRequest(EmployeeRequest employeeRequest) {
		employeeRequestRepository.save(employeeRequest);
	}

	@Override
	public void updateEmployeeRequest(EmployeeRequest employeeRequest) {
		employeeRequestRepository.save(employeeRequest);
	}

	@Override
	public void deleteEmployeeRequestById(long id) {
		employeeRequestRepository.deleteById(id);

	}

	@Override
	public boolean isEmployeeRequestDateRangeUnique(String email, Date startDate, Date endDate) {

		BooleanBuilder where = new BooleanBuilder();
		QEmployeeRequest qQEmployeeRequest = QEmployeeRequest.employeeRequest;

		where.and(qQEmployeeRequest.email.eq(email).and(qQEmployeeRequest.startDate.loe(endDate))
				.and(qQEmployeeRequest.endDate.goe(startDate))
				.and(qQEmployeeRequest.approvalStatus.eq(RequestApprovalStatusEnum.PENDING.getValue())));
		return !employeeRequestRepository.exists(where);
	}

	@Override
	public boolean isEmployeeRequestInitiatedOrApproved(String email) {

		QEmployeeRequest qEmployeeRequest = QEmployeeRequest.employeeRequest;
		BooleanBuilder where = new BooleanBuilder();
		where.and(qEmployeeRequest.email.eq(email)).and(qEmployeeRequest.submissionDate.eq(new Date()))
				.and(qEmployeeRequest.requestType.eq(2))
				.and(qEmployeeRequest.approvalStatus.eq(RequestApprovalStatusEnum.APPROVED.getValue()));
		return employeeRequestRepository.exists(where);
	}

	@Override
	public void createEmailLog(EmailLog emailLog) {
		emailLogRepository.save(emailLog);

	}

	@Override
	public void editEmailLog(EmailLog emailLog) {
		emailLogRepository.save(emailLog);
	}

	@Override
	public List<EmailLog> findAllEmailLogByStatus(boolean status) {

		return emailLogRepository.findByDelivered(status);

	}

	@Override
	public Optional<EmailLog> findEmailLogById(long id) {
		return emailLogRepository.findById(id);
	}

	@Override
	public List<Department> findAllDepartment() {
		return departmentRepository.findAll();
	}

	@Override
	public Optional<Department> findDepartmentById(long id) {
		return departmentRepository.findById(id);
	}

	@Override
	public void addDepartment(Department department) {
		departmentRepository.save(department);

	}

	@Override
	public void updateDepartment(Department department) {
		departmentRepository.save(department);

	}

	@Override
	public void deleteDepartmentById(long id) {
		departmentRepository.deleteById(id);

	}

	@Override
	public boolean isDepartmentNameUnique(Department department) {
		QDepartment qDepartment = QDepartment.department;
		BooleanBuilder where = new BooleanBuilder();
		where.and(qDepartment.name.eq(department.getName()));
		where.and(qDepartment.id.ne(department.getId()));
		return !departmentRepository.exists(where);
	}

	@Override
	public List<Designation> findAllDesignation() {
		return designationRepository.findAll();
	}

	@Override
	public Optional<Designation> findDesignationById(long id) {
		return designationRepository.findById(id);
	}

	@Override
	public void addDesignation(Designation designation) {
		designationRepository.save(designation);

	}

	@Override
	public void updateDesignation(Designation designation) {
		designationRepository.save(designation);

	}

	@Override
	public void deleteDesignationById(long id) {
		designationRepository.deleteById(id);

	}

	@Override
	public boolean isDesignationNameUnique(Designation designation) {
		BooleanBuilder where = new BooleanBuilder();
		QDesignation qDesignation = QDesignation.designation;
		where.and(qDesignation.name.eq(designation.getName()));
		where.and(qDesignation.id.ne(designation.getId()));
		return !designationRepository.exists(where);
	}

	@Override
	public List<Map<String, Object>> findEmployeeAttendanceByEmail(String email) {

		String rawSql = "select user_name userName,user_email userEmail,dated,ifnull(check_in,' ') checkIn,ifnull(check_out,' ') checkOut from hr_attendance_view where user_email=:email order by dated desc;";

		@SuppressWarnings("unchecked")
		List<javax.persistence.Tuple> rows = em.createNativeQuery(rawSql, javax.persistence.Tuple.class)
				.setParameter("email", email).getResultList();

		List<Map<String, Object>> dtos = new ArrayList<>();

		rows.forEach(row -> {
			Map<String, Object> dto = new HashMap<>();

			dto.put("userName", row.get(0).toString());
			dto.put("userEmail", row.get(1).toString());
			dto.put("dated", row.get(2).toString());
			dto.put("checkIn", row.get(3).toString());
			dto.put("checkOut", row.get(4).toString());

			dtos.add(dto);
		});

		return dtos;
	}

	@Override
	public List<EmployeeRequest> findEmployeeRequestByEmail(String email) {
		// TODO Auto-generated method stub
		return employeeRequestRepository.findByEmailOrderByIdDesc(email);
	}

	@Override
	public List<Map<String, Object>> findEmployeeRequestToApprove(Long managerId) {

		String rawSql = "select ur.id,concat(e.first_name,e.last_name),ur.email,'Pending' approvalStatus,ur.start_date,ur.end_date,case when ur.request_type=1 then 'Leave Request' else 'Remote Request' end requestType,ur.submission_date,IFNULL(ur.remarks,'') remarks,ur.reason\r\n"
				+ " from employee e inner join user_request ur on e.id=ur.employee_id where e.manager_id=:managerId "
				+  " and ur.approval_status=1 order by submission_date desc;";

		@SuppressWarnings("unchecked")
		List<javax.persistence.Tuple> rows = em.createNativeQuery(rawSql, javax.persistence.Tuple.class)
		.setParameter("managerId", managerId)		
		.getResultList();

		List<Map<String, Object>> dtos = new ArrayList<>();
		rows.forEach(row -> {

			Map<String, Object> dto = new HashMap<>();
			dto.put("id", row.get(0).toString());
			dto.put("name", row.get(1).toString());
			dto.put("email", row.get(2).toString());
			dto.put("approvalStatus", row.get(3).toString());
			dto.put("startDate", row.get(4).toString());
			dto.put("endDate", row.get(5).toString());
			dto.put("requestType", row.get(6).toString());
			dto.put("submissionDate", row.get(7).toString());
			dto.put("remarks", row.get(8).toString());
			dto.put("reason", row.get(9).toString());

			dtos.add(dto);
		});
		return dtos;
	}

	@Override
	public void disableEmployee(long employeeId) {
		Optional<Employee> emp = employeeRepository.findById(employeeId);
		emp.get().setEnabled(false);
		employeeRepository.save(emp.get());

	}

}
