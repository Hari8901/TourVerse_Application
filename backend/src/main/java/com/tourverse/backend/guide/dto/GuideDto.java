package com.tourverse.backend.guide.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

import com.tourverse.backend.guide.entity.Guide;

@Data
@Builder
public class GuideDto {
	private Long id;
	private String name;
	private String email;
	private String phone;
	private String profilePictureUrl;
	private String bio;
	private List<String> languages;
	private BigDecimal ratePerHour;
	private String location;
	private Guide.VerificationStatus verificationStatus;
}
