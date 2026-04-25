package com.missinglink.backend.module.review;

import com.missinglink.backend.common.response.ApiResponse;
import com.missinglink.backend.module.review.dto.CreateReviewRequest;
import com.missinglink.backend.module.review.dto.ReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // POST /api/v1/reviews — submit a review
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.createReview(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Review submitted successfully"));
    }

    // GET /api/v1/reviews/user/{userId} — get reviews for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsForUser(
            @PathVariable UUID userId) {
        List<ReviewResponse> reviews = reviewService.getReviewsForUser(userId);
        return ResponseEntity.ok(
                ApiResponse.success(reviews, "Reviews fetched successfully"));
    }

    // GET /api/v1/reviews/by/{userId} — get reviews written by a user
    @GetMapping("/by/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByUser(
            @PathVariable UUID userId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(
                ApiResponse.success(reviews, "Reviews fetched successfully"));
    }
}