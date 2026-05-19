package com.missinglink.backend.module.ride.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RideRequest {

//    @NotBlank(message = "Car name is required")
//    private String carName;
//
//    @NotBlank(message = "Car number is required")
//    private String carNumber;

    @NotNull(message = "Vehicle is required")
    private UUID vehicleId;

    @NotBlank(message = "Origin city is required")
    private String originCity;

    @NotBlank(message = "Destination city is required")
    private String destinationCity;

    private String originAddress;
    private String destinationAddress;

    private Double originLat;
    private Double originLng;

    private Double destinationLat;
    private Double destinationLng;


    @NotNull(message = "Departure time is required")
    @Future(message = "Departure time must be in the future")
    private LocalDateTime departureTime;

    @Min(value = 1, message = "At least 1 seat required")
    @Max(value = 7, message = "Maximum 7 seats allowed")
    private int totalSeats;

    @NotNull(message = "Price per seat is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal pricePerSeat;

    private String description;

    private boolean instantBooking = false;
}
