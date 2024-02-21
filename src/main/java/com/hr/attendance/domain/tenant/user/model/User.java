package com.hr.attendance.domain.tenant.user.model;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hr.attendance.domain.tenant.audit.Auditable;
import com.hr.attendance.domain.tenant.employee.model.Employee;


@Entity
@Table(name = "user")
public class User extends Auditable<String> implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String password;
	
	private boolean enabled;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private Date passwordChangedDate;
	
	private List<Role> roles;
	private Employee refEmployee;
	
	Set<GrantedAuthority> permissions;
	
    public User(){
    	this.roles = new ArrayList<>();
    }
    
    public User(Long id, String username, Employee refEmployee){
    	this.id = id;
    	this.username = username;
    	this.accountNonLocked = true;
    	this.enabled = true;
    	this.refEmployee = refEmployee;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    
    public void setId(Long id){
    	this.id=id;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	@ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	public List<Role> getAuthorities() {
		return this.roles;
	}
	
	public void setAuthorities(List<Role> newRoles){
		this.roles = newRoles;
	}
	
	public void addRole(Role role){
		this.roles.add(role);
	}
	
	//Indicates whether the user's account has expired. An expired account cannot be authenticated.
	@Transient
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}
	
	public void setAccountNonLocked(boolean flag){
		this.accountNonLocked = flag;
	}
	
	//Indicates whether the user's credentials (password) has expired. Expired credentials prevent authentication.
	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	
	public void setCredentialsNonExpired(boolean flag){
		this.credentialsNonExpired = flag;
	}
	
	//Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	@Temporal(TIMESTAMP)
	public Date getPasswordChangedDate() {
		return passwordChangedDate;
	}

	public void setPasswordChangedDate(Date passwordChangeDate) {
		this.passwordChangedDate = passwordChangeDate;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="employee_id", nullable = false)
	public Employee getRefEmployee() {
		return refEmployee;
	}

	public void setRefEmployee(Employee refEmployee) {
		this.refEmployee = refEmployee;
	}
	
	@Transient
	public Set<GrantedAuthority> getPermissions(){
		if(permissions != null && !permissions.isEmpty())
			return permissions;
		
		permissions = new HashSet<>();
		for (Role role : this.getAuthorities()) {
			role.getPermissions().forEach(permission -> {
				GrantedAuthority authority = new SimpleGrantedAuthority(permission.getAuthority());
				if (!permissions.contains(authority))
					permissions.add(authority);
			});
		}
		
		return permissions;
	}
}
