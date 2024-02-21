package com.hr.attendance.domain.tenant.user.event;

import org.springframework.context.ApplicationEvent;

import com.hr.attendance.domain.tenant.user.model.User;

@SuppressWarnings("serial")
public class UserCreatedEvent extends ApplicationEvent {
	private final User user;
	private final String plainPassword;
	
	public UserCreatedEvent(User user, String plainPassword) {
		super(user);
		this.user = user;
		this.plainPassword = plainPassword;
	}
	public User getUser() {
		return user;
	}
	
	public String getPlainPassword() {
		return plainPassword;
	}
}
