package com.hr.attendance.domain.tenant.audit;

import org.hibernate.envers.RevisionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnversRevisionListener implements RevisionListener{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void newRevision(Object revision) {
		EnversRevisionEntity revisionEntity = (EnversRevisionEntity) revision;
		
		try{
			revisionEntity.setUserId(123);
//			revisionEntity.setUserId(RequestContext.getCurrentUser().getId());
		}
		catch(Exception ex){
			log.info("Exception occurred while setting the audit author,"
					+ " couldn't retrieve user from RequestContext");
		}
	}
}