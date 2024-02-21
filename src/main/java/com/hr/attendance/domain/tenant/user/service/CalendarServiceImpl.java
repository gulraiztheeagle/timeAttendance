package com.hr.attendance.domain.tenant.user.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Service;
@Service
public class CalendarServiceImpl implements CalendarService{

	public LocalDate convertFromDateToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
}
