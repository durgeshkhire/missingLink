package com.missinglink.backend.module.user.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserProfileResponse {

    private UUID id;
    private String name;
    private String email;
    private String bio;
    private String avatarUrl;
    private String role;
    private boolean emailVerified;
    private BigDecimal averageRating;
    private int totalRatings;
    private LocalDateTime createdAt;
}