package com.missinglink.backend.module.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateReviewRequest {

    @NotNull(message = "Ride ID is required")
    private UUID rideId;

    @NotNull(message = "Reviewee ID is required")
    private UUID revieweeId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    private String comment;
}
