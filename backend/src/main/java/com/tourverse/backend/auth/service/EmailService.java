package com.tourverse.backend.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

	private final JavaMailSender mailSender;

	public void sendOtpEmail(String to, String otp, String subject) {
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText("<p>Your OTP is: <b>" + otp + "</b>. It is valid for 5 minutes.</p>", true);
			mailSender.send(msg);
			log.info("OTP email sent to {}", to);
			
		} catch (Exception e) {
			
			log.error("Failed to send OTP email to {}: {}", to, e.getMessage());
		}
	}
}