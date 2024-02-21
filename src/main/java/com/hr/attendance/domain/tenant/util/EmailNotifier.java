package com.hr.attendance.domain.tenant.util;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class EmailNotifier {
	@Autowired
	private JavaMailSender sender;
	@Autowired
	private Environment env;
	
	
//	@Value("${notifier.mail.fromEmail}")
//	private String fromEmail;
	@Value("${notifier.mail.fromName}")
	private String fromName;
	
	

	@Value("${spring.mail.username}")
	private String fromEmail;
	@Value("${email.to}")
	private String toEmail;

	@Async
	public void sendMail(String subject, String body,String[] to,String[] cc) throws MessagingException {

		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(fromEmail);
		helper.setTo(to);
		helper.setCc(cc);
		helper.setText(body, true);
		helper.setSubject(subject);

		sender.send(message);

	}

	public static boolean pingSMTP(String url,int port) {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(url, port), 1000);
			return true;
		} catch (IOException e) {
			return false; // Either timeout or unreachable or failed DNS lookup.
		}
	}
	

	
	/*@Async
	public void sendPlainTextEmail(String fromEmail, String fromName, String toEmail, String toName, String subject, String body)
			throws UnsupportedEncodingException{
		final Email email = DefaultEmail.builder()
                .from(new InternetAddress(fromEmail,
                        fromName))
                .to(newArrayList(
                        new InternetAddress(toEmail,
                                toName)))
                .subject(subject)
                .body(body)
                .encoding("UTF-8").build();

        emailService.send(email);
	}*/
	
	@Async
	public void sendPlainTextEmail(String toEmail, String toName, String subject, String body)
			throws UnsupportedEncodingException, MessagingException{

		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(fromEmail);
		helper.setTo(toEmail);
		helper.setText(body, true);
		helper.setSubject(subject);

		sender.send(message);

//        emailService.send(email);
	}
	

}
