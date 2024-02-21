package com.hr.attendance.domain.tenant.enums;

public enum RequestApprovalStatusEnum {
	PENDING(1), APPROVED(2), REJECTED(3), CANCELLED(4);
	private int value;
	
	private RequestApprovalStatusEnum(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
}
