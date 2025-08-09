package com.tourverse.backend.review.service;

import com.tourverse.backend.booking.entity.Booking;
import com.tourverse.backend.booking.repository.BookingRepository;
import com.tourverse.backend.review.document.Review;
import com.tourverse.backend.review.dto.ReviewRequest;
import com.tourverse.backend.review.dto.ReviewResponse;
import com.tourverse.backend.review.repository.ReviewRepository;
import com.tourverse.backend.user.entity.User;
import com.tourverse.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    /**
     * Submits a review for a completed booking.
     *
     * @param reviewerId   The ID of the user submitting the review.
     * @param reviewerRole The role of the user submitting the review.
     * @param request      The DTO containing the review details.
     */
    @Transactional
    public void submitReview(Long reviewerId, User.Role reviewerRole, ReviewRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found."));

        // Validation 1: Ensure the booking is completed before a review can be left.
        // You would typically have a cron job or a manual process to mark bookings as COMPLETED.
        if (booking.getStatus() != Booking.BookingStatus.COMPLETED) {
            throw new IllegalStateException("Reviews can only be submitted for completed trips.");
        }

        // Validation 2: Ensure the reviewer is part of this booking.
        if (!booking.getTraveler().getId().equals(reviewerId) && !booking.getGuide().getId().equals(reviewerId)) {
            throw new IllegalStateException("You are not authorized to review this booking.");
        }

        // Validation 3: Prevent a user from reviewing the same booking twice.
        if (reviewRepository.existsByBookingIdAndReviewerId(request.getBookingId(), reviewerId)) {
            throw new IllegalStateException("You have already submitted a review for this booking.");
        }

        // Determine who is being reviewed
        Long revieweeId = reviewerRole == User.Role.TRAVELER ? booking.getGuide().getId() : booking.getTraveler().getId();

        Review review = Review.builder()
                .bookingId(request.getBookingId())
                .reviewerId(reviewerId)
                .reviewerRole(reviewerRole)
                .revieweeId(revieweeId)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        reviewRepository.save(review);

        // Update the average rating for the user who was reviewed
        updateUserRating(revieweeId, request.getRating());
    }

    /**
     * Retrieves all reviews for a specific user.
     * @param userId The ID of the user whose reviews are to be fetched.
     * @return A list of review response DTOs.
     */
    public List<ReviewResponse> getReviewsForUser(Long userId) {
        List<Review> reviews = reviewRepository.findByRevieweeId(userId);
        
        // Fetch reviewer names for better context in the response
        List<Long> reviewerIds = reviews.stream().map(Review::getReviewerId).collect(Collectors.toList());
        Map<Long, String> reviewerNames = userRepository.findAllById(reviewerIds).stream()
                .collect(Collectors.toMap(User::getId, User::getName));

        return reviews.stream()
                .map(review -> convertToResponseDto(review, reviewerNames.get(review.getReviewerId())))
                .collect(Collectors.toList());
    }

    // --- Private Helper Methods ---

    @Transactional
    private void updateUserRating(Long userId, int newRating) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for rating update."));

        double currentTotalRating = user.getAverageRating() * user.getRatingCount();
        int newRatingCount = user.getRatingCount() + 1;
        double newAverageRating = (currentTotalRating + newRating) / newRatingCount;

        user.setAverageRating(newAverageRating);
        user.setRatingCount(newRatingCount);
        userRepository.save(user);
    }

    private ReviewResponse convertToResponseDto(Review review, String reviewerName) {
        return ReviewResponse.builder()
                .id(review.getId())
                .bookingId(review.getBookingId())
                .reviewerId(review.getReviewerId())
                .reviewerName(reviewerName) // Enriched with the reviewer's name
                .reviewerRole(review.getReviewerRole())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}