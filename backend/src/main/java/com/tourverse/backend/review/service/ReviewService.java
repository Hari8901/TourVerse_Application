package com.tourverse.backend.review.service;

import com.tourverse.backend.booking.entity.Booking;
import com.tourverse.backend.booking.repository.BookingRepository;
import com.tourverse.backend.common.exceptions.UserNotFoundException;
import com.tourverse.backend.review.document.Review;
import com.tourverse.backend.review.dto.ReviewRequest;
import com.tourverse.backend.review.dto.ReviewResponse;
import com.tourverse.backend.review.repository.ReviewRepository;
import com.tourverse.backend.user.entity.User;
import com.tourverse.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Transactional
    public void submitReview(Long reviewerId, User.Role reviewerRole, ReviewRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found."));

        if (booking.getStatus() != Booking.BookingStatus.COMPLETED) {
            throw new IllegalStateException("Reviews can only be submitted for completed trips.");
        }

        if (!booking.getTraveler().getId().equals(reviewerId) && !booking.getGuide().getId().equals(reviewerId)) {
            throw new IllegalStateException("You are not authorized to review this booking.");
        }

        if (reviewRepository.existsByBookingIdAndReviewerId(request.getBookingId(), reviewerId)) {
            throw new IllegalStateException("You have already submitted a review for this booking.");
        }

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

        updateUserRating(revieweeId, request.getRating());
    }

    public List<ReviewResponse> getReviewsForUser(Long userId) {
        List<Review> reviews = reviewRepository.findByRevieweeId(userId);
        
        List<Long> reviewerIds = reviews.stream().map(Review::getReviewerId).collect(Collectors.toList());
        Map<Long, String> reviewerNames = userRepository.findAllById(reviewerIds).stream()
                .collect(Collectors.toMap(User::getId, User::getName));

        return reviews.stream()
                .map(review -> convertToResponseDto(review, reviewerNames.get(review.getReviewerId())))
                .collect(Collectors.toList());
    }

    @Async
    @Transactional
    public void updateUserRating(Long userId, int newRating) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found for rating update."));

            double currentTotalRating = user.getAverageRating() * user.getRatingCount();
            int newRatingCount = user.getRatingCount() + 1;
            double newAverageRating = (currentTotalRating + newRating) / newRatingCount;

            user.setAverageRating(newAverageRating);
            user.setRatingCount(newRatingCount);
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Failed to update user rating for userId: {}", userId, e);
        }
    }

    private ReviewResponse convertToResponseDto(Review review, String reviewerName) {
        return ReviewResponse.builder()
                .id(review.getId())
                .bookingId(review.getBookingId())
                .reviewerId(review.getReviewerId())
                .reviewerName(reviewerName)
                .reviewerRole(review.getReviewerRole())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}