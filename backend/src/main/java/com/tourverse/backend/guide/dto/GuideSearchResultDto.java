package com.tourverse.backend.guide.dto;

import com.tourverse.backend.guide.entity.Guide;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class GuideSearchResultDto {
	// Guide Profile Info
	private Long id;
	private String name;
	private String profilePictureUrl;
	private String bio;
	private List<String> languages;
	private String location;
	private BigDecimal ratePerHour;
	private Guide.VerificationStatus verificationStatus;

	// Availability for the Searched Date
	private List<LocalTime> availableSlots;
}