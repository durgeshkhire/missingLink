package com.missinglink.backend.module.ride.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RideResponse {
    private UUID id;
    private UUID driverId;
    private String driverName;
    private String driverAvatarUrl;
    private BigDecimal driverRating;
    private String carName;
    private String carNumber;

    private String originCity;
    private String destinationCity;
    private String originAddress;
    private String destinationAddress;

    private Double originLat;
    private Double originLng;
    private Double destinationLat;
    private Double destinationLng;

    private LocalDateTime departureTime;
    private int totalSeats;
    private int availableSeats;
    private BigDecimal pricePerSeat;
    private String status;
    private String description;
    private boolean instantBooking;
    private LocalDateTime createdAt;
}
