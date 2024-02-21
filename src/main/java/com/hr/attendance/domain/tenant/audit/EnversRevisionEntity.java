package com.hr.attendance.domain.tenant.audit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Table(name="audit_revision_log")
@RevisionEntity(value = EnversRevisionListener.class)
public class EnversRevisionEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    private int revisonId;
    @RevisionTimestamp
    private long revisionTimestamp;
    private long userId;
    
	public int getRevisonId() {
		return revisonId;
	}
	public void setRevisonId(int revisonId) {
		this.revisonId = revisonId;
	}
	public long getRevisionTimestamp() {
		return revisionTimestamp;
	}
	public void setRevisionTimestamp(long revisionTimestamp) {
		this.revisionTimestamp = revisionTimestamp;
	}
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}  
}
