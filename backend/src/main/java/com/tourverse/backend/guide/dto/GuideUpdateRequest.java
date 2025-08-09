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
public class GuideUpdateRequest {
	// Fields that a guide can update
	private String name;
	private String phone;
	private MultipartFile profilePicture;
	private String bio;
	private List<String> languages;
	private BigDecimal ratePerHour;
	private String location;
}