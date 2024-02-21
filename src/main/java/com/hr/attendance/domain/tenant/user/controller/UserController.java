package com.hr.attendance.domain.tenant.user.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hr.attendance.domain.tenant.employee.model.Employee;
import com.hr.attendance.domain.tenant.employee.service.EmployeeService;
import com.hr.attendance.domain.tenant.exception.type.InvalidPasswordStrengthException;
import com.hr.attendance.domain.tenant.security.RequestContext;
import com.hr.attendance.domain.tenant.user.dto.CreateUserDTO;
import com.hr.attendance.domain.tenant.user.dto.PermissionDTO;
import com.hr.attendance.domain.tenant.user.dto.RoleDTO;
import com.hr.attendance.domain.tenant.user.dto.UpdateUserDTO;
import com.hr.attendance.domain.tenant.user.dto.UserDetailsDTO;
import com.hr.attendance.domain.tenant.user.dto.UserListItemDTO;
import com.hr.attendance.domain.tenant.user.dto.UserRoleDTO;
import com.hr.attendance.domain.tenant.user.event.UserCreatedEvent;
import com.hr.attendance.domain.tenant.user.model.Permission;
import com.hr.attendance.domain.tenant.user.model.Role;
import com.hr.attendance.domain.tenant.user.model.User;
import com.hr.attendance.domain.tenant.user.service.RoleService;
import com.hr.attendance.domain.tenant.user.service.UserDetailsServiceImpl;
import com.hr.attendance.domain.tenant.user.service.UserMappingHelper;
import com.hr.attendance.domain.tenant.util.ApiJsonResponse;
import com.hr.attendance.domain.tenant.util.ApiMessageLookup;
import com.hr.attendance.domain.tenant.util.EmailNotifier;

@RestController
@RequestMapping(value="/user")
@CrossOrigin(origins = "*")
public class UserController {
	@Autowired
	private UserDetailsServiceImpl userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserMappingHelper userMappingHelper;
	@Autowired
	private EmailNotifier emailNotifier;
	@Autowired
	private ApiMessageLookup messageLookup;
	@Autowired
	private EmployeeService employeeService;
	@Value("${webclient.passwordResetUrl}")
	private String webPasswordResetUrl;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
//	@Autowired
//	private TenantService tenantService;
	
	//=========================== User Management =========================//
//	@PreAuthorize("hasAuthority('VIEW_ALL_USERS')")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<UserListItemDTO>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		List<UserListItemDTO> allUsersDtoList = new ArrayList<>();
		
		users.forEach(user -> {
			UserListItemDTO listItem = new UserListItemDTO();
			listItem.setEmail(user.getRefEmployee().getEmail());
			listItem.setEnabled(user.isEnabled());
			listItem.setId(user.getId());
			if(user.getRefEmployee() != null){
				listItem.setMobileNo(user.getRefEmployee().getContactNo());
				listItem.setName(user.getRefEmployee().getFullName());
				listItem.setUserDesignation(user.getRefEmployee().getDesignation() != null ? user.getRefEmployee().getDesignation().getName() : null);
			}
			listItem.setUsername(user.getUsername());
			listItem.setRole(user.getAuthorities().get(0).getDescription());
			
			allUsersDtoList.add(listItem);
		});
		
		return new ResponseEntity<List<UserListItemDTO>>(allUsersDtoList, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAuthority('VIEW_A_USER')")
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable Long userId) {
		if (userId == null) {
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("user.getUser.nullId"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
		}
		User user = userService.getUserByUserId(userId);
		if (user == null) {
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("user.getUser.invalidUserId"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
		}
		
		List<UserRoleDTO> rolesDtos = new ArrayList<>();
		
		user.getAuthorities().parallelStream().forEach(r -> {
			UserRoleDTO roleDto = new UserRoleDTO();
			 roleDto.setId(r.getId());
			 roleDto.setName(r.getAuthority());
			 roleDto.setEnabled(r.isEnabled());
			 roleDto.setDescription(r.getDescription());
			 
			 rolesDtos.add(roleDto);
		});
		
		UserDetailsDTO userDTO = new UserDetailsDTO(user.getId(), user.getRefEmployee().getFullName(), user.getUsername(), user.getRefEmployee().getContactNo(), user.getRefEmployee().getEmail(), null, 
				user.getCreatedDate(), user.getCreatedBy(), user.isEnabled(), !user.isAccountNonLocked(), !user.isAccountNonExpired(), !user.isCredentialsNonExpired(),
				user.getAuthorities().get(0).getDescription());
		
		userDTO.setRoles(rolesDtos);
		if(user.getRefEmployee() != null){
			userDTO.setDesignation(user.getRefEmployee().getDesignation() != null ? user.getRefEmployee().getDesignation().getName() : null);
		}
				
		return new ResponseEntity<UserDetailsDTO>(userDTO, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('CREATE_A_USER')")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<ApiJsonResponse> createUser(@Valid @RequestBody CreateUserDTO newUser){
		/*
		 * String domainName =
		 * tenantService.getDomainNameByTenantSchemaName(RequestContext.
		 * getCurrentTenantSchema());
		 * 
		 * StringBuilder usernameBuilder = new StringBuilder();
		 * usernameBuilder.append(newUser.getUsername()).append("@").append(domainName);
		 * 
		 * String username = usernameBuilder.toString();
		 */
		
		if (userService.userExists(newUser.getUsername())) {
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("user.createUser.duplicateUser"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(userService.userExists(newUser.getEmployeeId())){
			ApiJsonResponse response = new ApiJsonResponse(false, "A User for the specified Employee already exists.");
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		log.debug("hi uer is being created ");
		final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
		String randomPassword ="Password1!"; //RandomStringUtils.random( 8, characters );
		
		User user = new User();
		user.setUsername(newUser.getUsername());
		user.setPassword(randomPassword);

		Optional<Employee> employee = employeeService.findEmployeeById(newUser.getEmployeeId());
		if (employee.isPresent())
		{
		user.setRefEmployee(employee.get());
		}
//		log.debug(user.getRefEmployee().toString());
		if(newUser.getRoleIds() != null){
			for(long roleId: newUser.getRoleIds()){
				user.addRole(roleService.getRoleProxy(roleId).get());
			}
		}
		
		user = userService.createUser(user);
		eventPublisher.publishEvent(new UserCreatedEvent(user, randomPassword));
		
		ApiJsonResponse response = new ApiJsonResponse(true, messageLookup.getMessage("user.createUser.UserCreated"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('UPDATE_A_USER')")
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public ResponseEntity<ApiJsonResponse> updateUser(@RequestBody UpdateUserDTO updateUserDto) {
		User user = userService.getUserByUserId(updateUserDto.getId());
		ApiJsonResponse response;
		if(user == null){
			response = new ApiJsonResponse(false, messageLookup.getMessage("user.updateUser.invalidUserId"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
		}
		List<Role> updatedRoles = new ArrayList<>();
		user.getAuthorities().clear();
		updateUserDto.getRoles().forEach(roleDtoItem ->{
			Role role = roleService.findRoleById(roleDtoItem.getId()).get();
			if(role != null){
				updatedRoles.add(role);
			}
		});
		
		user.setAuthorities(updatedRoles);
		user.setEnabled(updateUserDto.isEnabled());
		user.setCredentialsNonExpired(!updateUserDto.isCredentialsExpired());
		user.setAccountNonLocked(!updateUserDto.isAccountLocked());
		
		userService.updateUser(user);
		
		response = new ApiJsonResponse(true, messageLookup.getMessage("user.updateUser.userUpdated"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ResponseEntity<ApiJsonResponse> changePassword(@RequestBody Map<String, Object> model,@RequestParam Long userId,
			Principal principal) throws InvalidPasswordStrengthException {
		String newPassword = null;
		if (model.containsKey("newPassword")) {
			newPassword = model.get("newPassword").toString();
		}
		
		if (StringUtils.isBlank(newPassword)) {
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("password.changePasswrod.emptyNewPassword"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		
//		String oldPassword = userService.findByUsername(principal.getName()).getPassword();
//
//		if (newPassword.toString().equals(oldPassword.toString())) {
//			ApiJsonResponse response = new ApiJsonResponse(false,
//					messageLookup.getMessage("password.changePassword.differentNewPassword"));
//			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
//		}

		userService.changePassword(newPassword, userId);

		ApiJsonResponse response = new ApiJsonResponse(true, messageLookup.getMessage("password.changePassword.passwordChanged"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/requestNewPassword", method = RequestMethod.POST)
	public ResponseEntity<ApiJsonResponse> requestNewPassword(@RequestParam(name="email", required=true) String email) throws UnsupportedEncodingException, MessagingException{
		ApiJsonResponse response;
		User user = userService.findUserByEmail(email);
		if(user == null){
			response = new ApiJsonResponse(false, messageLookup.getMessage("password.requestPasswordResetToken.invalidEmail"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		
		final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
		String randomPassword =RandomStringUtils.random( 8, characters );
		
		try {
			userService.changePassword(randomPassword, user.getId());
		} catch (InvalidPasswordStrengthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String body = "Your new password has been generated. \r\n " +randomPassword;
		emailNotifier.sendPlainTextEmail(user.getRefEmployee().getEmail(), user.getRefEmployee().getFullName(), "TMC Attendance App Password Reset", body);
		response = new ApiJsonResponse(true, messageLookup.getMessage("password.requestPasswordResetToken.emailSent"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="/requestPasswordResetToken", method = RequestMethod.GET)
	public ResponseEntity<ApiJsonResponse> requestPasswordResetToken(@RequestParam(name="email", required=true) String email) throws UnsupportedEncodingException, MessagingException{
		ApiJsonResponse response;
		User user = userService.findUserByEmail(email);
		if(user == null){
			response = new ApiJsonResponse(false, messageLookup.getMessage("password.requestPasswordResetToken.invalidEmail"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		String token = userService.createPasswordResetTokenForUser(user);
		String body = "Reset your password by clicking on this url \r\n "+webPasswordResetUrl+ "/reset/"+ 
				token  + "/" +  user.getId();
		emailNotifier.sendPlainTextEmail(user.getRefEmployee().getEmail(), user.getRefEmployee().getFullName(), "Password Reset", body);
		response = new ApiJsonResponse(true, messageLookup.getMessage("password.requestPasswordResetToken.emailSent"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/resetPasswordByPasswordResetToken/{id}/{token}", method = RequestMethod.POST)
	public ResponseEntity<ApiJsonResponse> resetPassword(@PathVariable Long id, @PathVariable String token,
			@RequestBody Map<String, Object> model) throws InvalidPasswordStrengthException{
		
		String newPassword = null;	
		if(model.containsKey("newPassword")){
			newPassword= model.get("newPassword").toString();
		}
		
		if(StringUtils.isBlank(newPassword)){
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("password.resetPassword.emptyNewPassword"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		
		String jsonresponse = userService.validatePasswordResetToken(id, token);
		
		if(jsonresponse != null){
			ApiJsonResponse response = new ApiJsonResponse(false, jsonresponse);
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		
		userService.changePassword(newPassword, id);
		
		userService.removePasswordResetToken(token);
		
		ApiJsonResponse response = new ApiJsonResponse(true, messageLookup.getMessage("password.resetPassword.passwordChanged"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
		
	}
	//========================= Role Management =======================//
	
//	@PreAuthorize("hasAuthority('CREATE_A_ROLE')")
	@RequestMapping(value = "/role/add", method = RequestMethod.POST)
	public ResponseEntity<ApiJsonResponse> createRole(@RequestBody RoleDTO dto) {
		ApiJsonResponse response;
		if(StringUtils.isBlank(dto.getName())|| StringUtils.containsWhitespace(dto.getName())){
			response = new ApiJsonResponse(false, messageLookup.getMessage("role.createRole.emptyRoleName"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		// Checks for avoiding duplicate role names
		if (roleService.isRoleDuplicate(dto.getName())) {
			response = new ApiJsonResponse(false, messageLookup.getMessage("role.createRole.dulicateRole"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(dto.getPermissions() == null || dto.getPermissions().isEmpty()){
			response = new ApiJsonResponse(false, messageLookup.getMessage("role.createRole.numberOfPermission"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		} else{
			for(PermissionDTO permissionDto : dto.getPermissions()){
				if(!roleService.isPermissionValid(permissionDto.getAuthority())){
					response = new ApiJsonResponse(false, messageLookup.getMessage("role.createRole.invalidPermission"));
					return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
				}
			}
		}
		
		if(dto.isEnabled() == null){
			dto.setEnabled(true);
		}
		
		Role newRole = new Role();
		newRole.setAuthority(dto.getName());
		newRole.setDescription(dto.getDescription());
		newRole.setEnabled(dto.isEnabled());
		if(dto.getPermissions() != null && !dto.getPermissions().isEmpty())
		{
			List<Permission> permissions = new ArrayList<>();
			dto.getPermissions().forEach(permissionDto -> {
				Permission permission = roleService.findPermissionById(permissionDto.getAuthority()).get();
				permissions.add(permission);
			});
			
			newRole.setPermissions(permissions);
		}
		
		roleService.createRole(newRole);
		
		response = new ApiJsonResponse(true, messageLookup.getMessage("role.createRole.roleCreated", new Object[]{newRole.getAuthority()}));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAuthority('VIEW_A_ROLE')")
	@RequestMapping(value = "/role/{roleId}", method = RequestMethod.GET)
	public ResponseEntity<?> getRole(@PathVariable Long roleId){
		Role role =  roleService.findRoleById(roleId).get();
		if(role == null){
			ApiJsonResponse response = new ApiJsonResponse(false, messageLookup.getMessage("role.notFound"));
			return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_FOUND);
		}
		
		RoleDTO roleDto = new RoleDTO();
		roleDto.setId(role.getId());
		roleDto.setName(role.getAuthority());
		roleDto.setEnabled(role.isEnabled());
		roleDto.setDescription(role.getDescription());
		if(roleDto.getPermissions() != null){
			List<PermissionDTO> perms = new ArrayList<>();
			
			roleDto.getPermissions().forEach(p -> {
				perms.add(new PermissionDTO(p.getAuthority(), p.getDescription()));
			});
			
			roleDto.setPermissions(perms);
		}
		
		return new ResponseEntity<RoleDTO>(roleDto, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAnyAuthority('VIEW_ALL_ROLES')")
	@RequestMapping(value = "/role/", method = RequestMethod.GET)
	public ResponseEntity<List<RoleDTO>> getAllRoles() {
		List<RoleDTO> rolesDtos = new ArrayList<>();
		List<Role> roles = roleService.findAllRoles();
		
		roles.forEach(role -> {
			RoleDTO dto = new RoleDTO();
			dto.setId(role.getId());
			dto.setName(role.getAuthority());
			dto.setDescription(role.getDescription());
			dto.setEnabled(role.isEnabled());
			
			List<PermissionDTO> permDtos = new ArrayList<>();
			role.getPermissions().forEach(per -> {
				PermissionDTO perDto = new PermissionDTO();
				perDto.setAuthority(per.getAuthority());
				perDto.setDescription(per.getDescription());
				
				permDtos.add(perDto);
			});
			
			dto.setPermissions(permDtos);
			
			rolesDtos.add(dto);
		});
		return new ResponseEntity<List<RoleDTO>>(rolesDtos, HttpStatus.OK);
	}

//	@PreAuthorize("hasAnyAuthority('UPDATE_A_ROLE')")
	@RequestMapping(value = "/role/edit", method = RequestMethod.PUT)
	public ResponseEntity<ApiJsonResponse> updateRole(@RequestBody RoleDTO authorityDTO) {
		ApiJsonResponse response;
		Role authorityEntityFromDTO = userMappingHelper.convertRoleDtoToDomainModel(authorityDTO);
		if(authorityDTO != null && authorityDTO.getId() != null){
			Role authorityEntity = roleService.findRoleById(authorityDTO.getId()).get();
			
			if(authorityEntity != null){
				authorityEntity.setDescription(authorityEntityFromDTO.getDescription());
				authorityEntity.setEnabled(authorityEntityFromDTO.isEnabled());
				authorityEntity.setAuthority(authorityEntityFromDTO.getAuthority());
				if(authorityEntityFromDTO.getPermissions() == null || authorityEntityFromDTO.getPermissions().isEmpty()){
					response = new ApiJsonResponse(false, messageLookup.getMessage("role.createRole.numberOfPermission"));
					return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
				}
				authorityEntity.setPermissions(authorityEntityFromDTO.getPermissions());			
				roleService.updateRole(authorityEntity);
				
				response = new ApiJsonResponse(true, messageLookup.getMessage("role.editRole.roleUpdated"));
				return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.OK);
			}
		}
		response = new ApiJsonResponse(false, messageLookup.getMessage("role.editRole.invalidRole"));
		return new ResponseEntity<ApiJsonResponse>(response, HttpStatus.NOT_ACCEPTABLE);
	}
}
