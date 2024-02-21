package com.hr.attendance.domain.tenant.employee.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "designation")
public class Designation {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private boolean enabled;
	
	public Designation() {}
	
	public Designation(long id, String name, boolean enabled) {
		super();
		this.id = id;
		this.name = name;
		this.enabled = enabled;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	
}
