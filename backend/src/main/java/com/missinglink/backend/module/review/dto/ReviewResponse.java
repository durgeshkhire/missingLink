package com.missinglink.backend.module.review.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReviewResponse {

    private UUID id;

    private UUID reviewerId;
    private String reviewerName;
    private String reviewerAvatar;

    private UUID revieweeId;
    private String revieweeName;

    private UUID rideId;
    private String originCity;
    private String destinationCity;

    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
