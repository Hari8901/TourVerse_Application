package com.tourverse.backend.tourPackage.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateTourPackageRequest {
	private String title;
	private String description;
	private String location;
	private int durationDays;
	private BigDecimal price;
	private MultipartFile image;
	private List<String> inclusions;
}