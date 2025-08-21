package com.tourverse.backend.review.document;

import com.tourverse.backend.user.entity.User.Role;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "reviews")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@Builder
public class Review {

	@Id
	private String id;

	@Field("booking_id")
	private Long bookingId; // Links to the Booking in MySQL

	@Field("reviewer_id")
	private Long reviewerId;

	@Field("reviewer_role")
	private Role reviewerRole;

	@Field("reviewee_id")
	private Long revieweeId; // The user being reviewed

	@Field("rating")
	private int rating; // e.g., 1 to 5

	@Field("comment")
	private String comment;

	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();
}