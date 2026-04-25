package com.missinglink.backend.module.review;

import com.missinglink.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    // All reviews received by a user (shown on profile)
    List<Review> findByRevieweeIdOrderByCreatedAtDesc(UUID revieweeId);

    // All reviews written by a user
    List<Review> findByReviewerIdOrderByCreatedAtDesc(UUID reviewerId);

    // Check if reviewer already reviewed this ride for this person
    Optional<Review> findByReviewerIdAndRideIdAndRevieweeId(
            UUID reviewerId, UUID rideId, UUID revieweeId);

    // Calculate average rating for a user
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewee.id = :userId")
    Double calculateAverageRating(@Param("userId") UUID userId);

    // Count total reviews for a user
    int countByRevieweeId(UUID revieweeId);

}
