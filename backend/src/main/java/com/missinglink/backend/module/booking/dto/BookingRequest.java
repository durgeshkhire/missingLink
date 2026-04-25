package com.missinglink.backend.module.booking.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Data
public class BookingRequest {

    @NotNull(message = "Ride ID is required")
    private UUID rideId;

    @Min(value = 1, message = "At least 1 seat required")
    @Max(value = 6, message = "Maximum 6 seats allowed")
    private int seatsRequested = 1;

    private String pickupNote;
}
