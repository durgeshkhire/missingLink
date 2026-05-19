package com.missinglink.backend.module.vehicle.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehicleRequest {

    @NotBlank(message = "Car name is required")
    private String carName;

    @NotBlank(message = "Car number is required")
    private String carNumber;

    @NotBlank(message = "Car type is required")
    private String carType; // SEDAN, SUV, HATCHBACK

    @Min(value = 1, message = "Total seats must be at least 1")
    @Max(value = 7, message = "Total seats must be at most 7")
    private int totalSeats;

}
