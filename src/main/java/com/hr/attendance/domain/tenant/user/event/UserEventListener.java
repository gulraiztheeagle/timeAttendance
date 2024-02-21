package com.hr.attendance.domain.tenant.user.event;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.hr.attendance.domain.tenant.config.FreemakerTemplete;
import com.hr.attendance.domain.tenant.util.EmailNotifier;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;


/**
 * This listener notifies employee's on the employee email address with details about retrieval of password 
   * and details of user creation.
 * @author Adil Khalil
 *
 */
@Component
public class UserEventListener {
	@Autowired
	private FreemakerTemplete freemakerTemplete;
	
	@Autowired
	private EmailNotifier emailNotifier;
	@Value("${webclient.passwordResetUrl}")
	private String webPasswordResetUrl;
	
	@EventListener
	public void handleUserCreationEvent(UserCreatedEvent event) throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
	

//		StringBuilder builder = new StringBuilder();
//		builder.append("Your username has been created for TMC Time Attendance app.").append("\n");`
//		builder.append("Username: ").append(event.getUser().getUsername()).append("\r\n\r\n");
//		builder.append("Password: ").append(event.getPlainPassword()).append(" \r\n\r\n");
//		builder.append("Please change your password after you login").append("\r\n\r\n");
//		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("username", event.getUser().getUsername());
		model.put("password", event.getPlainPassword());
		model.put("name", event.getUser().getRefEmployee().getFirstName());
//		builder.append(webPasswordResetUrl);

		String body = freemakerTemplete.processTempleteIntoString(model, "user_created.ftl");

		
		
//		String body = builder.toString();
//		emailNotifier.sendMail("TMC Attendance App User Created", event.getUser().getRefEmployee().getEmail(), body);
			emailNotifier.sendPlainTextEmail(event.getUser().getRefEmployee().getEmail(), event.getUser().getRefEmployee().getFullName(), "TMC Attendance App User Created", body);
	}
	
	
}
