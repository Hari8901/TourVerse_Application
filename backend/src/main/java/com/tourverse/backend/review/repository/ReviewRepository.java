package com.tourverse.backend.review.repository;

import com.tourverse.backend.review.document.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
	// Find all reviews for a specific user
	List<Review> findByRevieweeId(Long revieweeId);

	// Check if a review already exists for a booking from a specific reviewer
	boolean existsByBookingIdAndReviewerId(Long bookingId, Long reviewerId);
}