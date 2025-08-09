package com.tourverse.backend.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class GuideVerificationDto {
	private Long id;
	private String name;
	private String email;
	private String phone;
	private String location;
	private List<String> languages;
	private BigDecimal ratePerHour;

	// Document URLs for verification
	private String aadhaarDocumentUrl;
	private String panDocumentUrl;
	private String guideCertificateUrl;
}