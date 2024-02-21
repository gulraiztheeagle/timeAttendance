package com.hr.attendance.domain.tenant.employee.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hr.attendance.domain.tenant.config.FreemakerTemplete;
import com.hr.attendance.domain.tenant.employee.dto.CreateDepartmentDTO;
import com.hr.attendance.domain.tenant.employee.dto.CreateDesignationDTO;
import com.hr.attendance.domain.tenant.employee.dto.CreateEmployeeAttendanceDTO;
import com.hr.attendance.domain.tenant.employee.dto.CreateEmployeeDTO;
import com.hr.attendance.domain.tenant.employee.dto.CreateEmployeeRequestDTO;
import com.hr.attendance.domain.tenant.employee.dto.ListEmployeeDTO;
import com.hr.attendance.domain.tenant.employee.dto.ListEmployeeRequestDTO;
import com.hr.attendance.domain.tenant.employee.dto.UpdateDepartmentDTO;
import com.hr.attendance.domain.tenant.employee.dto.UpdateDesignationDTO;
import com.hr.attendance.domain.tenant.employee.dto.UpdateEmployeeDTO;
import com.hr.attendance.domain.tenant.employee.dto.UpdateEmployeeRequestDTO;
import com.hr.attendance.domain.tenant.employee.dto.ViewDepartmentDTO;
import com.hr.attendance.domain.tenant.employee.dto.ViewDesignationDTO;
import com.hr.attendance.domain.tenant.employee.dto.ViewEmployeeDTO;
import com.hr.attendance.domain.tenant.employee.dto.ViewEmployeeRequestDTO;
import com.hr.attendance.domain.tenant.employee.model.Department;
import com.hr.attendance.domain.tenant.employee.model.Designation;
import com.hr.attendance.domain.tenant.employee.model.EmailLog;
import com.hr.attendance.domain.tenant.employee.model.Employee;
import com.hr.attendance.domain.tenant.employee.model.EmployeeAttendance;
import com.hr.attendance.domain.tenant.employee.model.EmployeeRequest;
import com.hr.attendance.domain.tenant.employee.service.EmployeeService;
import com.hr.attendance.domain.tenant.enums.RequestApprovalStatusEnum;
import com.hr.attendance.domain.tenant.security.RequestContext;
import com.hr.attendance.domain.tenant.util.ApiJsonResponse;
import com.hr.attendance.domain.tenant.util.ApiMessageLookup;
import com.hr.attendance.domain.tenant.util.EmailNotifier;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@RestController
@RequestMapping("/employee")
@CrossOrigin(origins = "*")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ApiMessageLookup messageLookup;
	@Autowired
	private EmailNotifier notifier;
	@Autowired
	private FreemakerTemplete freemakerTemplete;
	@Autowired
	private Environment env;

	@GetMapping(value = "/attendance")
	public ResponseEntity<?> getEmployeeAttendanceByEmail(@RequestParam String email) {
		List<Map<String, Object>> list = employeeService.findEmployeeAttendanceByEmail(email);

		return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);
	}

	@PostMapping("/attendance/add")
	public ResponseEntity<?> addEmployeeAttendance(@RequestBody EmployeeAttendance employeeAttendance) {
		String type = (employeeAttendance.getType() == 1) ? "Checked-In" : "Checked-Out";

		if (!employeeService.isEmployeeRequestInitiatedOrApproved(employeeAttendance.getUserEmail())
				&& "Home".equalsIgnoreCase(employeeAttendance.getRemoteType())) {
			ApiJsonResponse response = new ApiJsonResponse(false,
					messageLookup.getMessage("user.request.approval.notFound"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}

		if (!employeeService.isEmployeeAttendanceUnique(employeeAttendance.getUserEmail(),
				employeeAttendance.getAppTimeStamp(), employeeAttendance.getType())) {
			ApiJsonResponse response = new ApiJsonResponse(false,
					messageLookup.getMessage("You Already " + type + " for today."));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.CONFLICT);
		}

		if (employeeAttendance.getType() == 2 && employeeService.isEmployeeAttendanceUnique(
				employeeAttendance.getUserEmail(), employeeAttendance.getAppTimeStamp(), 1)) {
			ApiJsonResponse response = new ApiJsonResponse(false,
					messageLookup.getMessage("You need to check-in first before doing check-out"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.CONFLICT);

		}

		employeeService.addEmployeeAttendance(employeeAttendance);
		CreateEmployeeAttendanceDTO dto = new CreateEmployeeAttendanceDTO(type);

		return new ResponseEntity<CreateEmployeeAttendanceDTO>(dto, HttpStatus.OK);
	}

	@GetMapping("/request")
	public ResponseEntity<?> findEmployeeRequestByEmail(@RequestParam(value = "email") String email) {

		List<ListEmployeeRequestDTO> dtoList = new ArrayList<>();

		List<EmployeeRequest> request = employeeService.findEmployeeRequestByEmail(email);

		request.forEach(s -> {
			ListEmployeeRequestDTO dto = new ListEmployeeRequestDTO();
			dto.setId(s.getId());
			dto.setName(s.getName());
			dto.setEmail(s.getEmail());
			dto.setSubmissionDate(s.getSubmissionDate());
			dto.setRequestType(s.getRequestType());
			dto.setReason(s.getReason());
			dto.setStartDate(s.getStartDate());
			dto.setEndDate(s.getEndDate());
			dto.setRemarks(s.getRemarks());
			dto.setApprovalStatus(s.getApprovalStatus());
			dto.setApprovalRemarks(s.getApprovalRemarks());
			dtoList.add(dto);

		});

		return new ResponseEntity<List<ListEmployeeRequestDTO>>(dtoList, HttpStatus.OK);

	}

	@GetMapping("/request/approvals/{managerId}")
	public ResponseEntity<?> findEmployeeRequestToApprove(@PathVariable long managerId) {
		List<Map<String, Object>> dtos = employeeService.findEmployeeRequestToApprove(managerId);

		return new ResponseEntity<List<Map<String, Object>>>(dtos, HttpStatus.OK);
	}

	@PostMapping("/request/add")
	public ResponseEntity<ApiJsonResponse> addEmployeeRequest(@Valid @RequestBody CreateEmployeeRequestDTO requestDTO)
			throws Exception {

		String type = (requestDTO.getRequestType() == 1) ? "Leave " : "Remote Work ";

		if (!employeeService.isEmployeeRequestDateRangeUnique(requestDTO.getEmail(), requestDTO.getStartDate(),
				requestDTO.getEndDate())) {
			ApiJsonResponse response = new ApiJsonResponse(false,
					messageLookup.getMessage("user.createRequest.conflict"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.CONFLICT);

		}

		EmployeeRequest request = new EmployeeRequest();
		Optional<Employee> employee  =employeeService.findEmployeeByEmail(requestDTO.getEmail()); 
		if (employee.isPresent())
		request.setEmployee(employee.get());

		
		request.setName(employee.get().getFullName());
		request.setEmail(requestDTO.getEmail());
		request.setRequestType(requestDTO.getRequestType());
		request.setStartDate(requestDTO.getStartDate());
		request.setEndDate(requestDTO.getEndDate());
		request.setReason(requestDTO.getReason());
		request.setSubmissionDate(new Date()); 
		request.setRemarks(requestDTO.getRemarks());
		request.setApprovalStatus(RequestApprovalStatusEnum.PENDING.getValue());
		

		employeeService.addEmployeeRequest(request);

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", employee.get().getFullName());
		model.put("email", requestDTO.getEmail());
		model.put("requestType", type);
		model.put("startDate", requestDTO.getStartDate());
		model.put("endDate", requestDTO.getEndDate());
		model.put("reason", requestDTO.getReason());
		model.put("remarks", requestDTO.getRemarks());
		model.put("submissionDate", new Date());
		model.put("subject", type);
		model.put("managerName",employee.get().getManager().getFullName());

		String text = freemakerTemplete.processTempleteIntoString(model, "hr_request.ftl");

		StringBuilder subject = new StringBuilder();
		subject.append(employee.get().getFullName()).append(" ").append(type).append(" Request");

		String[] toArray = { employee.get().getManager().getEmail() };
		String[] ccArray = { requestDTO.getEmail(), env.getProperty("email.cc"), env.getProperty("email.cc1"),
				env.getProperty("email.cc2") };

		if (!EmailNotifier.pingSMTP("smtp.gmail.com", 587)) {
			EmailLog dto = new EmailLog();
			dto.setSubject(subject.toString());
			dto.setEmailBody(text);
			dto.setUserEmail(requestDTO.getEmail());
			dto.setTimestamp(new Date());
			dto.setDelivered(false);
			dto.setTo(toArray.toString());
			dto.setCc(ccArray.toString());
			employeeService.createEmailLog(dto);
			ApiJsonResponse response = new ApiJsonResponse(true,
					type + messageLookup.getMessage("user.request.postpone"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);

		}

		notifier.sendMail(subject.toString(), text, toArray, ccArray);

		ApiJsonResponse response = new ApiJsonResponse(true, type + messageLookup.getMessage("user.createRequest"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);

	}

	@PutMapping("/request/edit")
	public ResponseEntity<ApiJsonResponse> withdrawEmployeeRequest(@Valid @RequestParam(value = "id") Long id)
			throws Exception {
		Optional<EmployeeRequest> request = employeeService.findEmployeeRequestById(id);
		if (!request.isPresent()) {
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("user.notFound"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
		}
		request.get().setApprovalStatus(RequestApprovalStatusEnum.CANCELLED.getValue());
		employeeService.updateEmployeeRequest(request.get());

		

		Employee employee  = employeeService.findEmployeeByEmail(request.get().getEmail()).get(); 
	
			
			
		String type = (request.get().getRequestType() == 1) ? "Leave " : "Remote Work ";

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", employee.getFullName());
		model.put("email", request.get().getEmail());
		model.put("requestType", type);
		model.put("startDate", request.get().getStartDate());
		model.put("endDate", request.get().getEndDate());
		model.put("reason", request.get().getReason());
		model.put("remarks", request.get().getRemarks());
		model.put("submissionDate", request.get().getSubmissionDate());
		model.put("subject", type);
		String text = freemakerTemplete.processTempleteIntoString(model, "hr_request_withdraw.ftl");

		StringBuilder subject = new StringBuilder();
		subject.append(employee.getFullName()).append(" ").append(type).append(" Request Withdrawal");

		String[] toArray = {employee.getManager().getEmail()};
		String[] ccArray = { request.get().getEmail(), env.getProperty("email.cc"), env.getProperty("email.cc1"),
				env.getProperty("email.cc2") };

		if (!EmailNotifier.pingSMTP("smtp.gmail.com", 587)) {
			EmailLog dto = new EmailLog();
			dto.setSubject(subject.toString());
			dto.setEmailBody(text);
			dto.setUserEmail(request.get().getEmail());
			dto.setTimestamp(new Date());
			dto.setDelivered(false);
			dto.setTo(toArray.toString());
			dto.setCc(ccArray.toString());

			employeeService.createEmailLog(dto);
			ApiJsonResponse response = new ApiJsonResponse(true,
					type + messageLookup.getMessage("user.editRequest.cancel"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
		}

		notifier.sendMail(subject.toString(), text, toArray, ccArray);

		ApiJsonResponse response = new ApiJsonResponse(true, messageLookup.getMessage("user.editRequest.cancel"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);

	}

	@PutMapping("/request/update")
	public ResponseEntity<?> updateEmployeeRequest(@RequestParam(required = false) Long id,
			@RequestBody @Valid UpdateEmployeeRequestDTO dto) throws MessagingException, TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Optional<EmployeeRequest> request = employeeService.findEmployeeRequestById(id);

		if (!request.isPresent()) {
			ApiJsonResponse response = new ApiJsonResponse(false, "User Request not found");
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_FOUND);

		}
		request.get().setApprovalStatus(dto.getApprovalStatus());
		request.get().setApprovalDate(new Date());
		request.get().setApprovalRemarks(dto.getApprovalRemarks());
		request.get().setApprovedBy(
				employeeService.findEmployeeById(request.get().getEmployee().getManager().getId()).get());

		employeeService.updateEmployeeRequest(request.get());

		String type = (request.get().getRequestType() == 1) ? "Leave " : "Remote Work ";

		String approvalStatus = "";
		if (dto.getApprovalStatus() == 2)
			approvalStatus = RequestApprovalStatusEnum.APPROVED.name();
		else if (dto.getApprovalStatus() == 3)
			approvalStatus = RequestApprovalStatusEnum.REJECTED.name();

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", request.get().getName());
		model.put("email", request.get().getEmail());
		model.put("requestType", type);
		model.put("startDate", request.get().getStartDate());
		model.put("endDate", request.get().getEndDate());
		model.put("reason", request.get().getReason());
		model.put("remarks", request.get().getRemarks());
		model.put("submissionDate", request.get().getSubmissionDate());
		model.put("approvalRemarks", request.get().getApprovalRemarks());
		model.put("subject", type);
		model.put("pApprovalStatus", approvalStatus);
		String text = freemakerTemplete.processTempleteIntoString(model, "hr_request_approval.ftl");

		StringBuilder subject = new StringBuilder();
		subject.append(request.get().getName()).append(" ").append(type).append(" Request");

		String[] toArray = {request.get().getEmail()};
		String[] ccArray = {employeeService.findEmployeeByEmail(request.get().getEmail()).get().getManager().getEmail()
				,env.getProperty("email.cc"), env.getProperty("email.cc1"),env.getProperty("email.cc2") };

		if (!EmailNotifier.pingSMTP("smtp.gmail.com", 587)) {
			EmailLog log = new EmailLog();
			log.setSubject(subject.toString());
			log.setEmailBody(text);
			log.setUserEmail(request.get().getEmail());
			log.setTimestamp(new Date());
			log.setDelivered(false);
			log.setTo(toArray.toString());
			log.setCc(ccArray.toString());

			employeeService.createEmailLog(log);
			ApiJsonResponse response = new ApiJsonResponse(true,
					type + messageLookup.getMessage("user.editRequest.cancel"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);

		}

		notifier.sendMail(subject.toString(), text, toArray, ccArray);

		ApiJsonResponse response = new ApiJsonResponse(true, "Request has been updated.");
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);

	}

//	@GetMapping("/findByEmail")
//	public ResponseEntity<?> findEmployeeByEmail(@RequestParam(value = "email") String email) {
//		Optional<Employee> user = employeeService.findEmployeeByEmail(email);
//
//		if (!user.isPresent()) {
//			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("user.notFound"));
//			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
//		}
//		return new ResponseEntity<Optional<Employee>>(user, HttpStatus.OK);
//	}

	@GetMapping("/find/{employeeId}")
	public ResponseEntity<?> getEmployeeById(@PathVariable Long employeeId) {
		Optional<Employee> employee = employeeService.findEmployeeById(employeeId);

		if (!employee.isPresent()) {
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("employee.notFound"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_FOUND);
		}

		ViewEmployeeDTO employeeDto = new ViewEmployeeDTO();
		employeeDto.setId(employee.get().getId());
		employeeDto.setFirstName(employee.get().getFirstName());
		employeeDto.setLastName(employee.get().getLastName());
		employeeDto.setFatherName(employee.get().getFatherName());
		employeeDto.setCnic(employee.get().getCnic());
		employeeDto.setContactNo(employee.get().getContactNo());
		employeeDto.setEmail(employee.get().getEmail());
		employeeDto.setQualification(employee.get().getQualification());
		employeeDto.setEmployeeType(employee.get().getEmployeeType());
		employeeDto.setPersonalNo(employee.get().getPersonalNo());
		
		if (employee.get().getDesignation() != null) {
			employeeDto.setDesignationName(employee.get().getDesignation().getName());
			employeeDto.setDesignationId(employee.get().getDesignation().getId());
		}
		if (employee.get().getDepartment() != null) {
			employeeDto.setDepartmentName(employee.get().getDepartment().getName());
			employeeDto.setDepartmentId(employee.get().getDepartment().getId());
		}
		employeeDto.setEnabled(employee.get().getEnabled());
		employeeDto.setQualification(employee.get().getQualification());

		if (employee.get().getManager() != null) {
			employeeDto.setManagerId(employee.get().getManager().getId());
			employeeDto.setManagerName(employee.get().getManager().getFullName());
		}

		return new ResponseEntity<ViewEmployeeDTO>(employeeDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<?> getAllEmployees() {
		List<Employee> employees = employeeService.findAllEmployee();
		List<ListEmployeeDTO> employeeList = new ArrayList<>();
		employees.forEach(employee -> {
			ListEmployeeDTO employeeDto = new ListEmployeeDTO();
			employeeDto.setId(employee.getId());
			employeeDto.setFirstName(employee.getFirstName());
			employeeDto.setLastName(employee.getLastName());
			employeeDto.setFatherName(employee.getFatherName());
			employeeDto.setCnic(employee.getCnic());
			employeeDto.setContactNo(employee.getContactNo());
			employeeDto.setEmail(employee.getEmail());
			employeeDto.setEnabled(employee.getEnabled());
			employeeDto.setQualification(employee.getQualification());

//			if(employee.getAssignedHierarchyList() != null){
//				employee.getAssignedHierarchyList().forEach(hierarchy -> {
//					employeeDto.addAssignedHierarchy(new HierarchyDetailDTO(hierarchy.getId(), hierarchy.getName()));
//				});
//			}
			if (employee.getDesignation() != null) {
				employeeDto.setDesignationName(employee.getDesignation().getName());
			}
			if (employee.getDepartment() != null) {
				employeeDto.setDepartmentName(employee.getDepartment().getName());
			}
//			if(employee.getDistributions() != null){
//				List<String> names = employee.getDistributions().stream().map(p -> p.getName()).collect(Collectors.toList());
//				
//				employeeDto.setDistributionNames(names);
//			}
			if (employee.getManager() != null) {
				employeeDto.setManagerId(employee.getManager().getId());
				employeeDto.setManagerName(employee.getManager().getFullName());
			}

			employeeList.add(employeeDto);

		});
		return new ResponseEntity<List<ListEmployeeDTO>>(employeeList, HttpStatus.OK);
	}

	@RequestMapping(value = "/{employeeId}/disable", method = RequestMethod.POST)
	public ResponseEntity<?> disableEmployee(@PathVariable Long employeeId) {
		employeeService.disableEmployee(employeeId);

		ApiJsonResponse response = new ApiJsonResponse(true, "Employee has been disabled.");
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}

//	@GetMapping("/list")
//	public ResponseEntity<?> findEmployee(@RequestParam(value = "email") String email) {
//
//		ViewEmployeeDTO dto = new ViewEmployeeDTO();
//
//		Optional<Employee> user = employeeService.findEmployeeByEmail(email);
//
//		if (!user.isPresent()) {
//			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("user.notFound"));
//			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
//		}
//
//		List<EmployeeRequest> userRequest = employeeService.findEmployeeRequestByEmail(email);
//
//		dto.setName(user.get().getFirstName());
//		dto.setEmail(user.get().getEmail());
//		dto.setTotalLeaves(user.get().getTotalLeaves());
//		dto.setAvailedLeaves(0);
//
//		List<ViewEmployeeRequestDTO> dtoo = new ArrayList<ViewEmployeeRequestDTO>();
//
//		userRequest.forEach(s -> {
//			ViewEmployeeRequestDTO requestDTO = new ViewEmployeeRequestDTO();
//			requestDTO.setId(s.getId());
//			requestDTO.setName(s.getName());
//			requestDTO.setEmail(s.getEmail());
//			requestDTO.setStartDate(s.getStartDate());
//			requestDTO.setEndDate(s.getEndDate());
//			requestDTO.setApprovalStatus(s.getApprovalStatus());
//			requestDTO.setRemarks(s.getRemarks());
//			requestDTO.setReason(s.getReason());
//			requestDTO.setRequestType(s.getRequestType());
//			requestDTO.setSubmissionDate(s.getSubmissionDate());
//
//			dtoo.add(requestDTO);
//		});
//		dto.setUserRequest(dtoo);
//
//		return new ResponseEntity<ViewEmployeeDTO>(dto, HttpStatus.OK);
//
//	}

	@PostMapping("/add")
	public ResponseEntity<ApiJsonResponse> createEmployee(@Valid @RequestBody CreateEmployeeDTO dto) {

		Optional<Employee> emp = employeeService.findEmployeeByEmail(dto.getEmail());
		if (emp.isPresent()) {
			ApiJsonResponse response = new ApiJsonResponse(true, messageLookup.getMessage("user.email.duplicate"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);

		}

		Employee employee = new Employee();
		employee.setFirstName(dto.getFirstName());
		employee.setLastName(dto.getLastName());
		employee.setFatherName(dto.getFatherName());
		employee.setQualification(dto.getQualification());
		employee.setEmail(dto.getEmail());
		employee.setCnic(dto.getCnic());
		employee.setContactNo(dto.getContactNo());
		employee.setEnabled(dto.getEnabled());
		employee.setTotalLeaves(dto.getTotalLeaves());
		employee.setEmployeeType(dto.getEmployeeType());
		employee.setPersonalNo(dto.getPersonalNo());

		if (dto.getManagerId() != null) {
			Optional<Employee> emp1 = employeeService.findEmployeeById(dto.getManagerId());
			if (emp1.isPresent())
				employee.setManager(emp1.get());
		}

		if (dto.getDepartmentId() != null) {
			Optional<Department> department = employeeService.findDepartmentById(dto.getDepartmentId());
			if (department.isPresent())
				employee.setDepartment(department.get());

		}
		if (dto.getDesignationId() != null) {
			Optional<Designation> designation = employeeService.findDesignationById(dto.getDesignationId());
			if (designation.isPresent())
				employee.setDesignation(designation.get());
		}
		employeeService.addEmployee(employee);

		ApiJsonResponse response = new ApiJsonResponse(true, messageLookup.getMessage("employee.added"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}

	@PutMapping("/edit/{employeeId}")
	public ResponseEntity<ApiJsonResponse> editEmployee(@PathVariable long employeeId,
			@Valid @RequestBody UpdateEmployeeDTO dto) {

		Employee employee = employeeService.findEmployeeById(employeeId).get();

		employee.setFirstName(dto.getFirstName());
		employee.setLastName(dto.getLastName());
		employee.setFatherName(dto.getFatherName());
		employee.setQualification(dto.getQualification());
		employee.setEmail(dto.getEmail());
		employee.setCnic(dto.getCnic());
		employee.setContactNo(dto.getContactNo());
		employee.setEnabled(dto.getEnabled());
		employee.setEmployeeType(dto.getEmployeeType());
		employee.setPersonalNo(dto.getPersonalNo());

		if (dto.getManagerId() != null) {
		Optional<Employee> emp = employeeService.findEmployeeById(dto.getManagerId());
		if (emp.isPresent())
			employee.setManager(emp.get());
		}
		
		Optional<Department> department = employeeService.findDepartmentById(dto.getDepartmentId());
		if (department.isPresent())
			employee.setDepartment(department.get());

		Optional<Designation> designation = employeeService.findDesignationById(dto.getDesignationId());
		if (designation.isPresent())
			employee.setDesignation(designation.get());
		employeeService.updateEmployee(employee);
		ApiJsonResponse response = new ApiJsonResponse(true, messageLookup.getMessage("employee.editUser"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/department/", method = RequestMethod.GET)
	public ResponseEntity<?> getAllDepartments() {
		List<Department> departments = employeeService.findAllDepartment();
		List<ViewDepartmentDTO> deptList = new ArrayList<>();
		departments.forEach(department -> {
			ViewDepartmentDTO departmentDto = new ViewDepartmentDTO();
			departmentDto.setId(department.getId());
			departmentDto.setName(department.getName());
			departmentDto.setEnabled(department.isEnabled());

			deptList.add(departmentDto);
		});
		return new ResponseEntity<List<ViewDepartmentDTO>>(deptList, HttpStatus.OK);
	}

	@RequestMapping(value = "department/{departmentId}", method = RequestMethod.GET)
	public ResponseEntity<?> getDepartmentById(@PathVariable Long departmentId) {
		Optional<Department> department = employeeService.findDepartmentById(departmentId);
		if (!department.isPresent()) {
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("department.notFound"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_FOUND);
		}

		ViewDepartmentDTO departmentDto = new ViewDepartmentDTO();
		departmentDto.setId(department.get().getId());
		departmentDto.setName(department.get().getName());
		departmentDto.setEnabled(department.get().isEnabled());

		return new ResponseEntity<ViewDepartmentDTO>(departmentDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/department/add", method = RequestMethod.POST)
	public ResponseEntity<ApiJsonResponse> addDepartment(@Valid @RequestBody CreateDepartmentDTO departmentDto) {
		Department department = new Department();
		department.setName(departmentDto.getName());
		department.setEnabled(true);

		if (!employeeService.isDepartmentNameUnique(department)) {
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("department.nameNotUnique"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.BAD_REQUEST);
		}

		employeeService.addDepartment(department);
		ApiJsonResponse response = new ApiJsonResponse(true, messageLookup.getMessage("department.added"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/department/update/{departmentId}", method = RequestMethod.PUT)
	public ResponseEntity<ApiJsonResponse> updateDepartment(@PathVariable Long departmentId,
			@Valid @RequestBody UpdateDepartmentDTO departmentDto) {
		Optional<Department> department = employeeService.findDepartmentById(departmentId);
		if (department.isPresent()) {
			department.get().setName(departmentDto.getName());
			department.get().setEnabled(departmentDto.isEnabled());
		}
		if (!employeeService.isDepartmentNameUnique(department.get())) {
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("department.nameNotUnique"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.BAD_REQUEST);
		}

		employeeService.updateDepartment(department.get());
		ApiJsonResponse response = new ApiJsonResponse(true, messageLookup.getMessage("department.updated"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}

	@PostMapping("/designation/add")
	public ResponseEntity<ApiJsonResponse> addDesignation(@RequestBody @Valid CreateDesignationDTO dto) {

		Designation desig = new Designation();
		desig.setName(dto.getName());
		desig.setEnabled(true);
		employeeService.addDesignation(desig);

		ApiJsonResponse response = new ApiJsonResponse(true, messageLookup.getMessage("designation.add"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);

	}

	@GetMapping("/getServerTimestamp")
	public ResponseEntity<ApiJsonResponse> getCurrentServerTimestamp() {
		ApiJsonResponse response = new ApiJsonResponse(true, "Server Timestamp");
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/designation/", method = RequestMethod.GET)
	public ResponseEntity<?> getAllDesignation() {
		List<Designation> designations = employeeService.findAllDesignation();
		List<ViewDesignationDTO> designationList = new ArrayList<>();
		designations.forEach(designation -> {
			ViewDesignationDTO designationDto = new ViewDesignationDTO();
			designationDto.setId(designation.getId());
			designationDto.setName(designation.getName());
			designationDto.setEnabled(designation.isEnabled());

			designationList.add(designationDto);
		});
		return new ResponseEntity<List<ViewDesignationDTO>>(designationList, HttpStatus.OK);
	}

	@RequestMapping(value = "designation/{designationId}", method = RequestMethod.GET)
	public ResponseEntity<?> getDesignationById(@PathVariable Long designationId) {
		Optional<Designation> designation = employeeService.findDesignationById(designationId);

		if (!designation.isPresent()) {
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("designation.notFound"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_FOUND);
		}

		ViewDesignationDTO designationDto = new ViewDesignationDTO();
		designationDto.setId(designation.get().getId());
		designationDto.setName(designation.get().getName());
		designationDto.setEnabled(designation.get().isEnabled());

		return new ResponseEntity<ViewDesignationDTO>(designationDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/designation/update/{designationId}", method = RequestMethod.PUT)
	public ResponseEntity<ApiJsonResponse> updateDesignation(@PathVariable Long designationId,
			@Valid @RequestBody UpdateDesignationDTO designationDTO) {
		Optional<Designation> designation = employeeService.findDesignationById(designationId);
		ApiJsonResponse response;

		if (!designation.isPresent()) {
			response = new ApiJsonResponse(false, messageLookup.getMessage("designation.notFound"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}

		designation.get().setName(designationDTO.getName());
		designation.get().setEnabled(designationDTO.isEnabled());

		if (!employeeService.isDesignationNameUnique(designation.get())) {
			response = new ApiJsonResponse(false, messageLookup.getMessage("designation.nameNotUnique"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.BAD_REQUEST);
		}

		employeeService.updateDesignation(designation.get());

		response = new ApiJsonResponse(true, messageLookup.getMessage("designation.updated"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}

}
