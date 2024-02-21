package com.hr.attendance.domain.tenant.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hr.attendance.domain.tenant.user.model.User;
import com.hr.attendance.domain.tenant.user.service.UserDetailsServiceImpl;


@Component
public class RequestContext {
	private static UserDetailsServiceImpl userService;
	private final static String DEFAULT_SCHEMA = "tmc_hr_prod";
	
	@Autowired
	public RequestContext(UserDetailsServiceImpl userService){
		RequestContext.userService = userService;
	}
	
	private static ThreadLocal<User> currentUser = new ThreadLocal<User>(){
		@Override
		protected User initialValue(){
			return null;
		}
	};
	
	/*
	 * private static ThreadLocal<String> currentTenantSchema = new
	 * ThreadLocal<String>(){
	 * 
	 * @Override protected String initialValue(){ return DEFAULT_SCHEMA; } };
	 */
	
	/*
	 * public static void setCurrentTenantSchema(String schemaName){
	 * currentTenantSchema.set(schemaName); }
	 */
	
	/*
	 * private static ThreadLocal<Tenant> currentTenant = new ThreadLocal<Tenant>(){
	 * 
	 * @Override protected Tenant initialValue(){ return null; } };
	 */
	
	public static void clear() {
		currentUser.remove();
//		currentTenant.remove();
//		currentTenantSchema.remove();
	}
	
	/*
	 * public static Tenant getCurrentTenant(){ return currentTenant.get(); }
	 */
	
	/*
	 * public static void setCurrentTenant(Tenant tenant){
	 * currentTenant.set(tenant); }
	 */

	/*
	 * public static String getCurrentTenantSchema() { return
	 * currentTenantSchema.get(); }
	 */
	
	public static User getCurrentUser() {
		if(currentUser.get() == null && SecurityContextHolder.getContext().getAuthentication() != null){
			User user = userService.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			setCurrentUser(user);
		}
		
		return currentUser.get();
	}

	public static void setCurrentUser(User user) {
		currentUser.set(user);
	}
}