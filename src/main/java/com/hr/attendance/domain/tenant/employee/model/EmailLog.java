package com.hr.attendance.domain.tenant.employee.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class EmailLog {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id; 
	private Date timestamp;
	private String userEmail;
	private String errorMessage;
	private String emailBody;
	private String type;
	private String subject;
	private boolean delivered;
	private Date deliveryTimestamp;
	private String to;
	private String cc;
	
	
	public EmailLog() {}


	public EmailLog(long id, Date timestamp, String userEmail, String errorMessage, String emailBody, String type,
			String subject, boolean delivered, Date deliveryTimestamp, String to, String cc) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.userEmail = userEmail;
		this.errorMessage = errorMessage;
		this.emailBody = emailBody;
		this.type = type;
		this.subject = subject;
		this.delivered = delivered;
		this.deliveryTimestamp = deliveryTimestamp;
		this.to = to;
		this.cc = cc;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Date getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}


	public String getUserEmail() {
		return userEmail;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public String getEmailBody() {
		return emailBody;
	}


	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public boolean isDelivered() {
		return delivered;
	}


	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}


	public Date getDeliveryTimestamp() {
		return deliveryTimestamp;
	}


	public void setDeliveryTimestamp(Date deliveryTimestamp) {
		this.deliveryTimestamp = deliveryTimestamp;
	}


	public String getTo() {
		return to;
	}


	public void setTo(String to) {
		this.to = to;
	}


	public String getCc() {
		return cc;
	}


	public void setCc(String cc) {
		this.cc = cc;
	}
	

}
