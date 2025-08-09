package com.tourverse.backend.review.controller;

import com.tourverse.backend.auth.util.UserPrincipal;
import com.tourverse.backend.review.dto.ReviewRequest;
import com.tourverse.backend.review.dto.ReviewResponse;
import com.tourverse.backend.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Base mapping
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * Endpoint for an authenticated user (Traveler or Guide) to submit a review.
	 */
	@PostMapping("/reviews")
	public ResponseEntity<Void> submitReview(Authentication auth, @RequestBody ReviewRequest request) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		reviewService.submitReview(principal.getUser().getId(), principal.getUser().getRole(), request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Public endpoint to retrieve all reviews for a specific user (Traveler or
	 * Guide).
	 */
	@GetMapping("/public/reviews/{userId}")
	public ResponseEntity<List<ReviewResponse>> getReviewsForUser(@PathVariable Long userId) {
		List<ReviewResponse> reviews = reviewService.getReviewsForUser(userId);
		return ResponseEntity.ok(reviews);
	}
}