package com.missinglink.backend.module.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BookingResponse {
    private UUID id;

    private UUID rideId;
    private String originCity;
    private String destinationCity;
    private LocalDateTime departureTime;
    private String carName;
    private String carNumber;

    private UUID riderId;
    private String riderName;
    private String riderEmail;

    private int seatsRequested;
    private String status;
    private String pickupNote;
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
}
