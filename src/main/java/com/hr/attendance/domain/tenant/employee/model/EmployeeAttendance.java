package com.hr.attendance.domain.tenant.employee.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.hr.attendance.domain.tenant.audit.Auditable;

@Entity
@Table(name = "hr_time_attendance")
@EntityListeners(AuditingEntityListener.class)
public class EmployeeAttendance extends Auditable<String> {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String userName;
	private String userEmail;
	private int type;
	private Double latitude;
	private Double longitude;
	private String remarks;
	private String remoteType;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date appTimeStamp;

	public EmployeeAttendance() {
	}

	public EmployeeAttendance(long id, String userName, String userEmail, int type, Double latitude, Double longitude,
			Date appTimeStamp, String remarks) {
		super();
		this.id = id;
		this.userName = userName;
		this.userEmail = userEmail;
		this.type = type;
		this.latitude = latitude;
		this.longitude = longitude;
		this.appTimeStamp = appTimeStamp;
		this.remarks = remarks;
		this.remoteType=remoteType;
	}

	@NotNull
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@NotNull
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Date getAppTimeStamp() {
		return appTimeStamp;
	}

	public void setAppTimeStamp(Date appTimeStamp) {
		this.appTimeStamp = appTimeStamp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemoteType() {
		return remoteType;
	}

	public void setRemoteType(String remoteType) {
		this.remoteType = remoteType;
	}
	

}
