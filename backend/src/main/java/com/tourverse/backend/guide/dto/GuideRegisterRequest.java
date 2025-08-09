package com.tourverse.backend.guide.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideRegisterRequest {
	// Basic User Info
	private String name;
	private String email;
	private String phone;
	private String password;
	private MultipartFile profilePicture;

	// Guide-Specific Info
	private String aadhaarNumber;
	private String panNumber;
	private String bio;
	private List<String> languages;
	private BigDecimal ratePerHour;
	private String location;

	// Documents for Verification
	private MultipartFile aadhaarDocument;
	private MultipartFile panDocument;
	private MultipartFile guideCertificate;

	// OTP for verification step
	private String otp;
}