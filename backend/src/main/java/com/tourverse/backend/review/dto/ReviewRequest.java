package com.tourverse.backend.review.dto;

import lombok.Data;

@Data
public class ReviewRequest {
	private Long bookingId;
	private int rating; // e.g., 1-5
	private String comment;
}