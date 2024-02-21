package com.hr.attendance.domain.tenant.user.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;


public interface CalendarService {

	public LocalDate convertFromDateToLocalDate(Date date);
	
//	String getShortDayName(Date date);
	static Date getTimeStamp(){
		return Date.from(Instant.now());
	}
}
