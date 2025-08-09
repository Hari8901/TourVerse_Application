package com.tourverse.backend.tourPackage.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TourPackageDto {
	private Long id;
	private String title;
	private String description;
	private String location;
	private int durationDays;
	private BigDecimal price;
	private String imageUrl;
	private List<String> inclusions;
}