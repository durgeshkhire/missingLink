package com.missinglink.backend.module.review;

import com.missinglink.backend.entity.Review;
import com.missinglink.backend.entity.Ride;
import com.missinglink.backend.entity.User;
import com.missinglink.backend.entity.enums.BookingStatus;
import com.missinglink.backend.entity.enums.RideStatus;
import com.missinglink.backend.module.booking.BookingRepository;
import com.missinglink.backend.module.review.dto.CreateReviewRequest;
import com.missinglink.backend.module.review.dto.ReviewResponse;
import com.missinglink.backend.module.ride.RideRepository;
import com.missinglink.backend.module.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Submit a review
    @Transactional
    public ReviewResponse createReview(CreateReviewRequest request) {
        User reviewer = getCurrentUser();

        Ride ride = rideRepository.findById(request.getRideId())
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        // Ride must be completed
        if (ride.getStatus() != RideStatus.COMPLETED) {
            throw new RuntimeException("You can only review after the ride is completed");
        }

        User reviewee = userRepository.findById(request.getRevieweeId())
                .orElseThrow(() -> new RuntimeException("User to review not found"));

        // Cannot review yourself
        if (reviewer.getId().equals(reviewee.getId())) {
            throw new RuntimeException("You cannot review yourself");
        }

        // Check reviewer was part of this ride
        boolean isDriver = ride.getDriver().getId().equals(reviewer.getId());
        boolean isApprovedRider = bookingRepository
                .findByRideIdAndRiderId(ride.getId(), reviewer.getId())
                .map(b -> b.getStatus() == BookingStatus.APPROVED)
                .orElse(false);

        if (!isDriver && !isApprovedRider) {
            throw new RuntimeException("You were not part of this ride");
        }

        // Check already reviewed
        reviewRepository.findByReviewerIdAndRideIdAndRevieweeId(
                        reviewer.getId(), ride.getId(), reviewee.getId())
                .ifPresent(r -> {
                    throw new RuntimeException("You have already reviewed this person for this ride");
                });

        // Save review
        Review review = Review.builder()
                .reviewer(reviewer)
                .reviewee(reviewee)
                .ride(ride)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        reviewRepository.save(review);

        // Update reviewee's average rating
        updateUserRating(reviewee);

        return mapToResponse(review);
    }

    // Get all reviews for a user (their profile page)
    public List<ReviewResponse> getReviewsForUser(UUID userId) {
        return reviewRepository
                .findByRevieweeIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get reviews written by a user
    public List<ReviewResponse> getReviewsByUser(UUID userId) {
        return reviewRepository
                .findByReviewerIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update user average rating after new review
    private void updateUserRating(User user) {
        Double avg = reviewRepository.calculateAverageRating(user.getId());
        int count = reviewRepository.countByRevieweeId(user.getId());

        if (avg != null) {
            user.setAverageRating(
                    BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP));
        }
        user.setTotalRatings(count);
        userRepository.save(user);
    }

    // Map entity to response
    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .reviewerId(review.getReviewer().getId())
                .reviewerName(review.getReviewer().getName())
                .reviewerAvatar(review.getReviewer().getAvatarUrl())
                .revieweeId(review.getReviewee().getId())
                .revieweeName(review.getReviewee().getName())
                .rideId(review.getRide().getId())
                .originCity(review.getRide().getOriginCity())
                .destinationCity(review.getRide().getDestinationCity())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
