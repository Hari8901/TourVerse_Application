package com.tourverse.backend.review.dto;

import com.tourverse.backend.user.entity.User.Role;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponse {
	private String id;
	private Long bookingId;
	private Long reviewerId;
	private String reviewerName; // We'll add this for context
	private Role reviewerRole;
	private int rating;
	private String comment;
	private LocalDateTime createdAt;
}