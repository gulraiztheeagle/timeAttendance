//package com.hr.attendance.domain.tenant.scheduler;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import javax.mail.MessagingException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.hr.attendance.domain.tenant.employee.model.EmailLog;
//import com.hr.attendance.domain.tenant.employee.service.EmployeeService;
//import com.hr.attendance.domain.tenant.util.EmailNotifier;
//
//@Component
//public class ScheduledTask {
//	@Autowired
//	private EmployeeService userService;
//	@Autowired
//	private EmailNotifier notifier;
//
//	@Scheduled(fixedRate = 300000)
//	public void sendFailedEmails() {
//		if (EmailNotifier.pingSMTP("smtp.gmail.com",587)) {
//
//			List<EmailLog> lst = userService.findAllEmailLogByStatus(false);
//			lst.forEach(s -> {
//				try {
//					notifier.sendMail(s.getSubject(), s.getEmailBody(),s.getTo(),s.getCc().toCharArray());
//
//				} catch (MessagingException e) {
//					e.printStackTrace();
//				}
//				Optional<EmailLog> emailLog = userService.findEmailLogById(s.getId());
//				if (emailLog.isPresent()) {
//					emailLog.get().setDelivered(true);
//					emailLog.get().setDeliveryTimestamp(new Date());
//					userService.editEmailLog(emailLog.get());
//				}
//			});
//		}
//	}
//
//}
